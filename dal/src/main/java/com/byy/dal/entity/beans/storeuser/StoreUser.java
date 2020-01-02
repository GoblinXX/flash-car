package com.byy.dal.entity.beans.storeuser;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 门店后台用户
 * @author: Goblin
 * @create: 2019-06-12 10:45
 **/
@Alias("StoreUser")
@Setter
@Getter
@ToString
public class StoreUser extends BaseEntityArchive {

  /**
   * 账户
   */
  private String username;
  /**
   * 密码
   */
  private String password;
  /**
   * 关联的门店id
   */
  private Long storeId;
}
