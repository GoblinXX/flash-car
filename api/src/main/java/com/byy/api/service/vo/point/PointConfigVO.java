package com.byy.api.service.vo.point;

import com.byy.dal.enums.SceneType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 04:08
 * @Description:
 */
@Getter
@Setter
@ToString
public class PointConfigVO {
  /** id */
  private Long id;

  /** 可获得积分场景 */
  private SceneType scene;

  /** 可获得积分*/
  private BigDecimal amount;
}
