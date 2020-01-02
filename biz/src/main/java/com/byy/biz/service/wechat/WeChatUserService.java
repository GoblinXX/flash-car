package com.byy.biz.service.wechat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.wechat.WeChatUser;
import com.byy.dal.entity.dos.wechat.WeChatUserDO;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @author: yyc
 * @date: 19-3-29 下午1:03
 */
public interface WeChatUserService extends IService<WeChatUser> {

  /**
   * 保存或更新用户
   *
   * @param weChatUser
   * @return WeChatUser
   */
  WeChatUser saveOrUpdateWeChatUser(WeChatUser weChatUser);

  /**
   * 后台分页条件查询微信用户信息
   */
  IPage<WeChatUserDO> listUserBySys(Page page, Map<String, Object> params);

  /**
   * 后台查看下级用户
   */
  IPage<WeChatUserDO> checkSubordinateUserInfo(Page page, Map<String, Object> params);

  /**
   * 小程序查看我的下级
   */
  IPage<WeChatUser> selectSubordinateInfo(Page page, Long userId);
}
