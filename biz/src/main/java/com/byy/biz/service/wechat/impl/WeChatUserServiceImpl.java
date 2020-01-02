package com.byy.biz.service.wechat.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.wechat.WeChatUser;
import com.byy.dal.entity.dos.wechat.WeChatUserDO;
import com.byy.dal.enums.GlobalErrorCode;
import com.byy.dal.mapper.wechat.WeChatUserMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;;

/**
 * @author: yyc
 * @date: 19-3-20 下午1:29
 */
@Service
public class WeChatUserServiceImpl extends ServiceImpl<WeChatUserMapper, WeChatUser>
    implements WeChatUserService {

  private static final String DEFAULT_PWD = "e10adc3949ba59abbe56e057f20f883e";
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public WeChatUserServiceImpl(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public WeChatUser saveOrUpdateWeChatUser(WeChatUser weChatUser) {
    weChatUser.setUsername(weChatUser.getNickname());
    weChatUser.setPassword(passwordEncoder.encode(DEFAULT_PWD));
    String openId = weChatUser.getOpenId();
    WeChatUser one = getOne(WrapperProvider.oneQueryWrapper(WeChatUser::getOpenId, openId));
    boolean ret;
    if (one == null) {
      ret = save(weChatUser);
    } else {
      weChatUser.setId(one.getId());
      ret = updateById(weChatUser);
    }
    CheckHelper.trueOrThrow(ret, UserAuthException::new, GlobalErrorCode.USER_CREATE_ERROR);
    return weChatUser;
  }

  @Override
  public IPage<WeChatUserDO> listUserBySys(Page page, Map<String, Object> params) {
    return baseMapper.listUserBySys(page, params);
  }

  @Override
  public IPage<WeChatUserDO> checkSubordinateUserInfo(Page page, Map<String, Object> params) {
    return baseMapper.checkSubordinateUserInfo(page, params);
  }

  @Override
  public IPage<WeChatUser> selectSubordinateInfo(Page page, Long userId) {
    return baseMapper.selectSubordinateInfo(page, userId);
  }
}
