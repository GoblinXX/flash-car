package com.byy.api.service.vo.information;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 18/06/19 下午 02:53
 * @Description:
 */
@Setter
@Getter
@ToString
public class InformationVO {
  /** 用户id */
  private Long id;

  /** 昵称 */
  private String nickname;

  /** 头像 */
  private String avatar;

  /** 余额(佣金,t_distribution_center表) */
  private BigDecimal currentBalance;

  /** 积分(可用积分,t_user_point表) */
  private BigDecimal availablePoint;

  /** 用户优惠券数量(count,t_user_coupon表) */
  private Integer couponAmount;

  /** 连续打卡天数(t_user_punch表) */
  private Integer accumulatedDays;

  /** 未读信息条数 */
  private Integer unreadReplyCount;

  /** 代付款状态订单条数 */
  private Integer submittedCount;

  /** 代发货状态订单条数 */
  private Integer paidCount;

  /** 待收货订单条数 */
  private Integer shippedCount;

  /** 待评价订单条数 */
  private Integer receivedCount;

  /** 售后订单条数 */
  private Integer refundingCount;
}
