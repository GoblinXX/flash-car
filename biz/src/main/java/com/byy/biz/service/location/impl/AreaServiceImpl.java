package com.byy.biz.service.location.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.location.AreaService;
import com.byy.dal.entity.beans.location.Area;
import com.byy.dal.mapper.location.AreaMapper;
import org.springframework.stereotype.Service;

/**
 * @author: yyc
 * @date: 19-4-10 下午9:38
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area>
    implements AreaService {}
