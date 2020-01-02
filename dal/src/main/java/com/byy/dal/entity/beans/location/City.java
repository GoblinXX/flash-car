package com.byy.dal.entity.beans.location;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 市
 *
 * @author: yyc
 * @date: 19-4-10 上午9:40
 */
@Setter
@Getter
@ToString
public class City {

  /** 主键 */
  private Long id;

  /** 市名 */
  private String name;

  /** 关联省id */
  private Long provinceId;
}
