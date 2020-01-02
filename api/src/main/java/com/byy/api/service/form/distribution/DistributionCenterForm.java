package com.byy.api.service.form.distribution;

import com.byy.dal.entity.beans.distribution.DistributionCenter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 分销中心form
 * @author: Goblin
 * @create: 2019-06-13 15:25
 **/
@Getter
@Setter
@ToString
public class DistributionCenterForm extends DistributionCenter {

  /**
   * 邀请好友跳转页面
   */
  private String nextPage;
  /**
   * 邀请好友参数
   */
  private String scene;
}
