package com.byy.dal.entity.beans.comment;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import com.byy.dal.enums.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 27/06/19 上午 09:35
 * @Description:
 */
@Getter
@Setter
@ToString
@Alias("Comment")
public class Comment extends BaseEntityArchive {

  /** 订单id */
  private Long orderId;

  /** 评论内容 */
  private String content;

  /** 审核状态 */
  private ApprovalStatus approvalStatus;
}
