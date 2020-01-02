package com.byy.api.service.form.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: yyc
 * @date: 19-5-7 下午5:07
 */
@Setter
@Getter
@ToString
public class PageForm<T> extends Page<T> {

  public PageForm(Long page, Long size) {
    super(page, size);
  }

  /**
   * 获取偏移量
   *
   * @return Long
   */
  public Long getOffset() {
    return offset();
  }
}
