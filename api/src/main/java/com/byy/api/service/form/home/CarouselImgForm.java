package com.byy.api.service.form.home;

import com.byy.api.service.form.page.IPageForm;
import com.byy.dal.enums.JumpType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author: xcf
 * @Date: 19/06/19 下午 02:38
 * @Description:
 */
@Getter
@Setter
@ToString
public class CarouselImgForm implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;

  /** 商品或租赁商品(商品:true  租赁商品:false) */
  private Boolean productOrRent;

  /** 轮播图id */
  private Long id;

  /** 商品分类id */
  private Long categoryId;

  /** 商品id */
  private Long productId;

  /** 租赁商品id */
  private Long rentProductId;

  /** 首页展示图片 */
  private String img;

  /** 标题 */
  private String title;

  /** 跳转类型 */
  private JumpType jumpType;

  /** 跳转图片 */
  private String jumpImg;

}
