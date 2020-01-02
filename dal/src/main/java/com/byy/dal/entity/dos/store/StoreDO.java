package com.byy.dal.entity.dos.store;

import com.byy.dal.entity.beans.store.Store;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 门店do
 * @author: Goblin
 * @create: 2019-06-12 14:09
 **/
@Setter
@Getter
@ToString
@Alias("StoreDO")
public class StoreDO extends Store {

  /**
   * 门店负责人
   */
  private String username;
  /**
   * 门店负责人电话
   */
  private String userPhone;
}
