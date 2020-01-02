package com.byy.api.service.vo.product;

import com.byy.api.service.vo.comment.CommentVO;
import com.byy.dal.entity.beans.product.RentProductPic;
import com.byy.dal.entity.beans.product.RentProductTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 03:16
 * @Description:
 */
@Getter
@Setter
@ToString
public class RentProductVO {
  /** 租赁商品id */
  private Long id;

  /** 商品名 */
  private String name;

  /** 商品主图 */
  private String image;

  /** 库存 */
  private Integer amount;

  /** 租金(元/天) */
  private BigDecimal price;

  /** 押金 */
  private BigDecimal deposit;

  /** 成本价(元/天) */
  private BigDecimal costPrice;

  /** 上下架情况(上架:true,下架:false) */
  private Boolean onSale;

  /** 上级返佣比 */
  private BigDecimal parentCommissionRatio;

  /** 上上级返佣比 */
  private BigDecimal grandpaCommissionRatio;

  /** 租赁商品详情 */
  private String content;

  /** 详情图 */
  private List<RentProductPic> rentProductPics;

  /** 租期 */
  private List<RentProductTime> rentProductTimes;

  /** 租赁商品id(用于轮播图区分) */
  private Long rentProductId;

  /** 租赁商品评论 */
  private CommentVO commentVO;

  /** 评论总条数 */
  private Integer commentAmount;

  public Long getRentProductId(){
    return id;
  }
}
