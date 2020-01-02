package com.byy.dal.mapper.forum;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.dal.entity.beans.forum.InvitationDetail;
import com.byy.dal.entity.dos.forum.InvitationDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 03:02
 * @Description:
 */
public interface InvitationDetailMapper extends BaseMapper<InvitationDetail> {

  /**
   * 查询已通过的帖子
   * @param page
   * @param params
   * @return
   */
  IPage<InvitationDetailDO> getPassInvitationDetailSys(@Param("page") IPage page, @Param("params") Map<String, Object> params);

  /**
   * 查询帖子详情byId
   * @param id
   * @return
   */
  InvitationDetailDO getInvitationDetailById(@Param("id") Long id);

  /**
   * 查询所有帖子
   * @param page
   * @param params
   * @return
   */
  IPage<InvitationDetailDO> getAllInvitationDetailSys(@Param("page") IPage page, @Param("params") Map<String, Object> params);

  /**
   * 小程序端分页查询帖子
   * @param page
   * @param params
   * @return
   */
  IPage<InvitationDetailDO> getPassInvitationDetailApp(@Param("page") IPage page, @Param("params") Map<String, Object> params);
}
