package com.byy.dal.entity.dos.commission;

import com.byy.dal.entity.beans.commission.CommissionRecords;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description:
 * @author: Goblin
 * @create: 2019-06-28 09:48
 **/
@Alias("CommissionRecordsDO")
@Getter
@Setter
@ToString
public class CommissionRecordsDO extends CommissionRecords {

  /**
   * 下级昵称
   */
  private String nickname;

}
