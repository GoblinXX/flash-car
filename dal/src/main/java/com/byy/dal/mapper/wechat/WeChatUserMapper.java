package com.byy.dal.mapper.wechat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byy.dal.entity.beans.wechat.WeChatUser;
import com.byy.dal.entity.dos.wechat.WeChatUserDO;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @author: yyc
 * @date: 19-4-26 上午11:55
 */
public interface WeChatUserMapper extends BaseMapper<WeChatUser> {

  /**
   * 后台分页条件查询微信用户信息
   */
  IPage<WeChatUserDO> listUserBySys(Page page, @Param("params") Map<String, Object> params);

  /**
   * 后台查看下级用户
   */
  IPage<WeChatUserDO> checkSubordinateUserInfo(Page page,
      @Param("params") Map<String, Object> params);

  /**
   * 小程序查看我的下级
   */
  IPage<WeChatUser> selectSubordinateInfo(Page page, @Param("userId") Long userId);
}
