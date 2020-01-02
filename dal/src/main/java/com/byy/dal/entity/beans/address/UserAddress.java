package com.byy.dal.entity.beans.address;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户地址
 *
 * @author: yyc
 * @date: 19-6-17 下午3:43
 */
@Setter
@Getter
@ToString
public class UserAddress extends BaseEntityArchiveWithUserId {

  /** 联系人 */
  private String contact;

  /** 手机号 */
  private String phone;

  /** 区id */
  private Long areaId;

  /** 详细地址 */
  private String street;

  /** 是否设为默认地址(默认不是默认地址) */
  private Boolean onDefault;
}
