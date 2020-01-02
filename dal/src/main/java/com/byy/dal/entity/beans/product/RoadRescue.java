package com.byy.dal.entity.beans.product;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 12/06/19 下午 08:14
 * @Description:道路救援类
 */
@Setter
@Getter
@ToString
@Alias("RoadRescue")
public class RoadRescue extends BaseEntityArchive {
  /** 名称 */
  private String name;

  /** 价格 */
  private BigDecimal price;

  /** 图片 */
  private String picture;
}
