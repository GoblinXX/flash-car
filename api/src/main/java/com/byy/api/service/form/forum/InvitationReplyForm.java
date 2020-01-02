package com.byy.api.service.form.forum;

import com.byy.api.service.form.page.IPageForm;
import com.byy.dal.entity.beans.forum.InvitationReply;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author: xcf
 * @Date: 21/06/19 上午 10:46
 * @Description:
 */
@Getter
@Setter
@ToString
public class InvitationReplyForm extends InvitationReply implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;
  /** 回复id */
  private Long invitationReplyId;
}
