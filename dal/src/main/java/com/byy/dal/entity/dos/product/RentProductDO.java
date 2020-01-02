package com.byy.dal.entity.dos.product;

import com.byy.dal.entity.beans.product.RentProduct;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 02:12
 * @Description:
 */
@Getter
@Setter
@ToString
@Alias("RentProductDO")
public class RentProductDO extends RentProduct {

  /** 租金(元/天) */
  private BigDecimal price;


}
