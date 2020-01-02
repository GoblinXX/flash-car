package com.byy.dal.entity.beans.product;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.*;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 12/06/19 下午 02:31
 * @Description:商品图片类
 */
@Setter
@Getter
@ToString
@Alias("ProductPic")
public class ProductPic extends BaseEntityArchive {
  /** 图片地址 */
  private String picture;
  /** 商品id */
  private Long productId;
}
