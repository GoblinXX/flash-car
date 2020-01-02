package com.byy.dal.entity.beans.product;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.*;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 03:37
 * @Description:
 */
@Setter
@Getter
@ToString
@Alias("RentProductPic")
public class RentProductPic extends BaseEntityArchive {
  /** 图片地址 */
  private String picture;
  /** 商品id */
  private Long rentProductId;
}
