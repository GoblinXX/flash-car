package com.byy.biz.service.forum;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.forum.InvitationReply;
import com.byy.dal.entity.dos.forum.InvitationReplyDO;

import java.util.List;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 03:04
 * @Description:
 */
public interface InvitationReplyService extends IService<InvitationReply> {
  /**
   * 查看帖子的一级回复
   * @param page
   * @param invitationId
   * @return
   */
  IPage<InvitationReplyDO> getReplyByInvitationDetailId(IPage page,Long invitationId);

  /**
   * 获取一级回复及其下的所有二级回复
   * @param page
   * @param invitationReplyId
   * @return
   */
  IPage<InvitationReplyDO> getAllReplyByInvitationReplyId(IPage page,Long invitationReplyId);

  /**
   * 根据一级id查询所有二级回复
   * @param invitationReplyId
   * @return
   */
  List<InvitationReplyDO> getSubordinateReply(Long invitationReplyId);

  /**
   * 查询自己的回复
   * @param page
   * @param userId
   * @return
   */
  IPage<InvitationReplyDO> getOwnReply(IPage page,Long userId);

  /**
   * 根据一级回复id查询二级回复前两条(查询帖子详情时用)
   * @param invitationReplyId
   * @return
   */
  List<InvitationReplyDO> getSubordinateReplyBySuperId(Long invitationReplyId);
}
