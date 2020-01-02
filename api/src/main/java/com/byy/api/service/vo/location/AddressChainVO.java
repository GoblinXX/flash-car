package com.byy.api.service.vo.location;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 省市区
 *
 * @author: yyc
 * @date: 19-5-10 下午5:44
 */
@Setter
@Getter
@ToString
public class AddressChainVO {

  /** 区id */
  private Long areaId;

  /** 市id */
  private Long cityId;

  /** 省id */
  private Long provinceId;

  /** 区 */
  private String area;

  /** 市 */
  private String city;

  /** 省 */
  private String province;

  /** 拼接地址 */
  public String getAddress() {
    return province + city + area;
  }
}
