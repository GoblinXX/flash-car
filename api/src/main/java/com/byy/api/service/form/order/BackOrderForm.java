package com.byy.api.service.form.order;

import com.byy.api.service.form.page.IPageForm;
import com.byy.dal.entity.beans.order.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author: xcf
 * @Date: 26/06/19 上午 10:30
 * @Description:
 */
@Getter
@Setter
@ToString
public class BackOrderForm extends Order implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;
}
