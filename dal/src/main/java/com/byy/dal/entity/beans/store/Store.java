package com.byy.dal.entity.beans.store;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * @program: flash-car
 * @description: 门店
 * @author: Goblin
 * @create: 2019-06-11 15:18
 **/
@Alias("Store")
@Setter
@Getter
@ToString
public class Store extends BaseEntityArchive {

  /**
   * 门店名称
   */
  private String name;
  /**
   * 门店地址
   */
  private String address;
  /**
   * 门店主图
   */
  private String image;
  /**
   * 门店经度
   */
  private String longitude;
  /**
   * 门店纬度
   */
  private String latitude;
  /**
   * 门店电话
   */
  private String phone;
  /**
   * 门店座机
   */
  private String landline;
  /**
   * 门店详情
   */
  private String content;
  /**
   * 门店状态 默认下架
   */
  private Boolean onSale;
  /**
   * 审核门店id
   */
  private Long auditStoreId;

  /**
   * 门店累计金额
   */
  private BigDecimal cumulativeAmount;

  /**
   * 门店可用金额
   */
  private BigDecimal availableAmount;

}
