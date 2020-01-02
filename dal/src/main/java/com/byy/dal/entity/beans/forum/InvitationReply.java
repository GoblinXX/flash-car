package com.byy.dal.entity.beans.forum;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 02:25
 * @Description:帖子回复id
 */
@Getter
@Setter
@ToString
@Alias("InvitationReply")
public class InvitationReply extends BaseEntityArchiveWithUserId {

  /** 帖子id */
  private Long invitationId;

  /** 回复内容 */
  private String content;

  /** 上级回复id */
  private Long superReplyId;

  /** 拥有人id */
  private Long ownerId;

  /** 是否已观看 */
  private Boolean watched;
}
