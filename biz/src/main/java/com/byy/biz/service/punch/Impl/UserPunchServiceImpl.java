package com.byy.biz.service.punch.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.punch.UserPunchService;
import com.byy.dal.entity.beans.punch.UserPunch;
import com.byy.dal.mapper.punch.UserPunchMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 02:35
 * @Description:
 */
@Service
public class UserPunchServiceImpl extends ServiceImpl<UserPunchMapper, UserPunch> implements UserPunchService {
}
