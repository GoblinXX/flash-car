package com.byy.api.security.filter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byy.api.service.form.sys.SysUserForm;
import com.byy.biz.service.sys.SysUserService;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.JsonHelper;
import com.byy.dal.entity.beans.security.SecurityProperties;
import com.byy.dal.entity.beans.sys.SysUser;
import com.byy.dal.enums.AuthType;
import com.byy.dal.enums.GlobalErrorCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

import static com.byy.dal.common.utils.provider.WrapperProvider.oneQueryWrapper;

/**
 * @author: yyc
 * @date: 19-3-14 下午5:33
 */
public class SysUserJwtAuthenticationFilter extends JwtTokenBasedAuthenticationFilter {

  private final SysUserService sysUserService;

  public SysUserJwtAuthenticationFilter(
      AuthenticationManager authenticationManager,
      SecurityProperties securityProperties,
      SysUserService sysUserService) {
    super(authenticationManager, securityProperties);
    this.sysUserService = sysUserService;
  }

  @Override
  protected Authentication obtainAuthToken(HttpServletRequest request) {
    String params = obtainJsonParams(request);
    SysUserForm form = JsonHelper.str2Obj(params, SysUserForm.class);
    LambdaQueryWrapper<SysUser> queryWrapper =
        oneQueryWrapper(SysUser::getUsername, form.getUsername());
    if (form.getStoreId() != null && form.getStoreId() == 0) {
      queryWrapper.eq(SysUser::getStoreId, 0);
    } else {
      queryWrapper.ne(SysUser::getStoreId, 0);
    }
    SysUser sysUser = sysUserService.getOne(queryWrapper);
    CheckHelper.trueOrThrow(
        sysUser != null, UserAuthException::new, GlobalErrorCode.USER_NOT_FOUND);
    return new UsernamePasswordAuthenticationToken(sysUser.getId(), form.getPassword());
  }

  @Override
  protected AuthType authScope() {
    return AuthType.SYS;
  }
}
