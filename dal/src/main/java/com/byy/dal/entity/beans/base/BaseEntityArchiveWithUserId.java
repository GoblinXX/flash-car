package com.byy.dal.entity.beans.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: yyc
 * @date: 19-3-30 下午5:19
 */
@Setter
@Getter
@ToString
public abstract class BaseEntityArchiveWithUserId extends BaseEntityArchive {

  /** 用户UserId */
  private Long userId;
}
