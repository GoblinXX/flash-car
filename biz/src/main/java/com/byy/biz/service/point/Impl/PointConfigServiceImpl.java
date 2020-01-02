package com.byy.biz.service.point.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.point.PointConfigService;
import com.byy.dal.entity.beans.point.PointConfig;
import com.byy.dal.mapper.point.PointConfigMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 02:39
 * @Description:
 */
@Service
public class PointConfigServiceImpl extends ServiceImpl<PointConfigMapper, PointConfig> implements PointConfigService {
}
