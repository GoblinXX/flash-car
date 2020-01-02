package com.byy.dal.entity.beans.home;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import com.byy.dal.enums.JumpType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 19/06/19 下午 02:07
 * @Description:轮播图
 */
@Getter
@Setter
@ToString
@Alias("CarouselImg")
public class CarouselImg extends BaseEntityArchive {
  /** 首页展示图片 */
  private String img;

  /** 标题 */
  private String title;

  /** 跳转类型 */
  private JumpType jumpType;

  /** 跳转图片 */
  private String jumpImg;

  /** 跳转商品id */
  private Long productId;

  /** 租赁商品id */
  private Long rentProductId;

}
