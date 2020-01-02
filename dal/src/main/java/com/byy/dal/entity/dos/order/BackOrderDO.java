package com.byy.dal.entity.dos.order;

import com.byy.dal.entity.beans.order.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 26/06/19 下午 01:56
 * @Description:
 */
@Setter
@Getter
@ToString
@Alias("BackOrderDO")
public class BackOrderDO extends Order {

  /** 门店名 */
  private String storeName;

  /** 分类名 */
  private String categoryName;
}
