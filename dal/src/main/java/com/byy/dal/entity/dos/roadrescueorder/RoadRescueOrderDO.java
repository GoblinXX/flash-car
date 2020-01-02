package com.byy.dal.entity.dos.roadrescueorder;

import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @author: goblin
 * @date: 2019-06-18 15:09:34
 */
@Setter
@Getter
@ToString
@Alias("RoadRescueOrderDO")
public class RoadRescueOrderDO extends RoadRescueOrder {

  /**
   * 门店名称
   */
  private String name;
  /**
   * 门店主图
   */
  private String image;
  /**
   * 详细地址
   */
  private String newAddress;
  /**
   * 门店地址
   */
  private String storeAddress;
  /**
   * 商品价格
   */
  private BigDecimal price;
  /**
   * 商品图片
   */
  private String picture;
  /**
   * 商品名称
   */
  private String roadRescueName;
}
