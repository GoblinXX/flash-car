package com.byy.dal.common.errors;

import com.byy.dal.enums.GlobalErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * 登录认证异常
 *
 * @author: yyc
 * @date: 19-2-15 下午3:05
 */
@Getter
public class UserAuthException extends AuthenticationException {

  private static final long serialVersionUID = 1L;

  private final GlobalErrorCode errorCode;

  public UserAuthException(GlobalErrorCode ec) {
    this(ec, null);
  }

  public UserAuthException(String message) {
    this(message, null);
  }

  public UserAuthException(GlobalErrorCode ec, Throwable cause) {
    super(ec.getMessage(), cause);
    this.errorCode = ec;
  }

  public UserAuthException(String message, Throwable cause) {
    super(message, cause);
    this.errorCode = GlobalErrorCode.INTERNAL_ERROR;
  }
}
