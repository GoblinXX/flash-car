package com.byy.api.service.vo.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 27/06/19 上午 11:47
 * @Description:
 */
@Getter
@Setter
@ToString
@Alias("CommentPicVO")
public class CommentPicVO {

  /** 主键id */
  private Long id;
  /** 评论id */
  private Long commentId;

  /** 图片地址 */
  private String imgUrl;
}
