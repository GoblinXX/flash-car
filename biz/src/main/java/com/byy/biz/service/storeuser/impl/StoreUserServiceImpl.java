package com.byy.biz.service.storeuser.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.storeuser.StoreUserService;
import com.byy.dal.entity.beans.storeuser.StoreUser;
import com.byy.dal.mapper.storeuser.StoreUserMapper;
import org.springframework.stereotype.Service;

/**
 * @program: flash-car
 * @description: 门店用户service实现类
 * @author: Goblin
 * @create: 2019-06-12 11:04
 **/
@Service
public class StoreUserServiceImpl extends ServiceImpl<StoreUserMapper, StoreUser> implements
    StoreUserService {

}
