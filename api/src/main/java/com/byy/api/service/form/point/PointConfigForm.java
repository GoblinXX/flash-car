package com.byy.api.service.form.point;

import com.byy.dal.entity.beans.point.PointConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 04:02
 * @Description:
 */
@Setter
@Getter
@ToString
public class PointConfigForm {

  /** 积分配置类型的List */
  private List<PointConfig> pointConfigList;
}
