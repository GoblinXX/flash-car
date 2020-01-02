package com.byy.api.service.controller.information;

import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.vo.information.InformationVO;
import com.byy.biz.service.coupon.UserCouponService;
import com.byy.biz.service.distribution.DistributionCenterService;
import com.byy.biz.service.forum.InvitationReplyService;
import com.byy.biz.service.order.OrderService;
import com.byy.biz.service.point.UserPointService;
import com.byy.biz.service.punch.UserPunchService;
import com.byy.biz.service.refundorder.RefundOrderService;
import com.byy.biz.service.roadrescueorder.RoadRescueOrderService;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.coupon.UserCoupon;
import com.byy.dal.entity.beans.distribution.DistributionCenter;
import com.byy.dal.entity.beans.forum.InvitationReply;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.beans.point.UserPoint;
import com.byy.dal.entity.beans.punch.UserPunch;
import com.byy.dal.entity.beans.refundorder.RefundOrder;
import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import com.byy.dal.entity.beans.wechat.WeChatUser;
import com.byy.dal.enums.OrderStatus;
import com.byy.dal.enums.RefundStatus;
import com.byy.dal.enums.RoadType;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.enums.OrderStatus.*;
import static com.byy.dal.enums.OrderType.*;

/** @Author: xcf @Date: 18/06/19 下午 02:34 @Description: */
@RestController
@AllArgsConstructor
@RequestMapping("/information")
public class InformationController extends CommonController<WeChatUserService> {

  private DistributionCenterService distributionCenterService;

  private UserPointService userPointService;

  private UserCouponService userCouponService;

  private UserPunchService userPunchService;

  private InvitationReplyService invitationReplyService;

  private OrderService orderService;

  private RoadRescueOrderService roadRescueOrderService;

  private RefundOrderService refundOrderService;

  /**
   * 小程序端-我的
   *
   * @return
   */
  @GetMapping
  public ResponseObject<InformationVO> getInformationApp() {
    Long userId = getCurrentUserId();
    WeChatUser user = baseService.getById(userId);
    InformationVO informationVO = Transformer.fromBean(user, InformationVO.class);
    informationVO.setCurrentBalance(
        distributionCenterService
            .getOne(WrapperProvider.queryWrapper(DistributionCenter::getUserId, userId))
            .getCurrentBalance());
    informationVO.setAvailablePoint(
        userPointService
            .getOne(WrapperProvider.queryWrapper(UserPoint::getUserId, userId))
            .getAvailablePoint());
    informationVO.setCouponAmount(
        userCouponService.count(WrapperProvider.queryWrapper(UserCoupon::getUserId, userId)));
    UserPunch userPunch =
        userPunchService.getOne(WrapperProvider.queryWrapper(UserPunch::getUserId, userId));
    if (userPunch == null) {
      userPunch = new UserPunch();
      userPunch.setUserId(userId);
      userPunch.setAccumulatedDays(0);
      CheckHelper.trueOrThrow(
          userPunchService.save(userPunch), BizException::new, "用户打卡添加失败,请检查相应参数!");
    }
    LocalDateTime now = LocalDateTime.now();
    long index =
        now.toLocalDate().toEpochDay() - userPunch.getUpdatedAt().toLocalDate().toEpochDay();
    if (index <= 1) {
      informationVO.setAccumulatedDays(userPunch.getAccumulatedDays());
    } else {
      userPunch.setAccumulatedDays(0);
      userPunch.setPunchTime(now);
      CheckHelper.trueOrThrow(
          userPunchService.updateById(userPunch), BizException::new, "签到天数修改失败,请检查相应参数!");
      informationVO.setAccumulatedDays(userPunch.getAccumulatedDays());
    }
    // 查出未读信息条数
    int unreadReplyCount =
        invitationReplyService.count(
            WrapperProvider.queryWrapper(InvitationReply::getOwnerId, userId)
                .eq(InvitationReply::getWatched, 0));
    informationVO.setUnreadReplyCount(unreadReplyCount);
    // 查出代付款订单条数
    int submittedOrderCount =
        orderService.count(
            WrapperProvider.queryWrapper(Order::getUserId, userId)
                .eq(Order::getStatus, OrderStatus.SUBMITTED)
                .and(R -> R.eq(Order::getOrderType, STORE).or().eq(Order::getOrderType, HOME)));
    int submittedRoadOrderCount =
        roadRescueOrderService.count(
            WrapperProvider.queryWrapper(RoadRescueOrder::getUserId, userId)
                .eq(RoadRescueOrder::getStatus, RoadType.SUBMITTED)
                .eq(RoadRescueOrder::getOrderType, ROAD_RESCUE));
    informationVO.setSubmittedCount(submittedOrderCount + submittedRoadOrderCount);
    // 查出代发货订单条数
    int paidOrderCount =
        orderService.count(
            WrapperProvider.queryWrapper(Order::getUserId, userId)
                .eq(Order::getStatus, OrderStatus.PAID)
                .and(R -> R.eq(Order::getOrderType, STORE).or().eq(Order::getOrderType, HOME)));
    int paidRoadOrderCount =
        roadRescueOrderService.count(
            WrapperProvider.queryWrapper(RoadRescueOrder::getUserId, userId)
                .eq(RoadRescueOrder::getStatus, RoadType.PAID)
                .eq(RoadRescueOrder::getOrderType, ROAD_RESCUE));
    informationVO.setPaidCount(paidOrderCount + paidRoadOrderCount);
    // 查出待收获状态订单条数
    int shippedOrderCount =
        orderService.count(
            WrapperProvider.queryWrapper(Order::getUserId, userId)
                .eq(Order::getStatus, OrderStatus.SHIPPED)
                .and(R -> R.eq(Order::getOrderType, STORE).or().eq(Order::getOrderType, HOME)));
    int shippedRoadOrderCount =
        roadRescueOrderService.count(
            WrapperProvider.queryWrapper(RoadRescueOrder::getUserId, userId)
                .eq(RoadRescueOrder::getStatus, RoadType.PROCESSING)
                .eq(RoadRescueOrder::getOrderType, ROAD_RESCUE));
    informationVO.setShippedCount(shippedOrderCount + shippedRoadOrderCount);
    // 查出待评价订单条数
    int receivedOrderCount =
        orderService.count(
            WrapperProvider.queryWrapper(Order::getUserId, userId)
                .eq(Order::getStatus, OrderStatus.RECEIVED)
                .and(R -> R.eq(Order::getOrderType, STORE).or().eq(Order::getOrderType, HOME)));
    informationVO.setReceivedCount(receivedOrderCount);
    // 查出售后订单条数
    int refundingOrderCount =
        refundOrderService.count(
            WrapperProvider.queryWrapper(RefundOrder::getUserId, userId)
                .eq(RefundOrder::getStatus, RefundStatus.AUDITING)
                .and(
                    R ->
                        R.eq(RefundOrder::getOrderType, STORE)
                            .or()
                            .eq(RefundOrder::getOrderType, HOME)
                            .or()
                            .eq(RefundOrder::getOrderType, ROAD_RESCUE)));
    informationVO.setRefundingCount(refundingOrderCount);
    return success(informationVO);
  }
}
