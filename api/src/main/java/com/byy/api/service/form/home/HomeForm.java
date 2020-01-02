package com.byy.api.service.form.home;

import com.byy.api.service.form.page.IPageForm;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author: xcf
 * @Date: 25/09/19 下午 04:28
 * @Description:
 */
@Getter
@Setter
@ToString
public class HomeForm implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;
}
