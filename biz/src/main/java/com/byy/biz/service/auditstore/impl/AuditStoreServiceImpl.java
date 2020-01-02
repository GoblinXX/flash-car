package com.byy.biz.service.auditstore.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.auditstore.AuditStoreService;
import com.byy.dal.entity.beans.auditstore.AuditStore;
import com.byy.dal.mapper.auditstore.AuditStoreMapper;
import org.springframework.stereotype.Service;

/**
 * @program: flash-car
 * @description: 门店审核实现类
 * @author: Goblin
 * @create: 2019-06-11 16:39
 **/
@Service
public class AuditStoreServiceImpl extends ServiceImpl<AuditStoreMapper, AuditStore> implements
    AuditStoreService {

}
