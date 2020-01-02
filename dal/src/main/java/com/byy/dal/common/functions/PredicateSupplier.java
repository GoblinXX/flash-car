package com.byy.dal.common.functions;

/**
 * @author: yyc
 * @date: 19-4-30 下午6:32
 */
@FunctionalInterface
public interface PredicateSupplier<T> {

  /**
   * 无参布尔值
   *
   * @return T
   */
  T test();
}
