package com.byy.biz.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.sys.SysAuthorityService;
import com.byy.dal.entity.beans.sys.SysAuthority;
import com.byy.dal.mapper.sys.SysAuthorityMapper;
import org.springframework.stereotype.Service;

/**
 * @author: yyc
 * @date: 19-3-20 下午1:29
 */
@Service
public class SysAuthorityServiceImpl extends ServiceImpl<SysAuthorityMapper, SysAuthority>
    implements SysAuthorityService {}
