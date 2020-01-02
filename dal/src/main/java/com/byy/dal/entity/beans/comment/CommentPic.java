package com.byy.dal.entity.beans.comment;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
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
@Alias("CommentPic")
public class CommentPic extends BaseEntityArchive {
  /** 评论id */
  private Long commentId;

  /** 图片地址 */
  private String imgUrl;
}
