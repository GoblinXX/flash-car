package com.byy.api.service.advice;

import com.byy.api.response.ResponseObject;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.enums.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * created by
 *
 * @author wangxinhua at 18-8-6 下午11:54
 */
@Slf4j
@ControllerAdvice
public class ApiControllerExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * 处理业务异常, 其他异常在父类中已经有了默认实现
   *
   * @param e 异常对象
   * @return 异常处理结果
   */
  @ExceptionHandler(BizException.class)
  private ResponseEntity<?> handleBizException(BizException e) {
    log.error("异常拦截器截取到业务异常", e);
    return new ResponseEntity<>(
        ResponseObject.error(e.getErrorCode().getCode(), e.getMessage()), HttpStatus.OK);
  }

  /**
   * 处理登录验证异常
   *
   * @param e 异常对象
   * @return 异常封装对象
   */
  @ExceptionHandler(UserAuthException.class)
  private ResponseEntity<?> handleAuthException(UserAuthException e) {
    log.error("异常拦截器截取到登录异常", e);
    return new ResponseEntity<>(
        ResponseObject.error(e.getErrorCode().getCode(), e.getMessage()), HttpStatus.OK);
  }

  /**
   * 处理全局异常, 没有指定异常处理方法处理的方法
   *
   * @param e 当次需要处理的异常
   * @return 转化后的response
   */
  @ExceptionHandler(Throwable.class)
  private ResponseEntity<?> globalErrorHandler(Throwable e) {
    log.error("未知错误", e);
    return new ResponseEntity<>(
        ResponseObject.error(GlobalErrorCode.INTERNAL_ERROR.getCode(), "系统繁忙，请稍后再试"),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
