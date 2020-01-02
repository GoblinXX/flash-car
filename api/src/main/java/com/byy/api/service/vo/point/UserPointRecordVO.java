package com.byy.api.service.vo.point;

import com.byy.dal.enums.SceneType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 05:09
 * @Description:
 */
@Getter
@Setter
@ToString
public class UserPointRecordVO {

  /** 记录日期 */
  private LocalDateTime createdAt;

  /** 场景：评论成功后、签到、邀请新用户首次下单获得、​订单取消、订单退款、订单抵扣 */
  private SceneType scene;

  /** 积分记录  正或负 */
  private BigDecimal record;

  /** 历史积分(计算之前的积分) */
  private BigDecimal previousPoint;

  /** 当前积分(计算之后的积分) */
  private BigDecimal currentPoint;

  /** 订单编号 */
  private String orderNo;

}
