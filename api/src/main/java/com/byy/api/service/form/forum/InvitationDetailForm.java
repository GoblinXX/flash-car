package com.byy.api.service.form.forum;

import com.byy.api.service.form.page.IPageForm;
import com.byy.dal.entity.beans.forum.InvitationDetail;
import com.byy.dal.entity.beans.forum.InvitationPic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 03:38
 * @Description:
 */
@Getter
@Setter
@ToString
public class InvitationDetailForm extends InvitationDetail implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;
  /** 图片集合(含图片类型) */
  private List<InvitationPic> invitationPics;

}
