package com.byy.biz.service.forum;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.forum.InvitationDetail;
import com.byy.dal.entity.dos.forum.InvitationDetailDO;

import java.util.Map;


/**
 * @Author: xcf
 * @Date: 20/06/19 下午 03:21
 * @Description:
 */
public interface InvitationDetailService extends IService<InvitationDetail> {

  /**
   * 查询已通过的帖子
   * @param page
   * @param params
   * @return
   */
  IPage<InvitationDetailDO> getPassInvitationDetailSys(IPage page, Map<String,Object> params);

  /**
   * 查询帖子详情
   * @param id
   * @return
   */
  InvitationDetailDO getInvitationDetailById(Long id);

  /**
   * 查询所有帖子
   * @param page
   * @param params
   * @return
   */
  IPage<InvitationDetailDO> getAllInvitationDetailSys(IPage page, Map<String,Object> params);

  /**
   * 小程序端分页查询
   * @param page
   * @param params
   * @return
   */
  IPage<InvitationDetailDO> getPassInvitationDetailApp(IPage page, Map<String,Object> params);
}
