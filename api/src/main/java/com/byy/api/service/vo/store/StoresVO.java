package com.byy.api.service.vo.store;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 门店列表VO
 * @author: Goblin
 * @create: 2019-06-12 14:04
 **/
@Setter
@Getter
@ToString
public class StoresVO {

  /**
   * 主键id
   */
  private Long id;
  /**
   * 门店名称
   */
  private String name;
  /**
   * 网点联系电话
   */
  private String phone;
  /**
   * 门店负责人
   */
  private String username;
  /**
   * 门店负责人电话
   */
  private String userPhone;
  /**
   * 门店地址
   */
  private String address;
  /**
   * 门店状态
   */
  private Boolean onSale;
  /**
   * 门店经度
   */
  private String longitude;
  /**
   * 门店纬度
   */
  private String latitude;
}
