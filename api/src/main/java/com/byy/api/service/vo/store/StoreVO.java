package com.byy.api.service.vo.store;

import com.byy.api.service.vo.auditstore.AuditStoreVO;
import com.byy.dal.entity.beans.storepic.StorePic;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 门店vo
 * @author: Goblin
 * @create: 2019-06-12 10:28
 **/
@Setter
@Getter
@ToString
public class StoreVO {

  /**
   * 主键id
   */
  private Long id;
  /**
   * 审核门店vo
   */
  private AuditStoreVO auditStoreVO;
  /**
   * 门店详情图VO
   */
  private List<StorePic> images;
  /**
   * 门店账户
   */
  private String username;
  /**
   * 门店账户密码
   */
  private String password;

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
   * 门店座机
   */
  private String landline;

}
