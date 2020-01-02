package com.byy.biz.service.comment.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.dal.entity.beans.comment.Comment;
import com.byy.dal.entity.dos.comment.CommentDO;
import com.byy.dal.mapper.comment.CommentMapper;
import com.byy.biz.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: master
 * @date: 2019-06-27 09:53:26
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {

  @Autowired
  private CommentMapper commentMapper;

  @Override
  public IPage<CommentDO> getCommentList(IPage page, Map<String, Object> params) {
    return commentMapper.getCommentList(page,params);
  }

  @Override
  public CommentDO getComment(Long commentId) {
    return commentMapper.getComment(commentId);
  }

  @Override
  public CommentDO getCommentByOrderId(Long orderId) {
    return commentMapper.getCommentByOrderId(orderId);
  }

  @Override
  public CommentDO getProductComment(Long productId) {
    return commentMapper.getProductComment(productId);
  }

  @Override
  public CommentDO getRentProductComment(Long rentProDuctId) {
    return commentMapper.getRentProductComment(rentProDuctId);
  }

  @Override
  public IPage<CommentDO> getCommentByProductId(IPage page,Long productId) {
    return commentMapper.getCommentByProductId(page,productId);
  }

  @Override
  public IPage<CommentDO> getCommentByRentProductId(IPage page,Long rentProductId) {
    return commentMapper.getCommentByRentProductId(page,rentProductId);
  }

  @Override
  public Integer getProductCommentAmount(Long productId) {
    return commentMapper.getProductCommentAmount(productId);
  }

  @Override
  public Integer getRentProductCommentAmount(Long rentProductId) {
    return commentMapper.getRentProductCommentAmount(rentProductId);
  }
}
