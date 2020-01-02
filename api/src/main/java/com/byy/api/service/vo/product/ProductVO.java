package com.byy.api.service.vo.product;

import com.byy.api.service.vo.comment.CommentVO;
import com.byy.dal.entity.beans.product.ProductPic;
import com.byy.dal.entity.beans.product.Sku;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 12/06/19 上午 11:43
 * @Description:商品vo
 */
@Getter
@Setter
@ToString
public class ProductVO {

  /** 商品id */
  private Long id;

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

  /** 分类名 */
  private String categoryName;

  /** 售价 */
  private BigDecimal salePrice;

  /** 原价 */
  private BigDecimal originalPrice;

  /** 成本价 */
  private BigDecimal costPrice;

  /** 库存 */
  private Integer amount;

  /** 销量 */
  private Integer saleAmount;

  /** 详情图 */
  private List<ProductPic> picList;
  /** 规格 */
  private List<Sku> skuList;

  /** 商品id(用于轮播图区分) */
  private Long productId;

  /** 评论对象 */
  private CommentVO commentVO;

  /** 评论总条数 */
  private Integer commentAmount;

  public Long getProductId(){
    return id;
  }

}
