package com.byy.api.security.filter;

import com.byy.api.security.wechat.WeChatApiService;
import com.byy.api.security.wechat.WeChatDecipherData;
import com.byy.api.security.wechat.WeChatSessionData;
import com.byy.api.service.form.wechat.WeChatUserForm;
import com.byy.biz.service.point.UserPointService;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.JsonHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.point.UserPoint;
import com.byy.dal.entity.beans.security.SecurityProperties;
import com.byy.dal.entity.beans.wechat.WeChatUser;
import com.byy.dal.enums.AuthType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author: yyc
 * @date: 19-3-31 下午2:31
 */
public class WeChatJwtAuthenticationFilter extends JwtTokenBasedAuthenticationFilter {

  private final WeChatApiService weChatApiService;
  private final WeChatUserService weChatUserService;
  private final UserPointService userPointService;

  public WeChatJwtAuthenticationFilter(
      AuthenticationManager authenticationManager,
      SecurityProperties securityProperties,
      WeChatApiService weChatApiService,
      WeChatUserService weChatUserService,
      UserPointService userPointService) {
    super(authenticationManager, securityProperties);
    this.weChatApiService = weChatApiService;
    this.weChatUserService = weChatUserService;
    this.userPointService = userPointService;
  }

  @Override
  protected Authentication obtainAuthToken(HttpServletRequest request) {
    String jsonParams = obtainJsonParams(request);
    WeChatUserForm form = JsonHelper.str2Obj(jsonParams, WeChatUserForm.class);
    //  获取sessionKey
    WeChatSessionData session = weChatApiService.genWeChatSessionByCode(form.getCode());
    String sessionKey = session.getSessionKey();
    // 解密微信用户信息
    WeChatDecipherData decipher =
        weChatApiService.decrypt(
            form.getEncryptedData(), sessionKey, form.getIv(), WeChatDecipherData.class);
    WeChatUser weChatUser = Transformer.fromBean(decipher, WeChatUser.class);
    // 保存或更新用户
    weChatUser.setSessionKey(sessionKey);
    weChatUser = weChatUserService.saveOrUpdateWeChatUser(weChatUser);
    if (userPointService.getOne(WrapperProvider.queryWrapper(UserPoint::getUserId,weChatUser.getId())) == null){
      UserPoint userPoint = new UserPoint();
      userPoint.setUserId(weChatUser.getId());
      CheckHelper.trueOrThrow(userPointService.save(userPoint), BizException::new,"用户积分添加失败!");
    }
    return new UsernamePasswordAuthenticationToken(weChatUser.getId(), DEFAULT_PWD);
  }

  @Override
  protected AuthType authScope() {
    return AuthType.WECHAT;
  }
}
