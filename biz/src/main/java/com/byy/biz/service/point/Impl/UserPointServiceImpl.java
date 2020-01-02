package com.byy.biz.service.point.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.point.UserPointService;
import com.byy.dal.entity.beans.point.UserPoint;
import com.byy.dal.mapper.point.UserPointMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 02:36
 * @Description:
 */
@Service
public class UserPointServiceImpl extends ServiceImpl<UserPointMapper, UserPoint> implements UserPointService {
}
