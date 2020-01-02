package com.byy.api.service.form.product;

import com.byy.api.service.form.page.IPageForm;
import com.byy.dal.entity.beans.product.Sku;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 12/06/19 上午 11:33
 * @Description:商品form
 */
@Getter
@Setter
@ToString
public class ProductForm implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;
  /** 商品名模糊字段 */
  private String keyword;
  /** 分类id */
  private Long categoryId;
  /** 上下架状态(上架:true,下架:false) */
  private Boolean onSale;
  /** 商品id */
  private Long id;
  /** 商品名 */
  private String name;
  /** 商品主图 */
  private String image;
  /** 商品详情图 */
  private List<String> productPics;
  /** 安装费 */
  private BigDecimal installFee;
  /** 服务费 */
  private BigDecimal serviceFee;
  /** 规格 */
  private List<Sku> skuList;
  /** 上级返佣比 */
  private BigDecimal parentCommissionRatio;
  /** 上上级返佣比 */
  private BigDecimal grandpaCommissionRatio;
  /** 商品详情 */
  private String content;
  /** 热门情况(热门:true,非热门:false) */
  private Boolean onHot;

}
