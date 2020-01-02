package com.byy.dal.entity.dos.storecash;

import com.byy.dal.entity.beans.storecash.StoreCash;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @author: goblin
 * @date: 2019-06-27 15:44:29
 */
@Setter
@Getter
@ToString
@Alias("StoreCashDO")
public class StoreCashDO extends StoreCash {

  /**
   * 负责人姓名
   */
  private String name;
  /**
   * 负责人电话
   */
  private String phone;
  /**
   * 门店名称
   */
  private String storeName;
}
