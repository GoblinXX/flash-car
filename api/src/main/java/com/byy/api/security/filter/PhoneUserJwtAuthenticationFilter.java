package com.byy.api.security.filter;

import static com.byy.dal.common.utils.provider.WrapperProvider.oneQueryWrapper;

import com.byy.api.service.form.phone.PhoneForm;
import com.byy.api.service.form.sys.SysUserForm;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.JsonHelper;
import com.byy.dal.entity.beans.security.SecurityProperties;
import com.byy.dal.entity.beans.wechat.WeChatUser;
import com.byy.dal.enums.AuthType;
import com.byy.dal.enums.GlobalErrorCode;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author: Goblin
 * @date: 19-06-26 上午11:31
 */
public class PhoneUserJwtAuthenticationFilter extends JwtTokenBasedAuthenticationFilter {

  private final WeChatUserService weChatUserService;

  public PhoneUserJwtAuthenticationFilter(
      AuthenticationManager authenticationManager,
      SecurityProperties securityProperties,
      WeChatUserService weChatUserService) {
    super(authenticationManager, securityProperties);
    this.weChatUserService = weChatUserService;
  }

  @Override
  protected Authentication obtainAuthToken(HttpServletRequest request) {
    String params = obtainJsonParams(request);
    PhoneForm phoneForm = JsonHelper.str2Obj(params, PhoneForm.class);
    WeChatUser user = weChatUserService
        .getOne(oneQueryWrapper(WeChatUser::getPhone, phoneForm.getPhone()));
    CheckHelper.trueOrThrow(user != null, UserAuthException::new, GlobalErrorCode.USER_NOT_FOUND);
    return new UsernamePasswordAuthenticationToken(user.getId(), phoneForm.getPassword());
  }

  @Override
  protected AuthType authScope() {
    return AuthType.PHONE;
  }
}
