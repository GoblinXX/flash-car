package com.byy.api.service.controller.comment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.comment.CommentForm;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.utils.MapParamsUtils;
import com.byy.api.service.utils.PointUtils;
import com.byy.api.service.vo.action.DeletionVO;
import com.byy.api.service.vo.comment.CommentVO;
import com.byy.biz.service.comment.CommentPicService;
import com.byy.biz.service.comment.CommentService;
import com.byy.biz.service.distribution.DistributionCenterService;
import com.byy.biz.service.order.OrderService;
import com.byy.biz.service.point.PointConfigService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.comment.Comment;
import com.byy.dal.entity.beans.comment.CommentPic;
import com.byy.dal.entity.beans.distribution.DistributionCenter;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.beans.point.PointConfig;
import com.byy.dal.entity.dos.comment.CommentDO;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.byy.api.response.ResponseObject.error;
import static com.byy.api.response.ResponseObject.success;
import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;
import static com.byy.dal.common.utils.helper.CheckHelper.isNotNull;
import static com.byy.dal.enums.OrderStatus.SUCCESS;
import static com.byy.dal.enums.SceneType.COMMENT;

/**
 * @author: master
 * @date: 2019-06-27 09:53:26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentController extends CommonController<CommentService> {

  private MapParamsUtils mapParamsUtils;

  private CommentPicService commentPicService;

  private OrderService orderService;

  private DistributionCenterService distributionCenterService;

  private PointConfigService pointConfigService;

  private PointUtils pointUtils;
  /**
   * 写评论
   *
   * @param form CommentForm
   * @return ResponseObject
   */
  @PostMapping("/app")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<CommentVO> saveComment(@RequestBody CommentForm form) {
    if (isNotNull(baseService.getOne(WrapperProvider.queryWrapper(Comment::getOrderId, form.getId())))){
      return error(-1,"评论发布失败,该订单已被评论!");
    }
    Comment comment = Transformer.fromBean(form, Comment.class);
    CheckHelper.trueOrThrow(baseService.save(comment), BizException::new, "评论发送失败,请检查相应参数!");
    if (isNotEmpty(form.getImgUrls())) {
      List<CommentPic> commentPics =
          form.getImgUrls().stream()
              .map(
                  imgUrl -> {
                    CommentPic commentPic = new CommentPic();
                    commentPic.setCommentId(comment.getId());
                    commentPic.setImgUrl(imgUrl);
                    return commentPic;
                  })
              .collect(Collectors.toList());
      CheckHelper.trueOrThrow(
          commentPicService.saveBatch(commentPics), BizException::new, "评论图片保存失败,请检查相应参数!");
    }
    Order order = orderService.getById(form.getOrderId());
    order.setStatus(SUCCESS);
    CheckHelper.trueOrThrow(orderService.updateById(order), BizException::new, "订单状态修改失败,请检查相应参数!");
    // 计算佣金
    distributionCenterService.computationOfCommissionRebate(order);
    return success(Transformer.fromBean(form, CommentVO.class));
  }

  /**
   * 后台展示评论
   *
   * @param form
   * @param searchForm
   * @return
   */
  @GetMapping("/back/page")
  public ResponseObject<ImmutableMap<String, Object>> getCommentListSys(
      CommentForm form, SearchForm searchForm) {
    Map<String, Object> params = getParamsMap(mapParamsUtils.getMapParams(searchForm), form);
    IPage<CommentDO> page = baseService.getCommentList(form.newPage(), params);
    return toPageMap(Transformer.fromList(page.getRecords(), CommentVO.class), page.getTotal());
  }

  /**
   * 根据id查评论详情
   *
   * @param commentId
   * @return
   */
  @GetMapping("/back/{commentId}")
  public ResponseObject<CommentVO> getCommentSys(@PathVariable("commentId") Long commentId) {
    CommentVO commentVO = Transformer.fromBean(baseService.getComment(commentId), CommentVO.class);
    commentVO.setCommentPics(
        commentPicService.list(
            WrapperProvider.queryWrapper(CommentPic::getCommentId, commentVO.getId())));
    return success(commentVO);
  }

  /**
   * 删除评论
   *
   * @param commentId
   * @return
   */
  @DeleteMapping("/back/{commentId}")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<Object> deleteCommentSys(@PathVariable("commentId") Long commentId) {
    commentPicService.update(WrapperProvider.removeWrapper(CommentPic::getCommentId, commentId));
    return trueOrError(
        baseService.update(WrapperProvider.removeWrapper(Comment::getId, commentId)),
        commentId,
        "删除评论失败,请检查相应参数!");
  }

  /**
   * 修改评论审核状态
   *
   * @param form
   * @return
   */
  @PutMapping("/back")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<CommentVO> modifyCommentStatusSys(@RequestBody CommentForm form) {
    boolean update =
        baseService.update(
            WrapperProvider.updateWrapper(Comment::getId, form.getId())
                .set(Comment::getApprovalStatus, form.getApprovalStatus()));
    Long userId = orderService.getById(baseService.getById(form.getId()).getOrderId()).getUserId();
    PointConfig pointConfig = pointConfigService.getOne(WrapperProvider.queryWrapper(PointConfig::getScene, COMMENT));
    //将积分及积分变化量存入
    pointUtils.savePointAndRecord(userId,pointConfig.getScene(),pointConfig.getAmount(),null);
    return trueOrError(update, Transformer.fromBean(form, CommentVO.class), "修改评论审核状态失败,请检查相应参数!");
  }

  /**
   * 通过订单id查该订单评论
   *
   * @param orderId
   * @return
   */
  @GetMapping("/back/order/{orderId}")
  public ResponseObject<CommentVO> getCommentByOrderId(@PathVariable("orderId") Long orderId) {
    CommentVO commentVO =
        Transformer.fromBean(baseService.getCommentByOrderId(orderId), CommentVO.class);
    commentVO.setCommentPics(
        commentPicService.list(
            WrapperProvider.queryWrapper(CommentPic::getCommentId, commentVO.getId())));
    return success(commentVO);
  }

  /**
   * 通过商品id查该商品的评论
   *
   * @param productId
   * @return
   */
  @GetMapping("/app/product/{productId}")
  public ResponseObject<ImmutableMap<String, Object>> getCommentByProductId(
      @PathVariable("productId") Long productId, IPageForm iPageForm) {
    IPage<CommentDO> page = baseService.getCommentByProductId(iPageForm.newPage(), productId);
    List<CommentVO> commentVOS = setCommentPic(page);
    return toPageMap(commentVOS, page.getTotal());
  }

  /**
   * 通过租赁商品id查改租赁商品评论
   *
   * @param rentProductId
   * @return
   */
  @GetMapping("/app/rentProduct/{rentProductId}")
  public ResponseObject<ImmutableMap<String, Object>> getCommentRentProductId(
      @PathVariable("rentProductId") Long rentProductId, IPageForm iPageForm) {
    IPage<CommentDO> page =
        baseService.getCommentByRentProductId(iPageForm.newPage(), rentProductId);
    List<CommentVO> commentVOS = setCommentPic(page);
    return toPageMap(commentVOS, page.getTotal());
  }

  /**
   * 将评论图片放入评论对象中
   *
   * @param page
   * @return
   */
  private List<CommentVO> setCommentPic(IPage<CommentDO> page) {
    List<CommentVO> commentVOS = Transformer.fromList(page.getRecords(), CommentVO.class);
    commentVOS =
        commentVOS.stream()
            .peek(
                commentVO ->
                    commentVO.setCommentPics(
                        commentPicService.list(
                            WrapperProvider.queryWrapper(
                                CommentPic::getCommentId, commentVO.getId()))))
            .collect(Collectors.toList());
    return commentVOS;
  }

  /**
   * 拼装查询参数
   *
   * @param params
   * @param form
   * @return
   */
  private Map<String, Object> getParamsMap(Map<String, Object> params, CommentForm form) {
    if (isNotEmpty(form.getApprovalStatus())) {
      params.put("approvalStatus", form.getApprovalStatus());
    }
    return params;
  }
}
