package com.byy.dal.entity.beans.point;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import com.byy.dal.enums.SceneType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 02:06
 * @Description:积分配置类
 */
@Setter
@Getter
@ToString
@Alias("PointConfig")
public class PointConfig extends BaseEntityArchive {

  /** 可获得积分场景 */
  private SceneType scene;

  /** 可获得积分*/
  private BigDecimal amount;
}
