package com.byy.dal.entity.beans.product;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.*;
import org.apache.ibatis.type.Alias;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 12/06/19 上午 09:35
 * @Description:商品类
 */
@Setter
@Getter
@ToString
@Alias("Product")
public class Product extends BaseEntityArchive {

  /** 商品名 */
  private String name;

  /** 商品主图 */
  private String image;

  /** 安装费 */
  private BigDecimal installFee;

  /** 服务费 */
  private BigDecimal serviceFee;

  /** 分类id */
  private Long categoryId;

  /** 上级返佣比 */
  private BigDecimal parentCommissionRatio;

  /** 上上级返佣比 */
  private BigDecimal grandpaCommissionRatio;

  /** 商品详情 */
  private String content;

  /** 上下架情况(上架:true,下架:false) */
  private Boolean onSale;

  /** 热门情况(热门:true,非热门:false) */
  private Boolean onHot;






}
