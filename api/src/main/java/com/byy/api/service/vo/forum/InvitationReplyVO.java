package com.byy.api.service.vo.forum;

import com.byy.dal.entity.beans.forum.InvitationReply;
import com.byy.dal.entity.dos.forum.InvitationReplyDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 06:15
 * @Description:
 */
@Getter
@Setter
@ToString
public class InvitationReplyVO {

  /** id */
  private Long id;

  /** 帖子id */
  private Long invitationId;

  /** 回复内容 */
  private String content;

  /** 上级回复id */
  private Long superReplyId;

  /** 回复人id */
  private Long userId;

  /** 头像 */
  private String avatar;

  /** 昵称 */
  private String nickname;

  /** 帖子标题 */
  private String title;

  /** 回复时间 */
  private LocalDateTime createdAt;

  /** 二级回复集合 */
  private List<InvitationReplyDO> lowerInvitationReplies;

  /** 二级回复总条数 */
  private Integer lowerCount;
}
