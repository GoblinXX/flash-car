package com.byy.dal.mapper.forum;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.dal.entity.beans.forum.InvitationReply;
import com.byy.dal.entity.dos.forum.InvitationReplyDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 03:03
 * @Description:
 */
public interface InvitationReplyMapper extends BaseMapper<InvitationReply> {
  /**
   * 查看帖子的一级回复
   * @param page
   * @param invitationId
   * @return
   */
  IPage<InvitationReplyDO> getReplyByInvitationDetailId(@Param("page") IPage page,@Param("invitationId") Long invitationId);

  /**
   * 获取一级回复及其下的所有二级回复
   * @param invitationReplyId
   * @return
   */
  IPage<InvitationReplyDO> getAllReplyByInvitationReplyId(@Param("page") IPage page,@Param("invitationReplyId") Long invitationReplyId);

  /**
   * 根据一级回复查询所有二级回复
   * @param invitationReplyId
   * @return
   */
  List<InvitationReplyDO> getSubordinateReply(@Param("invitationReplyId") Long invitationReplyId);

  /**
   * 查询自己的回复
   * @param page
   * @param userId
   * @return
   */
  IPage<InvitationReplyDO> getOwnReply(@Param("page") IPage page,@Param("userId") Long userId);

  /**
   * 根据一级回复id查询二级回复前两条(查询帖子详情时用)
   * @param invitationReplyId
   * @return
   */
  List<InvitationReplyDO> getSubordinateReplyBySuperId(@Param("invitationReplyId") Long invitationReplyId);
}
