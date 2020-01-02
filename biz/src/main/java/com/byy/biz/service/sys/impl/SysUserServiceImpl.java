package com.byy.biz.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.sys.SysUserService;
import com.byy.dal.entity.beans.sys.SysUser;
import com.byy.dal.mapper.sys.SysUserMapper;
import org.springframework.stereotype.Service;

/**
 * @author: yyc
 * @date: 19-3-20 下午1:29
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {}
