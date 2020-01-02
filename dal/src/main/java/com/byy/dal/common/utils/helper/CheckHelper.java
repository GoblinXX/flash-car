package com.byy.dal.common.utils.helper;

import com.byy.dal.common.functions.ExceptionSupplier;
import com.byy.dal.common.functions.PredicateSupplier;

/**
 * @author: yyc
 * @date: 19-3-30 下午8:08
 */
public class CheckHelper {

  /**
   * 如果为true，返回第一个值，否则返回第二个值
   *
   * @param condition boolean
   * @param current T
   * @param other T
   * @param <T> T
   * @return T
   */
  public static <T> T trueOrGet(final boolean condition, final T current, final T other) {
    return condition ? current : other;
  }

  /**
   * 如果condition为false，抛出异常
   *
   * @param condition boolean
   * @param exceptionSupplier ExceptionSupplier
   * @param m M
   * @param <M> M
   * @return T
   */
  public static <M> void trueOrThrow(
      final boolean condition, final ExceptionSupplier<M> exceptionSupplier, final M m) {
    if (!condition) throw exceptionSupplier.setMessage(m);
  }

  /**
   * 如果condition为false，抛出异常
   *
   * @param predicateSupplier PredicateSupplier
   * @param exceptionSupplier ExceptionSupplier
   * @param m M
   * @param <M> M
   * @return T
   */
  public static <M> void trueOrThrow(
      final PredicateSupplier<Boolean> predicateSupplier,
      final ExceptionSupplier<M> exceptionSupplier,
      final M m) {
    if (predicateSupplier != null && !predicateSupplier.test())
      throw exceptionSupplier.setMessage(m);
  }

  /**
   * 如果为空，抛出异常，否则返回本身
   *
   * @param t T
   * @param exceptionSupplier ExceptionSupplier
   * @param m M
   * @param <T> T
   * @param <M> M
   * @return T
   */
  public static <T, M> T nonEmptyOrThrow(
      final T t, final ExceptionSupplier<M> exceptionSupplier, final M m) {
    final boolean b;
    if (t instanceof String) {
      b = ((String) t).trim().length() > 0;
    } else {
      b = t != null;
    }
    trueOrThrow(b, exceptionSupplier, m);
    return t;
  }

  /**
   * 判断字符串是否为空
   */
  public static boolean isNotBlank(String s) {
    return s != null && s.length() > 0;
  }

  /**
   * 检查对象是否为为空
   */
  public static <T> boolean isNotNull(T t) {
    return t != null && t != "";
  }
}
