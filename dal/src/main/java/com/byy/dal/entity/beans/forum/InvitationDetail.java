package com.byy.dal.entity.beans.forum;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import com.byy.dal.enums.ApprovalStatus;
import com.byy.dal.enums.InvitationType;
import com.byy.dal.enums.ReleaseType;
import com.byy.dal.enums.SupplyType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 01:44
 * @Description:
 */
@Getter
@Setter
@ToString
@Alias("InvitationDetail")
public class InvitationDetail extends BaseEntityArchiveWithUserId {

  /** 姓名 */
  private String name;

  /** 电话 */
  private String phone;

  /** 地址 */
  private String address;

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
}
