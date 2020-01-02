package com.byy.biz.service.wechat.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.wechat.WeChatConfigService;
import com.byy.dal.entity.beans.wechat.WeChatConfig;
import com.byy.dal.mapper.wechat.WeChatConfigMapper;
import org.springframework.stereotype.Service;

/**
 * @author: yyc
 * @date: 19-4-5 下午8:06
 */
@Service
public class WeChatConfigServiceImpl extends ServiceImpl<WeChatConfigMapper, WeChatConfig>
    implements WeChatConfigService {}
