package com.byy.dal.entity.beans.location;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * 省市区
 *
 * @author: yyc
 * @date: 19-5-10 下午5:44
 */
@Setter
@Getter
@ToString
@Alias("AddressChain")
public class AddressChain {

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
}
