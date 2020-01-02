package com.byy.api.service.vo.auditstore;

import com.byy.dal.enums.StoreCheckType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 门店审核VO
 * @author: Goblin
 * @create: 2019-06-11 16:50
 **/
@Setter
@Getter
@ToString
public class AuditStoreVO {

  /**
   * 主键id
   */
  private Long id;
  /**
   * 负责人姓名
   */
  private String name;
  /**
   * 负责人电话
   */
  private String phone;
  /**
   * 营业执照正面
   */
  private String businessLicenseUp;
  /**
   * 营业执照背面
   */
  private String businessLicenseDown;
  /**
   * 负责人身份证正面
   */
  private String idCardUp;
  /**
   * 负责人身份证背面
   */
  private String idCardDown;
  /**
   * 区域id
   */
  private Long areaId;
  /**
   * 详细地址
   */
  private String address;
  /**
   * user_id
   */
  private String userId;
  /**
   * 申请状态
   */
  private StoreCheckType status;
}
