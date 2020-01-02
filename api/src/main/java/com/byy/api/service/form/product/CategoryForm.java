package com.byy.api.service.form.product;

import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.page.PageForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author: xcf
 * @Date: 12/06/19 下午 01:36
 * @Description:商品分类form
 */
@Getter
@Setter
@ToString
public class CategoryForm implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;
  /** 分类id */
  private Long id;
  /** 分类名 */
  private String name;


}
