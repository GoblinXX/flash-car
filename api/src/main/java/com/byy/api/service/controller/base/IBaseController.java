package com.byy.api.service.controller.base;

import com.byy.api.response.ResponseObject;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.entity.beans.base.BaseUser;
import com.byy.dal.enums.GlobalErrorCode;
import com.google.common.collect.ImmutableMap;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * @author: yyc
 * @date: 19-3-31 下午5:15
 */
public interface IBaseController {

  /**
   * 获取当前用户Id
   *
   * @return 当前用户对象
   * @throws AccessDeniedException 当前用户为登录
   */
  default Long getCurrentUserId() {
    return CheckHelper.nonEmptyOrThrow(
            (BaseUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
            UserAuthException::new,
            GlobalErrorCode.USER_AUTH_ERROR)
        .getId();
  }

  /**
   * 判断当前用户是否是目标类型
   *
   * @param clazz 目标类型
   * @param <T> T
   * @return boolean
   */
  default <T> boolean isUserType(Class<T> clazz) {
    return clazz
        == SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass();
  }

  /**
   * 根据condition布尔值返回统一数据
   *
   * @param condition boolean
   * @param t T
   * @param errorMsg String
   * @param <T> T
   * @return ResponseObject
   */
  default <T> ResponseObject<T> trueOrError(boolean condition, T t, String errorMsg) {
    CheckHelper.trueOrThrow(condition, BizException::new, errorMsg);
    return ResponseObject.success(t);
  }

  /**
   * 返回分页数据
   *
   * @param list List
   * @param total Long
   * @param <T> T
   * @return ResponseObject
   */
  default <T> ResponseObject<ImmutableMap<String, Object>> toPageMap(List<T> list, Long total) {
    return ResponseObject.success(ImmutableMap.of("list", list, "total", total));
  }
}
