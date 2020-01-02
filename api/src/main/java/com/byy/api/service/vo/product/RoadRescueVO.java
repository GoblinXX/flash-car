package com.byy.api.service.vo.product;

import com.byy.dal.entity.beans.product.RoadRescue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 12/06/19 下午 08:34
 * @Description:
 */
@Setter
@Getter
@ToString
public class RoadRescueVO{

  /** 道路救援id */
  private Long id;

  /** 名称 */
  private String name;

  /** 价格 */
  private BigDecimal price;

  /** 图片 */
  private String picture;
}
