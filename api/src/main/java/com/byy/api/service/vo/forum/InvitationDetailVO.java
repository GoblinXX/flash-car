package com.byy.api.service.vo.forum;

import com.byy.dal.entity.beans.forum.InvitationPic;
import com.byy.dal.enums.ApprovalStatus;
import com.byy.dal.enums.InvitationType;
import com.byy.dal.enums.ReleaseType;
import com.byy.dal.enums.SupplyType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 04:34
 * @Description:
 */
@Getter
@Setter
@ToString
public class InvitationDetailVO {

  /** id */
  private Long id;

  /** 姓名 */
  private String name;

  /** 电话 */
  private String phone;

  /** 地址 */
  private String address;

  /** 发帖人id */
  private Long userId;

  /** 帖子类型枚举 */
  private InvitationType invitationType;

  /** 供求类型枚举 */
  private SupplyType supplyType;

  /** 发布类别枚举 */
  private ReleaseType releaseType;

  /** 标题 */
  private String title;

  /** 内容 */
  private String content;

  /** 隐藏状态 */
  private Boolean hiddenStatus;

  /** 审核状态 */
  private ApprovalStatus approvalStatus;

  /** 发帖时间 */
  private LocalDateTime createdAt;

  /** 头像 */
  private String avatar;

  /** 昵称 */
  private String nickname;

  /** 帖子图片集合 */
  private List<InvitationPic> invitationPics;
}
