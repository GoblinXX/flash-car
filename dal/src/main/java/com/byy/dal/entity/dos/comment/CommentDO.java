package com.byy.dal.entity.dos.comment;

import com.byy.dal.entity.beans.comment.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: master
 * @date: 2019-06-27 09:53:26
 */
@Setter
@Getter
@ToString
@Alias("CommentDO")
public class CommentDO extends Comment {
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
}
