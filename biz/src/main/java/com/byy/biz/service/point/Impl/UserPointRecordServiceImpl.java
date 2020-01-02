package com.byy.biz.service.point.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.point.UserPointRecordService;
import com.byy.dal.entity.beans.point.UserPointRecord;
import com.byy.dal.mapper.point.UserPointRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 02:38
 * @Description:
 */
@Service
public class UserPointRecordServiceImpl extends ServiceImpl<UserPointRecordMapper, UserPointRecord> implements UserPointRecordService {
}
