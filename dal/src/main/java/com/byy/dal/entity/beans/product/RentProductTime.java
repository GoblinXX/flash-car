package com.byy.dal.entity.beans.product;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 02:14
 * @Description:租期类
 */
@Setter
@Getter
@ToString
@Alias("RentProductTime")
public class RentProductTime extends BaseEntityArchive {
  /** 租期 */
  private Integer tenancy;

  /** 租金(元/天) */
  private BigDecimal price;

  /** 租赁商品id */
  private Long rentProductId;

}
