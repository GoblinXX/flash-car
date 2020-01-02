package com.byy.api.service.vo.roadrescueorder;

import com.byy.dal.entity.beans.roadrescuepic.RoadRescuePic;
import com.byy.dal.enums.OrderType;
import com.byy.dal.enums.RoadType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 道路救援订单
 * @author: Goblin
 * @create: 2019-06-18 14:55
 **/
@Alias("RoadRescueOrderVO")
@Getter
@Setter
@ToString
public class RoadRescueOrderVO {

  /**
   * 主键id
   */
  private Long id;

  /**
   * 用户userId
   */
  private Long userId;
  /**
   * 支付时间
   */
  private LocalDateTime paidAt;
  /**
   * 商品总价
   */
  private BigDecimal goodsFee;
  /**
   * 实付金额
   */
  private BigDecimal totalFee;
  /**
   * 买家姓名
   */
  private String buyerName;
  /**
   * 买家电话
   */
  private String buyerPhone;
  /**
   * 订单编号
   */
  private String orderNo;
  /**
   * 门店id
   */
  private Long storeId;
  /**
   * 订单状态
   */
  private RoadType status;
  /**
   * 选择地址
   */
  private String address;
  /**
   * 详细地址
   */
  private String detailedAddress;
  /**
   * 服务类型
   */
  private OrderType orderType;
  /**
   * 下单时间
   */
  private LocalDateTime createdAt;
  /**
   * 门店名称
   */
  private String name;
  /**
   * 门店主图
   */
  private String image;
  /**
   * 道路救援商品id
   */
  private Long roadRescueId;
  /**
   * 故障图片
   */
  private List<RoadRescuePic> images;
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
  /**
   * 备注
   */
  private String remarks;
  /**
   * 是否展示
   */
  private Boolean showBack;
}
