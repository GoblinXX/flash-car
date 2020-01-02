package com.byy.api.service.vo.product;

import com.byy.dal.entity.beans.product.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: xcf
 * @Date: 12/06/19 下午 12:02
 * @Description:商品分类vo
 */
@Getter
@Setter
@ToString
public class CategoryVO{
  /** 分类id */
  private Long id;
  /** 分类名 */
  private String name;
  /** 分类id(轮播图用于区分) */
  private Long categoryId;

  public Long getCategoryId(){
    return id;
  }

}
