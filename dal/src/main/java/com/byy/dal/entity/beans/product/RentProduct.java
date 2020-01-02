package com.byy.dal.entity.beans.product;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 01:55
 * @Description:租赁商品类
 */
@Setter
@Getter
@ToString
@Alias("RentProduct")
public class RentProduct extends BaseEntityArchive {

  /** 商品名 */
  private String name;

  /** 商品主图 */
  private String image;

  /** 库存 */
  private Integer amount;

  /** 成本价(元/天) */
  private BigDecimal costPrice;

  /** 押金 */
  private BigDecimal deposit;

  /** 上级返佣比 */
  private BigDecimal parentCommissionRatio;

  /** 上上级返佣比 */
  private BigDecimal grandpaCommissionRatio;

  /** 租赁商品详情 */
  private String content;

  /** 上下架情况(上架:true,下架:false) */
  private Boolean onSale;



}
