package com.byy.dal.entity.beans.location;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 区
 *
 * @author: yyc
 * @date: 19-4-10 上午9:40
 */
@Setter
@Getter
@ToString
public class Area {

  /** 主键 */
  private Long id;

  /** 区名 */
  private String name;

  /** 关联市id */
  private Long cityId;
}
