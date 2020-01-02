package com.byy.dal.common.functions;

/**
 * 异常处理函数式接口
 *
 * @author: yyc
 * @date: 19-3-30 下午11:24
 */
@FunctionalInterface
public interface ExceptionSupplier<M> {

  /**
   * @param m M
   * @return RuntimeException
   */
  RuntimeException setMessage(M m);
}
