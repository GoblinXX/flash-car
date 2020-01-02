package com.byy.dal.entity.beans.wechat;

import com.byy.dal.entity.beans.base.BaseUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * 用户
 *
 * @author: yyc
 * @date: 19-1-29 上午11:31
 */
@Setter
@Getter
@ToString
@Alias("WeChatUser")
public class WeChatUser extends BaseUser {

  /** 三方openId */
  private String openId;

  /** 三方unionId */
  private String unionId;

  /** 三方sessionKey */
  private String sessionKey;

  /** 手机号 */
  private String phone;

  /** 性别(0是未知，1代表男，2代表女) */
  private Integer gender;

  /** 昵称 */
  private String nickname;

  /** 头像 */
  private String avatar;
}
