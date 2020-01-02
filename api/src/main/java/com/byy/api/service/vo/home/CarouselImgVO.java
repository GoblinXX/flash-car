package com.byy.api.service.vo.home;

import com.byy.dal.enums.JumpType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: xcf
 * @Date: 19/06/19 下午 02:52
 * @Description:
 */
@Getter
@Setter
@ToString
public class CarouselImgVO {

  /** id */
  private Long id;

  /** 首页展示图片 */
  private String img;

  /** 标题 */
  private String title;

  /** 跳转类型 */
  private JumpType jumpType;

  /** 跳转类型Str */
  private String jumpTypeStr;

  /** 跳转图片 */
  private String jumpImg;

  /** 跳转商品id */
  private Long productId;

  /** 租赁商品id */
  private Long rentProductId;

  /** 商品名 */
  private String productName;

  /** 租赁商品名 */
  private String rentProductName;

  /** 分类id */
  private Long categoryId;

  /** 分类名 */
  private String categoryName;


  public String getJumpTypeStr(){
    return jumpType != null ? jumpType.getTypeName() : null;
  }
}
