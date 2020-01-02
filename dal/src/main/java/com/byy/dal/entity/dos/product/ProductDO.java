package com.byy.dal.entity.dos.product;

import com.byy.dal.entity.beans.product.Product;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 12/06/19 上午 09:59
 * @Description:
 */
@Getter
@Setter
@ToString
@Alias("ProductDO")
public class ProductDO extends Product {

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



}
