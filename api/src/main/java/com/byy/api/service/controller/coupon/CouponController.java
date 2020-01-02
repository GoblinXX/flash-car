package com.byy.api.service.controller.coupon;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.coupon.CouponForm;
import com.byy.api.service.vo.coupon.CouponVO;
import com.byy.biz.service.coupon.CouponService;
import com.byy.biz.service.coupon.UserCouponService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.coupon.Coupon;
import com.byy.dal.entity.beans.coupon.UserCoupon;
import com.byy.dal.enums.CouponStatus;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.enums.CouponStatus.*;

/** @Author: xcf @Date: 15/06/19 下午 02:08 @Description: */
@RestController
@AllArgsConstructor
@RequestMapping("/coupon")
public class CouponController extends CommonController<CouponService> {

  private UserCouponService userCouponService;

  /**
   * 后台分页查询优惠券
   *
   * @param form
   * @return
   */
  @GetMapping("/back/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllCouponSys(@Valid CouponForm form) {
    IPage<Coupon> page = baseService.page(form.newPage());
    ImmutableMap<String, Object> map =
        ImmutableMap.of(
            "list",
            Transformer.fromList(page.getRecords(), CouponVO.class),
            "total",
            page.getTotal());
    return success(map);
  }

  /**
   * 后台添加优惠券
   *
   * @param form
   * @return
   */
  @PostMapping("/back")
  public ResponseObject<CouponVO> saveCouponSys(@RequestBody CouponForm form) {
    Coupon coupon = Transformer.fromBean(form, Coupon.class);
    coupon.setRestAmount(coupon.getAmount());
    CheckHelper.trueOrThrow(baseService.save(coupon), BizException::new, "添加优惠券失败,请检查相应参数!");
    return success(Transformer.fromBean(baseService.getById(coupon.getId()), CouponVO.class));
  }

  /**
   * 后台删除优惠券
   *
   * @param couponId
   * @return
   */
  @DeleteMapping("/back/{couponId}")
  public ResponseObject<Object> removeCouponSys(@PathVariable("couponId") Long couponId) {
    CheckHelper.trueOrThrow(
        baseService.removeById(couponId), BizException::new, "删除优惠券失败,请检查相应参数!");
    return success(couponId);
  }

  /**
   * 小程序端展示所有优惠券
   *
   * @param form
   * @return
   */
  @GetMapping("/app/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllCouponApp(@Valid CouponForm form) {
    Long userId = getCurrentUserId();
    List<UserCoupon> userCouponList =
        userCouponService.list(WrapperProvider.queryWrapper(UserCoupon::getUserId, userId));
    IPage<Coupon> page =
        baseService.page(
            form.newPage(),
            Wrappers.<Coupon>lambdaQuery().ge(Coupon::getValidTo, LocalDateTime.now()));
    List<CouponVO> couponVOS = Transformer.fromList(page.getRecords(), CouponVO.class);
    // 区分自己是否已拥有该优惠券
    if (userCouponList.size() > 0) {
      couponVOS =
          couponVOS.stream()
              .peek(
                  couponVO -> {
                    couponVO.setPossession(
                        userCouponList.stream()
                            .anyMatch(
                                userCoupon -> userCoupon.getCouponId().equals(couponVO.getId())));
                    // 设置优惠券状态
                    setCouponStatus(couponVO);
                  })
              .collect(Collectors.toList());
    } else {
      couponVOS =
          couponVOS.stream()
              .peek(
                  couponVO -> {
                    couponVO.setPossession(false);
                    // 设置优惠券状态
                    setCouponStatus(couponVO);
                  })
              .collect(Collectors.toList());
    }
    return success(ImmutableMap.of("list", couponVOS, "total", page.getTotal()));
  }

  /**
   * 设置优惠券状态
   *
   * @param couponVO
   * @return
   */
  private void setCouponStatus(CouponVO couponVO) {
    LocalDateTime now = LocalDateTime.now();
    CouponStatus couponStatus = NORMAL;
    if (couponVO.getRestAmount() <= 0) {
      couponStatus = FINISHED;
    } else {
      if (now.isBefore(couponVO.getValidFrom())) {
        couponStatus = UNEXPIRED;
      } else if (now.isAfter(couponVO.getValidTo())) {
        couponStatus = EXPIRED;
      }
    }
    couponVO.setCouponStatus(couponStatus);
  }
}
