package com.byy.api.service.form.comment;

import com.byy.api.service.form.page.IPageForm;
import com.byy.dal.entity.beans.comment.Comment;
import com.byy.dal.enums.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: master
 * @date: 2019-06-27 09:53:26
 */
@Setter
@Getter
@ToString
public class CommentForm extends Comment implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;
  /** 评论图片地址集合 */
  private List<String> imgUrls;
}

