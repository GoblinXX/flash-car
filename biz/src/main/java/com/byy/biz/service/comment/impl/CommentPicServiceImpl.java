package com.byy.biz.service.comment.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.dal.entity.beans.comment.CommentPic;
import com.byy.dal.mapper.comment.CommentPicMapper;
import com.byy.biz.service.comment.CommentPicService;
import org.springframework.stereotype.Service;

/**
 * @author: master
 * @date: 2019-06-27 11:48:55
 */
@Service
public class CommentPicServiceImpl extends ServiceImpl<CommentPicMapper, CommentPic>
    implements CommentPicService {}
