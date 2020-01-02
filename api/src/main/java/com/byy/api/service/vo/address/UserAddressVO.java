package com.byy.api.service.vo.address;

import com.byy.dal.entity.beans.location.AddressChain;
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
public class UserAddressVO {

  /** 主键id */
  private Long id;

  /** 用户userId */
  private Long userId;

  /** 联系人 */
  private String contact;

  /** 手机号 */
  private String phone;

  /** 区id */
  private Long areaId;

  /** 详细地址 */
  private String street;

  /** 省市区 */
  private AddressChain addressChain;

  /** 是否设为默认地址(默认不是默认地址) */
  private Boolean onDefault;
}
