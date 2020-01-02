package com.byy.dal.entity.dos.distribution;

import com.byy.dal.entity.beans.distribution.DistributionCenter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description:
 * @author: Goblin
 * @create: 2019-06-13 16:37
 **/
@Setter
@Getter
@ToString
@Alias("DistributionCenterDO")
public class DistributionCenterDO extends DistributionCenter {

  /**
   * 上级nickname
   */
  private String superiorName;
  /**
   * 当前用户昵称
   */
  private String nickname;
  /**
   * 当前用户头像
   */
  private String avatar;
}
