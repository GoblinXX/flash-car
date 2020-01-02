package com.byy.dal.common.utils.provider;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.byy.dal.entity.beans.base.BaseEntityArchive;

import java.time.LocalDateTime;

/**
 * 查询条件生成类
 *
 * @author: yyc
 * @date: 19-2-26 上午10:45
 */
public class WrapperProvider {
  /**
   * 查询条件
   *
   * @param function SFunction
   * @param val Object
   * @param <T> T
   * @return LambdaQueryWrapper
   */
  public static <T> LambdaQueryWrapper<T> queryWrapper(SFunction<T, ?> function, Object val) {
    return Wrappers.<T>lambdaQuery().eq(function, val);
  }

  /**
   * 查询条件（单条记录）
   *
   * @param function SFunction
   * @param val Object
   * @param <T> T
   * @return LambdaQueryWrapper
   */
  public static <T> LambdaQueryWrapper<T> oneQueryWrapper(SFunction<T, ?> function, Object val) {
    return queryWrapper(function, val).last("limit 1");
  }

  /**
   * 更新条件
   *
   * @param function SFunction
   * @param val Object
   * @param <T> T
   * @return LambdaUpdateWrapper
   */
  public static <T extends BaseEntityArchive> LambdaUpdateWrapper<T> updateWrapper(
      SFunction<T, ?> function, Object val) {
    return Wrappers.<T>lambdaUpdate().eq(function, val).set(T::getUpdatedAt, LocalDateTime.now());
  }

  /**
   * 删除条件
   *
   * @param <T> T
   * @return LambdaQueryWrapper
   */
  public static <T extends BaseEntityArchive> LambdaUpdateWrapper<T> removeWrapper(
      SFunction<T, ?> function, Object val) {
    return updateWrapper(function, val).set(T::getArchive, true);
  }
}
