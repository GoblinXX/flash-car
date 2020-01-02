package com.byy.biz.service.refundorder.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.dal.entity.beans.refundorder.RefundOrder;
import com.byy.dal.mapper.refundorder.RefundOrderMapper;
import com.byy.biz.service.refundorder.RefundOrderService;
import org.springframework.stereotype.Service;

/**
 * @author: goblin
 * @date: 2019-06-24 16:01:09
 */
@Service
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder>
    implements RefundOrderService {

}
