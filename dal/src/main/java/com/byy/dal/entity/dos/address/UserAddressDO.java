package com.byy.dal.entity.dos.address;

import com.byy.dal.entity.beans.address.UserAddress;
import com.byy.dal.entity.beans.location.AddressChain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: yyc
 * @date: 2019-06-17 15:51:22
 */
@Setter
@Getter
@ToString
public class UserAddressDO extends UserAddress {

  /** 省市区 */
  private AddressChain addressChain;
}
