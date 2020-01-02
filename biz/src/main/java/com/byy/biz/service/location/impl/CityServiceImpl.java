package com.byy.biz.service.location.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.location.CityService;
import com.byy.dal.entity.beans.location.City;
import com.byy.dal.mapper.location.CityMapper;
import org.springframework.stereotype.Service;

/**
 * @author: yyc
 * @date: 19-4-10 下午9:38
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City>
    implements CityService {}
