package com.byy.dal.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通过查询Mapper
 *
 * @author: yyc
 * @date: 19-6-26 下午10:10
 */
public interface CommonMapper<T> extends BaseMapper<T> {

  /**
   * 通过map参数查询列表
   *
   * @param params Map
   * @param <M> M
   * @return List
   */
  <M> List<M> selectListByParams(@Param("params") Map<String, Object> params);

  /**
   * 通过map参数常规分页
   *
   * @param params params
   * @param <M> M
   * @return IPage
   */
  <M> IPage<M> selectPageByParams(IPage<M> page, @Param("params") Map<String, Object> params);
}
