package com.byy.api.service.vo.storecash;

import com.byy.dal.enums.CashType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Alias("StoreCashVO")
public class StoreCashVO {

  /**
   * 主键id
   */
  private Long id;
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
  /**
   * 提现时间
   */
  private LocalDateTime createdAt;

}
