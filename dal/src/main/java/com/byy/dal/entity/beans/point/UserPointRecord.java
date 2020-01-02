package com.byy.dal.entity.beans.point;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import com.byy.dal.enums.SceneType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 02:06
 * @Description:积分明细类
 */
@Getter
@Setter
@ToString
@Alias("UserPointRecord")
public class UserPointRecord extends BaseEntityArchiveWithUserId {

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
