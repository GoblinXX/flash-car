package com.byy.biz.service.price.chain;

import com.byy.biz.service.coupon.UserCouponService;
import com.byy.biz.service.price.params.PromotionContext;
import com.byy.biz.service.price.promotion.CouponPromotion;
import com.byy.biz.service.price.view.CouponPromotionView;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.entity.dos.coupon.UserCouponDO;
import com.byy.dal.enums.PromotionType;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 优惠券计算链
 *
 * @author: yyc
 * @date: 19-6-17 下午5:57
 */
public class CouponPromotionPriceChain extends AbstractPromotionPriceChain {

  private final UserCouponService userCouponService;

  public CouponPromotionPriceChain(UserCouponService userCouponService) {
    this.userCouponService = userCouponService;
  }

  public void calculatePrice(PromotionContext context) {
    boolean canUsePromotion = canUsePromotion(context);
    // 设置前端可用优惠券展示列表(过滤掉不能分摊的优惠券)
    context.withCouponPromotionViews(loadCouponPromotionViews(context, canUsePromotion));
    if (canUsePromotion) {
      CouponPromotion usePromotion = (CouponPromotion) getCurrentPromotion(context);
      UserCouponDO userCouponDO = loadUserCouponById(context, usePromotion.getPromotionId());
      BigDecimal promotionDiscount = userCouponDO.getFaceValue();
      // 优惠分摊
      divideDiscount(promotionDiscount, context, true, true);
    }
  }

  /**
   * 查询前端可用优惠券展示列表
   *
   * @param context PromotionContext
   * @param canUsePromotion boolean
   * @return List
   */
  private List<CouponPromotionView> loadCouponPromotionViews(
      PromotionContext context, boolean canUsePromotion) {
    // 查询所有可用的优惠券
    return loadUserCoupons(context, canUsePromotion, null).stream()
        .map(CouponPromotionView::withUserCoupon)
        .collect(Collectors.toList());
  }

  /**
   * 根据id查询用户选择的当前优惠券
   *
   * @param context PromotionContext
   * @param promotionId Long
   * @return UserCouponDO
   */
  private UserCouponDO loadUserCouponById(PromotionContext context, Long promotionId) {
    CheckHelper.trueOrThrow(promotionId != null, BizException::new, "请选择优惠券");
    List<UserCouponDO> userCouponDOs = loadUserCoupons(context, true, promotionId);
    CheckHelper.trueOrThrow(
        !CollectionUtils.isEmpty(userCouponDOs) && userCouponDOs.size() == 1,
        BizException::new,
        "优惠券不满足条件");
    return userCouponDOs.get(0);
  }

  /**
   * 查询可用优惠券
   *
   * @param context PromotionContext
   * @param promotionId Long
   * @return List
   */
  private List<UserCouponDO> loadUserCoupons(
      PromotionContext context, boolean canUsePromotion, Long promotionId) {
    // 可使用优惠券不能低于成本价
    BigDecimal maxFaceValue = context.getGoodsFee().subtract(context.getCostFee());
    if (canUsePromotion) {
      maxFaceValue = maxFaceValue.subtract(context.getTotalDiscount());
    }
    Map<String, Object> params = Maps.newHashMap();
    params.put("userId", context.getUserId());
    params.put("maxFaceValue", maxFaceValue);
    params.put("goodsFee", context.getGoodsFee());
    if (promotionId != null) {
      params.put("useCouponId", promotionId);
    }
    // 筛选可分摊的优惠券
    return userCouponService.loadCanUseCoupons(params).stream()
        .filter(
            userCouponDO -> {
              BigDecimal canDivideDiscount =
                  divideDiscount(userCouponDO.getFaceValue(), context, false, canUsePromotion);
              return canDivideDiscount.compareTo(userCouponDO.getFaceValue()) >= 0;
            })
        .collect(Collectors.toList());
  }

  @Override
  protected PromotionType promotionType() {
    return PromotionType.COUPON;
  }

  @Override
  protected Set<PromotionType> rejectPromotionTypes() {
    return ImmutableSet.of(PromotionType.POINT);
  }
}
