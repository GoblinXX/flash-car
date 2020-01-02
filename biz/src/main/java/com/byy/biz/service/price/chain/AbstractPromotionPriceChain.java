package com.byy.biz.service.price.chain;

import com.byy.biz.service.price.cart.SkuItem;
import com.byy.biz.service.price.detail.SkuPromotionDetail;
import com.byy.biz.service.price.params.PromotionContext;
import com.byy.biz.service.price.promotion.Promotion;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.enums.PromotionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * 优惠计算链
 *
 * @author: yyc
 * @date: 19-6-18 下午7:18
 */
@Setter
@Getter
public abstract class AbstractPromotionPriceChain {

  /** 下一个计算链 */
  private AbstractPromotionPriceChain nextChain;

  /**
   * 优惠计算
   *
   * @param param PromotionContext
   * @return PromotionResult
   */
  public void calculate(PromotionContext param) {
    if (!skipCurrent()) {
      this.calculatePrice(param);
    }
    if (hasNext()) {
      getNextChain().calculate(param);
    }
  }

  /**
   * 价格计算
   *
   * @param context PromotionContext
   */
  protected abstract void calculatePrice(PromotionContext context);

  /**
   * 当前优惠类型
   *
   * @return PromotionType
   */
  protected abstract PromotionType promotionType();

  /**
   * 互斥的优惠类型
   *
   * @return Set
   */
  protected abstract Set<PromotionType> rejectPromotionTypes();

  /**
   * 判断是否还有计算链
   *
   * @return boolean
   */
  protected boolean hasNext() {
    return getNextChain() != null;
  }

  /**
   * 是否跳过当前计算链
   *
   * @return boolean
   */
  protected boolean skipCurrent() {
    return false;
  }

  /**
   * 是否可以计算当前优惠
   *
   * @return boolean
   */
  protected boolean canUsePromotion(PromotionContext param) {
    // 是否已经有互斥的优惠计算过
    boolean hasReject =
        param.getUsePromotionMap().keySet().stream()
            .anyMatch(promotionType -> rejectPromotionTypes().contains(promotionType));
    if (hasReject) {
      return false;
    }
    // 当前订单合计(商品总价-订单折扣)小于成本价,不参与分摊计算
    if (param.getGoodsFee().subtract(param.getTotalDiscount()).compareTo(param.getCostFee()) <= 0) {
      return false;
    }
    // 优惠不为空且用户选择的优惠类型为当前优惠链类型
    return !CollectionUtils.isEmpty(param.getSelectPromotionMap())
        && param.getSelectPromotionMap().containsKey(promotionType());
  }

  /**
   * 获取当前计算链优惠
   *
   * @param context PromotionContext
   * @return Promotion
   */
  protected Promotion getCurrentPromotion(PromotionContext context) {
    return context.getSelectPromotionMap().get(promotionType());
  }

  /**
   * 获取大于0的BigDecimal最小值
   *
   * @param args BigDecimal
   * @return BigDecimal
   */
  protected BigDecimal getMinValue(BigDecimal... args) {
    BigDecimal min =
        Arrays.stream(args)
            .min(BigDecimal::compareTo)
            .orElseThrow(() -> new BizException("未找到最小值"));
    return min.compareTo(BigDecimal.ZERO) > 0 ? min : BigDecimal.ZERO;
  }

  /**
   * 计算优惠分摊
   *
   * @param promotionDiscount BigDecimal
   * @param context PromotionContext
   * @param isFormal boolean (是否累加优惠,false不累加,true累加)
   * @param canUsePromotion boolean
   * @return BigDecimal
   */
  protected BigDecimal divideDiscount(
      BigDecimal promotionDiscount,
      PromotionContext context,
      boolean isFormal,
      boolean canUsePromotion) {
    // 已分摊优惠
    BigDecimal hasDividedDiscount = BigDecimal.ZERO;
    List<SkuItem> skuItems = context.getSkuItems();
    // 按照最大优惠分摊排序skuItem
    sortSkuItem(skuItems, canUsePromotion);
    BigDecimal goodsFee = context.getGoodsFee();
    int index = 1;
    int size = skuItems.size();
    for (SkuItem item : skuItems) {
      BigDecimal amount = item.getAmount();
      BigDecimal itemPrice = item.getPrice().multiply(amount);
      BigDecimal itemTotalCostPrice = item.getCostPrice().multiply(amount);
      // 如果当前优惠链可以计算优惠,用item优惠价减去成本价,无法计算优惠,则用item原价减去成本价
      BigDecimal reducePrice =
          canUsePromotion
              ? item.getItemTotalPrice().subtract(itemTotalCostPrice)
              : itemPrice.subtract(itemTotalCostPrice);
      // item差价小于等于0时,不计算优惠
      if (reducePrice.compareTo(BigDecimal.ZERO) <= 0) {
        continue;
      }
      BigDecimal currentItemDiscount;
      if (index < size) {
        // 优惠分摊
        currentItemDiscount =
            itemPrice.multiply(promotionDiscount).divide(goodsFee, 1, BigDecimal.ROUND_HALF_EVEN);
      } else {
        // 补偿精度
        currentItemDiscount = promotionDiscount.subtract(hasDividedDiscount);
      }
      currentItemDiscount = getMinValue(currentItemDiscount, reducePrice);
      hasDividedDiscount = hasDividedDiscount.add(currentItemDiscount);
      // 记录每个item优惠详情
      addPromotionDetail(isFormal, item, currentItemDiscount);
      index++;
    }
    // 记录订单优惠详情
    addUsePromotion(isFormal, context, hasDividedDiscount);
    return hasDividedDiscount;
  }

  /**
   * 优惠分摊排序(price/price-costPrice从大到小排序规则)
   *
   * @param skuItems List
   * @param canUsePromotion boolean
   */
  private static void sortSkuItem(List<SkuItem> skuItems, boolean canUsePromotion) {
    skuItems.sort(
        Comparator.comparing(
            item -> {
              BigDecimal itemPrice = item.getPrice().multiply(item.getAmount());
              BigDecimal itemTotalCostPrice = item.getCostPrice().multiply(item.getAmount());
              // 如果当前优惠链可以计算优惠,用item优惠价减去成本价,无法计算优惠,则用item原价减去成本价
              BigDecimal reducePrice =
                  canUsePromotion
                      ? item.getItemTotalPrice().subtract(itemTotalCostPrice)
                      : itemPrice.subtract(itemTotalCostPrice);
              // 商品售价或优惠价等于成本价
              if (reducePrice.compareTo(BigDecimal.ZERO) <= 0) {
                return BigDecimal.valueOf(-1);
              }
              return itemPrice.divide(reducePrice, 1, BigDecimal.ROUND_HALF_EVEN).negate();
            }));
  }

  /**
   * 添加item优惠详情
   *
   * @param item SkuItem
   * @param currentItemDiscount BigDecimal
   * @param isFormal 是否添加优惠
   */
  private void addPromotionDetail(boolean isFormal, SkuItem item, BigDecimal currentItemDiscount) {
    if (!isFormal) {
      return;
    }
    BigDecimal promotionTotalPrice = item.getItemTotalPrice().subtract(currentItemDiscount);
    // 添加item优惠详情
    item.withItemTotalPrice(promotionTotalPrice)
        .withPromotionTotalDiscount(item.getPromotionTotalDiscount().add(currentItemDiscount))
        .getSkuPromotionDetails()
        .add(
            new SkuPromotionDetail()
                .withPromotionType(promotionType())
                .withPromotionTotalDiscount(currentItemDiscount));
  }

  /**
   * 记录订单优惠详情
   *
   * @param context PromotionContext
   * @param promotionDiscount BigDecimal
   * @param isFormal 是否累加优惠
   */
  private void addUsePromotion(
      boolean isFormal, PromotionContext context, BigDecimal promotionDiscount) {
    if (!isFormal) {
      return;
    }
    PromotionType promotionType = promotionType();
    Promotion promotion =
        context.getSelectPromotionMap().get(promotionType).withPromotionDiscount(promotionDiscount);
    // 累加订单优惠详情
    context
        .withTotalDiscount(context.getTotalDiscount().add(promotionDiscount))
        .getUsePromotionMap()
        .put(promotionType, promotion);
  }
}
