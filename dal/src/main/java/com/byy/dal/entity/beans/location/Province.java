package com.byy.dal.entity.beans.location;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 省
 *
 * @author: yyc
 * @date: 19-4-10 上午9:40
 */
@Setter
@Getter
@ToString
public class Province {

  /** 主键 */
  private Long id;

  /** 省名 */
  private String name;
}
