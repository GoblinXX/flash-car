package com.byy.api.service.controller.coupon;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.coupon.UserCouponForm;
import com.byy.api.service.vo.coupon.UserCouponVO;
import com.byy.biz.service.coupon.CouponService;
import com.byy.biz.service.coupon.UserCouponService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.coupon.Coupon;
import com.byy.dal.entity.beans.coupon.UserCoupon;
import com.byy.dal.entity.dos.coupon.UserCouponDO;
import com.byy.dal.enums.UserCouponStatus;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.byy.api.response.ResponseObject.error;
import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.enums.UserCouponStatus.*;
import static com.byy.dal.enums.UserCouponStatus.USED;

/** @Author: xcf @Date: 18/06/19 下午 02:37 @Description: */
@RestController
@AllArgsConstructor
@RequestMapping("/coupon/user")
public class UserCouponController extends CommonController<UserCouponService> {

  private CouponService couponService;
  /**
   * 小程序端用户领取优惠券
   *
   * @param couponId
   * @return
   */
  @PutMapping("/app/receive/{couponId}")
  public ResponseObject<UserCouponVO> receiveCouponApp(@PathVariable("couponId") Long couponId) {
    Long userId = getCurrentUserId();
    Coupon coupon = couponService.getOne(WrapperProvider.queryWrapper(Coupon::getId, couponId));
    List<UserCoupon> userCouponList =
        baseService.list(WrapperProvider.queryWrapper(UserCoupon::getUserId, userId));
    // 判断优惠券是否还有剩余
    if (coupon.getRestAmount() <= 0) {
      return error(-1, "优惠券已被领完!");
    }
    // 判断该优惠券是否已领取过
    if (userCouponList.stream().anyMatch(userCoupon -> userCoupon.getCouponId().equals(couponId))) {
      return error(-1, "请勿重复领取!");
    }
    coupon.setRestAmount(coupon.getAmount() - 1);
    // 利用数据库锁限制优惠券领取
    boolean update =
        couponService.update(
            WrapperProvider.updateWrapper(Coupon::getId, coupon.getId())
                .gt(Coupon::getRestAmount, 0)
                .set(Coupon::getRestAmount, coupon.getRestAmount()));
    if (!update) {
      return error(-1, "优惠券已被领完!");
    }
    UserCoupon userCoupon = new UserCoupon();
    userCoupon.setCouponId(couponId);
    userCoupon.setUserId(userId);
    CheckHelper.trueOrThrow(baseService.save(userCoupon), BizException::new, "用户优惠券保存失败,请检查相应参数!");
    UserCoupon byId = baseService.getById(userCoupon.getId());
    return success(Transformer.fromBean(byId, UserCouponVO.class));
  }

  /**
   * 小程序端查询自己的优惠券
   *
   * @return
   */
  @GetMapping("/app/own")
  public ResponseObject<ImmutableMap<String, Object>> getOwnCouponApp(@Valid UserCouponForm form) {
    Long userId = getCurrentUserId();
    IPage<UserCouponDO> page = baseService.getOwnCouponApp(form.newPage(), userId);
    List<UserCouponVO> userCouponVOS = Transformer.fromList(page.getRecords(), UserCouponVO.class);
    userCouponVOS =
        userCouponVOS.stream()
            .peek(
                userCouponVO -> {
                  // 优惠券未被删除的情况下,判断是否过期等状态
                  LocalDateTime now = LocalDateTime.now();
                  UserCouponStatus userCouponStatus = USED;
                  if (now.isBefore(userCouponVO.getValidFrom())) {
                    userCouponStatus = NO_DATE_USE;
                  } else if (now.isAfter(userCouponVO.getValidTo())) {
                    if (userCouponVO.getUsable()) {
                      userCouponStatus = EXPIRED;
                    }
                  } else {
                    if (userCouponVO.getUsable()) {
                      userCouponStatus = NORMAL;
                    }
                  }
                  userCouponVO.setUserCouponStatus(userCouponStatus);
                })
            .collect(Collectors.toList());
    return success(ImmutableMap.of("list", userCouponVOS, "total", page.getTotal()));
  }

  /**
   * 小程序端查询用户可用的优惠券
   *
   * @param form
   * @return
   */
  @GetMapping("/app/usable")
  public ResponseObject<ImmutableMap<String, Object>> getOwnUsableCouponApp(
      @Valid UserCouponForm form) {
    Long userId = getCurrentUserId();
    LocalDateTime now = LocalDateTime.now();
    Map<String, Object> params = Maps.newHashMap();
    params.put("userId", userId);
    params.put("nowTime", now);
    IPage<UserCouponDO> page = baseService.getOwnUsableCouponApp(form.newPage(), params);
    List<UserCouponVO> userCouponVOS = Transformer.fromList(page.getRecords(), UserCouponVO.class);
    userCouponVOS =
        userCouponVOS.stream()
            .peek(userCouponVO -> userCouponVO.setUserCouponStatus(UserCouponStatus.NORMAL))
            .collect(Collectors.toList());
    return success(ImmutableMap.of("list", userCouponVOS, "total", page.getTotal()));
  }
}
