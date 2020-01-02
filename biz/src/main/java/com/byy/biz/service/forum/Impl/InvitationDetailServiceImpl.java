package com.byy.biz.service.forum.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.forum.InvitationDetailService;
import com.byy.dal.entity.beans.forum.InvitationDetail;
import com.byy.dal.entity.dos.forum.InvitationDetailDO;
import com.byy.dal.mapper.forum.InvitationDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 03:22
 * @Description:
 */
@Service
public class InvitationDetailServiceImpl extends ServiceImpl<InvitationDetailMapper, InvitationDetail> implements InvitationDetailService {

  @Autowired
  private InvitationDetailMapper invitationDetailMapper;

  @Override
  public IPage<InvitationDetailDO> getPassInvitationDetailSys(IPage page, Map<String, Object> params) {
    return invitationDetailMapper.getPassInvitationDetailSys(page,params);
  }

  @Override
  public InvitationDetailDO getInvitationDetailById(Long id) {
    return invitationDetailMapper.getInvitationDetailById(id);
  }

  @Override
  public IPage<InvitationDetailDO> getAllInvitationDetailSys(IPage page, Map<String, Object> params) {
    return invitationDetailMapper.getAllInvitationDetailSys(page,params);
  }

  @Override
  public IPage<InvitationDetailDO> getPassInvitationDetailApp(IPage page, Map<String, Object> params) {
    return invitationDetailMapper.getPassInvitationDetailApp(page,params);
  }
}
