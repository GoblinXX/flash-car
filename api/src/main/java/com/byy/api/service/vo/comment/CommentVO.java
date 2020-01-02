package com.byy.api.service.vo.comment;

import com.byy.dal.entity.beans.comment.CommentPic;
import com.byy.dal.enums.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 27/06/19 上午 09:35
 * @Description:
 */
@Getter
@Setter
@ToString
@Alias("CommentVO")
public class CommentVO {

  /** 主键id */
  private Long id;

  /** 订单id */
  private Long orderId;

  /** 评论内容 */
  private String content;

  /** 审核状态 */
  private ApprovalStatus approvalStatus;

  /** 评论时间 */
  private LocalDateTime createdAt;

  /** 用户头像 */
  private String avatar;

  /** 用户昵称 */
  private String nickname;

  /** 商品名 */
  private String productName;

  /** 实付金额 */
  private BigDecimal totalFee;

  /** 购买时间 */
  private LocalDateTime paidAt;

  /** 购买数量 */
  private Integer amount;

  /** 订单编号 */
  private String orderNo;

  /** 图片集合 */
  private List<CommentPic> commentPics;
}
