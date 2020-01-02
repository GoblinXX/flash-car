package com.byy.api.service.form.store;

import com.byy.api.service.form.auditstore.AuditStoreForm;
import com.byy.dal.entity.beans.store.Store;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 门店form
 * @author: Goblin
 * @create: 2019-06-12 10:31
 **/
@Setter
@Getter
@ToString
public class StoreForm extends Store {

  /**
   * 门店审核form
   */
  private AuditStoreForm auditStoreForm;
  /**
   * 门店账户
   */
  private String username;
  /**
   * 门店账户密码
   */
  private String password;
  /**
   * 门店详情图集合
   */
  private List<String> images;

  //-------------------------

}
