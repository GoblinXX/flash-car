package com.byy.biz.service.price.chain;

import com.byy.biz.service.point.UserPointService;
import com.byy.biz.service.price.params.PromotionContext;
import com.byy.biz.service.price.promotion.PointPromotion;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.point.UserPoint;
import com.byy.dal.enums.PromotionType;
import com.google.common.collect.ImmutableSet;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 积分计算链
 *
 * @author: yyc
 * @date: 19-6-17 下午5:57
 */
public class PointPromotionPriceChain extends AbstractPromotionPriceChain {

  private final UserPointService userPointService;

  public PointPromotionPriceChain(UserPointService userPointService) {
    this.userPointService = userPointService;
  }

  @Override
  protected PromotionType promotionType() {
    return PromotionType.POINT;
  }

  @Override
  public void calculatePrice(PromotionContext context) {
    boolean canUsePromotion = canUsePromotion(context);
    // 查询出订单最大可用积分
    BigDecimal maxUsePoint = loadMaxCanUsePoint(context, canUsePromotion);
    if (canUsePromotion) {
      PointPromotion usePromotion = (PointPromotion) getCurrentPromotion(context);
      BigDecimal selectPoint = usePromotion.getPoint();
      if (selectPoint.compareTo(BigDecimal.ZERO) <= 0) {
        return;
      }
      CheckHelper.trueOrThrow(
          selectPoint.compareTo(maxUsePoint) <= 0, BizException::new, "不能超过最大可用积分");
      // 优惠分摊
      divideDiscount(
          selectPoint.divide(context.getRatio(), 1, BigDecimal.ROUND_DOWN), context, true, true);
    }
    // 设置前端展示的最大可用积分
    context.setCanUsePoint(maxUsePoint);
  }

  /**
   * 查询出订单最大可用积分
   *
   * @param context PromotionContext
   * @return BigDecimal
   */
  private BigDecimal loadMaxCanUsePoint(PromotionContext context, boolean canUsePromotion) {
    // 订单可用积分折扣不能小于成本价
    BigDecimal maxDiscount = context.getGoodsFee().subtract(context.getCostFee());
    if (canUsePromotion) {
      maxDiscount = maxDiscount.subtract(context.getTotalDiscount());
    }
    // 1.根据成本折扣计算出的可用积分
    BigDecimal maxPoint = maxDiscount.multiply(context.getRatio());
    // 2.当前用户可用积分
    UserPoint userPoint =
        userPointService.getOne(
            WrapperProvider.oneQueryWrapper(UserPoint::getUserId, context.getUserId()));
    BigDecimal availablePoint = userPoint == null ? BigDecimal.ZERO : userPoint.getAvailablePoint();
    // 3.分摊限制的可用积分
    BigDecimal canDivideDiscountToPoint =
        divideDiscount(maxDiscount, context, false, canUsePromotion).multiply(context.getRatio());
    // 取最少限制的积分(即订单最多可用积分)
    return getMinValue(maxPoint, availablePoint, canDivideDiscountToPoint)
        .setScale(0, BigDecimal.ROUND_DOWN);
  }

  @Override
  protected Set<PromotionType> rejectPromotionTypes() {
    return ImmutableSet.of(PromotionType.COUPON);
  }
}
