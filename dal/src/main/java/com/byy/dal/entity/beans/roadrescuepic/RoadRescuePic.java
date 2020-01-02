package com.byy.dal.entity.beans.roadrescuepic;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 道路救援故障图
 * @author: Goblin
 * @create: 2019-06-18 15:22
 **/
@Getter
@Setter
@ToString
@Alias("RoadRescuePic")
public class RoadRescuePic extends BaseEntityArchive {

  /**
   * 道路救援订单id
   */
  private Long roadRescueId;
  /**
   * 故障图片
   */
  private String image;
}
