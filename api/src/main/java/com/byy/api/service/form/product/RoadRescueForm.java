package com.byy.api.service.form.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 12/06/19 下午 08:41
 * @Description:
 */
@Setter
@Getter
@ToString
public class RoadRescueForm {
  /** id */
  private Long id;

  /** 名称 */
  private String name;

  /** 价格 */
  private BigDecimal price;

  /** 图片 */
  private String picture;

}
