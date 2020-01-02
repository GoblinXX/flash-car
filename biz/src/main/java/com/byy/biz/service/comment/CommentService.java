package com.byy.biz.service.comment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.comment.Comment;
import com.byy.dal.entity.dos.comment.CommentDO;

import java.util.Map;

/**
 * @author: master
 * @date: 2019-06-27 09:53:26
 */
public interface CommentService extends IService<Comment> {

  /**
   * 获取评论List
   * @param page
   * @param params
   * @return
   */
  IPage<CommentDO> getCommentList(IPage page, Map<String,Object> params);

  /**
   * 通过id查评论详情
   * @param commentId
   * @return
   */
  CommentDO getComment(Long commentId);

  /**
   * 通过订单id查该订单评论
   * @param orderId
   * @return
   */
  CommentDO getCommentByOrderId(Long orderId);

  /**
   * 通过商品id查询该商品评论第一条
   * @param productId
   * @return
   */
  CommentDO getProductComment(Long productId);

  /**
   * 通过商品id查询商品评论
   * @param productId
   * @return
   */
  IPage<CommentDO> getCommentByProductId(IPage page,Long productId);

  /**
   * 通过id查询租赁商品评论第一条
   * @param rentProductId
   * @return
   */
  CommentDO getRentProductComment(Long rentProductId);

  /**
   * 通过id查询租赁商品评论
   * @param rentProductId
   * @return
   */
  IPage<CommentDO> getCommentByRentProductId(IPage page,Long rentProductId);

  /**
   * 查询该商品评论总数
   * @param productId
   * @return
   */
  Integer getProductCommentAmount(Long productId);

  /**
   * 查询该商品评论总数
   * @param rentProductId
   * @return
   */
  Integer getRentProductCommentAmount(Long rentProductId);
}

