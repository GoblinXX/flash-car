package com.byy.api.service.form.point;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.service.form.page.IPageForm;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 04:52
 * @Description:
 */
@Getter
@Setter
@ToString
public class UserPointRecordForm implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;
  /** userId */
  private Long userId;

}
