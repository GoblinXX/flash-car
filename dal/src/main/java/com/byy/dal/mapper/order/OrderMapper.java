package com.byy.dal.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.dos.order.OrderDO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author: yyc
 * @date: 2019-06-24 14:10:46
 */
public interface OrderMapper extends BaseMapper<Order> {}

