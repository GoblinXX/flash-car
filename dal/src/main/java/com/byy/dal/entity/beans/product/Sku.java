package com.byy.dal.entity.beans.product;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Author: xcf
 * @Date: 12/06/19 下午 02:09
 * @Description:规格类
 */
@Setter
@Getter
@ToString
@Alias("Sku")
public class Sku extends BaseEntityArchive {
  /** 规格名称 */
  private String name;
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
  /** 商品id */
  private Long productId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Sku)) return false;
    Sku sku = (Sku) o;
    return Objects.equals(name, sku.name) &&
            Objects.equals(salePrice, sku.salePrice) &&
            Objects.equals(originalPrice, sku.originalPrice) &&
            Objects.equals(costPrice, sku.costPrice) &&
            Objects.equals(amount, sku.amount) &&
            Objects.equals(productId, sku.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, salePrice, originalPrice, costPrice, amount, productId);
  }

}
