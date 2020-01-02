package com.byy.api.service.vo.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 删除返回VO
 *
 * @author: yyc
 * @date: 19-5-11 下午6:30
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DeletionVO {

  /** 删除后返回的主键 */
  private Long id;

  public static DeletionVO withId(Long id) {
    return new DeletionVO(id);
  }
}
