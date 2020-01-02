package com.byy.api.service.controller.auth;

import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.BaseController;
import com.byy.api.service.vo.sys.SysUserVO;
import com.byy.api.service.vo.wechat.WeChatUserVO;
import com.byy.biz.service.sys.SysAuthorityService;
import com.byy.biz.service.sys.SysUserService;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.sys.SysAuthority;
import com.byy.dal.entity.beans.sys.SysUser;
import com.byy.dal.entity.beans.wechat.WeChatUser;
import com.byy.dal.enums.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户认证
 *
 * @author yyc
 * @date 19-3-30 上午12:53
 */
@RestController
@RequestMapping("/user/auth")
@Slf4j
public class UserAuthController extends BaseController {

  private final WeChatUserService weChatUserService;
  private final SysUserService sysUserService;
  private final SysAuthorityService sysAuthorityService;

  @Autowired
  public UserAuthController(
      WeChatUserService weChatUserService,
      SysUserService sysUserService,
      SysAuthorityService sysAuthorityService) {
    this.weChatUserService = weChatUserService;
    this.sysUserService = sysUserService;
    this.sysAuthorityService = sysAuthorityService;
  }

  /**
   * 通过框架登录成功的用户会被forward到该接口 获取登录用户的上下文后返回用户信息
   *
   * @return ResponseObject
   */
  @RequestMapping("/success")
  public ResponseObject<?> success() {
    if (isUserType(WeChatUser.class)) {
      WeChatUser weChatUser = weChatUserService.getById(getCurrentUserId());
      return ResponseObject.success(Transformer.fromBean(weChatUser, WeChatUserVO.class));
    } else if (isUserType(SysUser.class)) {
      return ResponseObject.success(loadSysUser(getCurrentUserId()));
    }
    throw new UserAuthException(GlobalErrorCode.USER_AUTH_ERROR);
  }

  /** 登录失败, 直接将异常信息抛给异常处理器处理 */
  @RequestMapping("/fail")
  public void fail() {
    throw new UserAuthException(GlobalErrorCode.USER_AUTH_ERROR);
  }

  /**
   * 查询后台用户
   *
   * @param userId Long
   * @return SysUserVO
   */
  private SysUserVO loadSysUser(Long userId) {
    SysUserVO sysUserVO = Transformer.fromBean(sysUserService.getById(userId), SysUserVO.class);
    List<String> sysAuthorities =
        sysAuthorityService.list(WrapperProvider.queryWrapper(SysAuthority::getUserId, userId))
            .stream()
            .map(SysAuthority::getMenuName)
            .collect(Collectors.toList());
    sysUserVO.setSysAuthorities(sysAuthorities);
    return sysUserVO;
  }
}
