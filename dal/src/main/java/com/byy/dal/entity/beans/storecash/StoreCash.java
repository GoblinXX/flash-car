package com.byy.dal.entity.beans.storecash;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import com.byy.dal.enums.CashType;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 门店提现
 * @author: Goblin
 * @create: 2019-06-27 15:37
 **/
@Getter
@Setter
@ToString
@Alias("StoreCash")
public class StoreCash extends BaseEntityArchive {

  /**
   * 门店id
   */
  private Long storeId;
  /**
   * 提现状态
   */
  private CashType status;
  /**
   * 提现额
   */
  private BigDecimal cashAmount;

  /**
   * 开户银行
   */
  private String bank;
  /**
   * 提现账号
   */
  private String cashAmountNumber;
  /**
   * 提现申请人
   */
  private String cashName;

}
