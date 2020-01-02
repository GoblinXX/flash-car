package com.byy.biz.service.address.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.address.UserAddressService;
import com.byy.dal.entity.beans.address.UserAddress;
import com.byy.dal.mapper.address.UserAddressMapper;
import org.springframework.stereotype.Service;

/**
 * @author: yyc
 * @date: 2019-06-17 15:51:22
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService {}
