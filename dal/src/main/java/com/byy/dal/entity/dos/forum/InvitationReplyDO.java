package com.byy.dal.entity.dos.forum;

import com.byy.dal.entity.beans.forum.InvitationReply;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 06:05
 * @Description:
 */
@Getter
@Setter
@ToString
@Alias("InvitationReplyDO")
public class InvitationReplyDO extends InvitationReply {
  /** 头像 */
  private String avatar;

  /** 昵称 */
  private String nickname;

  /** 帖子标题 */
  private String title;
}
