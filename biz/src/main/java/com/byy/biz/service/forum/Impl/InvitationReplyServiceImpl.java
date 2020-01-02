package com.byy.biz.service.forum.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.forum.InvitationReplyService;
import com.byy.dal.entity.beans.forum.InvitationReply;
import com.byy.dal.entity.dos.forum.InvitationReplyDO;
import com.byy.dal.mapper.forum.InvitationReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 03:24
 * @Description:
 */
@Service
public class InvitationReplyServiceImpl extends ServiceImpl<InvitationReplyMapper, InvitationReply> implements InvitationReplyService {

  @Autowired
  private InvitationReplyMapper invitationReplyMapper;

  @Override
  public IPage<InvitationReplyDO> getReplyByInvitationDetailId(IPage page, Long invitationId) {
    return invitationReplyMapper.getReplyByInvitationDetailId(page,invitationId);
  }

  @Override
  public IPage<InvitationReplyDO> getAllReplyByInvitationReplyId(IPage page, Long invitationReplyId) {
    return invitationReplyMapper.getAllReplyByInvitationReplyId(page,invitationReplyId);
  }

  @Override
  public List<InvitationReplyDO> getSubordinateReply(Long invitationReplyId) {
    return invitationReplyMapper.getSubordinateReply(invitationReplyId);
  }

  @Override
  public IPage<InvitationReplyDO> getOwnReply(IPage page, Long userId) {
    return invitationReplyMapper.getOwnReply(page,userId);
  }

  @Override
  public List<InvitationReplyDO> getSubordinateReplyBySuperId(Long invitationReplyId) {
    return invitationReplyMapper.getSubordinateReplyBySuperId(invitationReplyId);
  }
}
