package com.byy.dal.entity.dos.forum;

import com.byy.dal.entity.beans.forum.InvitationDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 03:45
 * @Description:
 */
@Getter
@Setter
@ToString
@Alias("InvitationDetailDO")
public class InvitationDetailDO extends InvitationDetail {
  /** 头像 */
  private String avatar;

  /** 昵称 */
  private String nickname;
}
