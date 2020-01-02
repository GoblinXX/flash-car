package com.byy.api.service.form.auditstore;

import com.byy.api.service.form.search.SearchForm;
import com.byy.dal.entity.beans.auditstore.AuditStore;
import com.byy.dal.enums.StoreCheckType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 门店审核表单
 * @author: Goblin
 * @create: 2019-06-11 16:49
 **/
@Setter
@Getter
@ToString
public class AuditStoreForm extends AuditStore {

  /**
   * 姓名、手机号
   */
  private String keyWord;
  /**
   * 状态
   */
  private StoreCheckType status;

}
