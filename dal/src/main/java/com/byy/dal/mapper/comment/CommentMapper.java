package com.byy.dal.mapper.comment;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.dal.entity.beans.comment.Comment;
import com.byy.dal.entity.dos.comment.CommentDO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author: master
 * @date: 2019-06-27 09:53:26
 */
public interface CommentMapper extends BaseMapper<Comment> {

  /**
   * 获取评论List
   * @param page
   * @param params
   * @return
   */
  IPage<CommentDO> getCommentList(@Param("page") IPage page,@Param("params") Map<String, Object> params);

  /**
   * 通过id查评论详情
   * @param commentId
   * @return
   */
  CommentDO getComment(@Param("commentId") Long commentId);

  /**
   * 通过订单id查该订单评论
   * @param orderId
   * @return
   */
  CommentDO getCommentByOrderId(@Param("orderId") Long orderId);

  /**
   * 通过商品id查询该商品评论取第一条
   * @param productId
   * @return
   */
  CommentDO getProductComment(@Param("productId") Long productId);

  /**
   * 通过商品id查询商品评论
   * @param productId
   * @return
   */
  IPage<CommentDO> getCommentByProductId(@Param("page") IPage page,@Param("productId") Long productId);

  /**
   * 查询该商品评论总数
   * @return
   */
  Integer getProductCommentAmount(@Param("productId") Long productId);

  /**
   * 通过id查询租赁商品评论第一条
   * @param rentProductId
   * @return
   */
  CommentDO getRentProductComment(@Param("rentProductId") Long rentProductId);

  /**
   * 通过id查询租赁商品评论
   * @param rentProductId
   * @return
   */
  IPage<CommentDO> getCommentByRentProductId(@Param("page") IPage page,@Param("rentProductId") Long rentProductId);

  /**
   * 查询该商品评论总数
   * @param rentProductId
   * @return
   */
  Integer getRentProductCommentAmount(@Param("rentProductId") Long rentProductId);
}

