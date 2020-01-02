package com.byy.api.service.form.page;

/**
 * 自定义分页表单
 *
 * @author: yyc
 * @date: 19-3-11 下午4:11
 */
public interface IPageForm {

  /** 当前页(从1开始) */
  Long getPage();

  /** 分页大小 */
  Long getSize();

  default <T> PageForm<T> newPage() {
    return new PageForm<>(getPage() == null ? 1L : getPage(), getSize() == null ? 8L : getSize());
  }
}
