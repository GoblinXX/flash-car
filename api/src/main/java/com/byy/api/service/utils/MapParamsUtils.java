package com.byy.api.service.utils;

import com.byy.api.service.form.search.SearchForm;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;

/**
 * @Author: xcf
 * @Date: 26/06/19 上午 10:40
 * @Description:
 */
@Component
public class MapParamsUtils {

  public Map<String,Object> getMapParams(SearchForm searchForm){
    Map<String, Object> params = Maps.newHashMap();
    if (isNotEmpty(searchForm.getKeyWord())) {
      params.put("keyword", searchForm.getKeyWord());
    }
    if (isNotEmpty(searchForm.getStartTime())) {
      params.put("startTime", searchForm.of(searchForm.getStartTime()));
    }
    if (isNotEmpty(searchForm.getEndTime())) {
      params.put("endTime", searchForm.of(searchForm.getEndTime()).plusDays(1));
    }
    return params;
  }
}
