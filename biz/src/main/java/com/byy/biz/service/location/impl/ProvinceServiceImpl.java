package com.byy.biz.service.location.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.location.ProvinceService;
import com.byy.dal.entity.beans.location.Province;
import com.byy.dal.mapper.location.ProvinceMapper;
import org.springframework.stereotype.Service;

/**
 * @author: yyc
 * @date: 19-4-10 下午9:38
 */
@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province>
    implements ProvinceService {}
