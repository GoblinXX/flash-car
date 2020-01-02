package com.byy.dal.entity.beans.forum;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import com.byy.dal.enums.ImgType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 02:18
 * @Description:
 */
@Getter
@Setter
@ToString
@Alias("InvitationPic")
public class InvitationPic extends BaseEntityArchive {

  /** 帖子id */
  private Long invitationId;

  /** 图片地址 */
  private String imgUrl;

  /** 图片类型 */
  private ImgType imgType;
}
