package com.byy.dal.entity.beans.product;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.*;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 12/06/19 上午 11:51
 * @Description:商品分类类
 */
@Getter
@Setter
@ToString
@Alias("Category")
public class Category extends BaseEntityArchive {
  /** 分类名 */
  private String name;
}
