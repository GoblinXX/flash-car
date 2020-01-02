package com.byy.dal.entity.beans.auditstore;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import com.byy.dal.enums.StoreCheckType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 门店申请列表
 * @author: Goblin
 * @create: 2019-06-11 16:03
 **/
@Alias("AuditStore")
@Setter
@Getter
@ToString
public class AuditStore extends BaseEntityArchiveWithUserId {

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
   * 申请状态
   */
  private StoreCheckType status;


}
