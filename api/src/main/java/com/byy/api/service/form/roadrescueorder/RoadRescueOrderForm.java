package com.byy.api.service.form.roadrescueorder;

import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: goblin
 * @date: 2019-06-18 15:09:34
 */
@Setter
@Getter
@ToString
public class RoadRescueOrderForm extends RoadRescueOrder {

  /**
   * 故障图片
   */
  private List<String> images;
}

