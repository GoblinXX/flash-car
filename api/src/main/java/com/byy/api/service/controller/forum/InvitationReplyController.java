package com.byy.api.service.controller.forum;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.forum.InvitationReplyForm;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.page.PageForm;
import com.byy.api.service.vo.forum.InvitationReplyVO;
import com.byy.biz.service.forum.InvitationDetailService;
import com.byy.biz.service.forum.InvitationReplyService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.forum.InvitationDetail;
import com.byy.dal.entity.beans.forum.InvitationReply;
import com.byy.dal.entity.dos.forum.InvitationReplyDO;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotNull;
import static com.byy.api.response.ResponseObject.error;
import static com.byy.api.response.ResponseObject.success;

/** @Author: xcf @Date: 21/06/19 上午 11:19 @Description: */
@RestController
@AllArgsConstructor
@RequestMapping("/invitation/reply")
public class InvitationReplyController extends CommonController<InvitationReplyService> {

  private InvitationDetailService invitationDetailService;

  /**
   * 后台查看改贴回复(一级回复)
   *
   * @param invitationDetailId
   * @param iPage
   * @return
   */
  @GetMapping("/back/{invitationDetailId}")
  public ResponseObject<ImmutableMap<String, Object>> getReplyByInvitationDetailId(
      @PathVariable("invitationDetailId") Long invitationDetailId, IPageForm iPage) {
    IPage<InvitationReplyDO> page =
        baseService.getReplyByInvitationDetailId(iPage.newPage(), invitationDetailId);
    List<InvitationReplyVO> invitationReplyVOS =
        Transformer.fromList(page.getRecords(), InvitationReplyVO.class);
    ImmutableMap<String, Object> map =
        ImmutableMap.of("list", invitationReplyVOS, "total", page.getTotal());
    return success(map);
  }

  /**
   * 后台删除帖子回复(一级回复以及其下面所有二级回复)
   *
   * @param invitationReplyId
   * @return
   */
  @DeleteMapping("/back/{invitationReplyId}")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<Object> removeReplyByInvitationReplyId(
      @PathVariable("invitationReplyId") Long invitationReplyId) {
    baseService.update(
        WrapperProvider.removeWrapper(InvitationReply::getSuperReplyId, invitationReplyId));
    return trueOrError(
        baseService.update(
            WrapperProvider.removeWrapper(InvitationReply::getId, invitationReplyId)),
        invitationReplyId,
        "回复删除失败.请检查相应参数!");
  }

  /**
   * 后台查看该一级回复下的所有二级回复
   *
   * @param form
   * @return
   */
  @GetMapping("/back/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllReplyByInvitationReplyId(
      InvitationReplyForm form) {
    IPage<InvitationReplyDO> page =
        baseService.getAllReplyByInvitationReplyId(form.newPage(), form.getInvitationReplyId());
    return success(
        ImmutableMap.of(
            "list",
            Transformer.fromList(page.getRecords(), InvitationReplyVO.class),
            "total",
            page.getTotal()));
  }

  /**
   * 小程序端根据一级回复查询二级回复
   *
   * @param invitationReplyId
   * @return
   */
  @GetMapping("/app/subordinate/{invitationReplyId}")
  public ResponseObject<List<InvitationReplyVO>> getSubordinateReply(
      @PathVariable("invitationReplyId") Long invitationReplyId) {
    List<InvitationReplyVO> invitationReplyVOS =
        Transformer.fromList(
            baseService.getSubordinateReply(invitationReplyId), InvitationReplyVO.class);
    return success(invitationReplyVOS);
  }

  /**
   * 回复
   *
   * @param form
   * @return
   */
  @PostMapping("/app")
  public ResponseObject<InvitationReplyVO> saveInvitationReply(
      @RequestBody InvitationReplyForm form) {
    Long userId = getCurrentUserId();
    InvitationReply invitationReply = Transformer.fromBean(form, InvitationReply.class);
    InvitationDetail invitationDetail = invitationDetailService.getById(form.getInvitationId());
    if (form.getSuperReplyId() != null && !form.getSuperReplyId().equals(0L)) {
      if (!baseService
              .getOne(WrapperProvider.queryWrapper(InvitationReply::getId, form.getSuperReplyId()))
              .getUserId()
              .equals(userId)
          && !invitationDetail.getUserId().equals(userId)) {
        return error(-1, "回复失败,只有发帖人与评论人能回复!");
      } else {
        // 设置拥有人
        if (invitationDetail.getUserId().equals(userId)) {
          InvitationReply superInvitationReply =
              baseService.getOne(
                  WrapperProvider.queryWrapper(InvitationReply::getId, form.getSuperReplyId()));
          if (superInvitationReply.getUserId().equals(userId)) {
            invitationReply.setOwnerId(null);
          } else {
            invitationReply.setOwnerId(superInvitationReply.getUserId());
          }
        } else {
          invitationReply.setOwnerId(invitationDetail.getUserId());
        }
        invitationReply.setUserId(userId);
        return trueOrError(
            baseService.save(invitationReply),
            Transformer.fromBean(invitationReply, InvitationReplyVO.class),
            "回复帖子失败,请检查相应参数!");
      }
    } else {
      // 设置拥有人
      if (invitationDetail.getUserId().equals(userId)) {
        invitationReply.setOwnerId(null);
      } else {
        invitationReply.setOwnerId(invitationDetail.getUserId());
      }
      invitationReply.setUserId(userId);
      return trueOrError(
          baseService.save(invitationReply),
          Transformer.fromBean(invitationReply, InvitationReplyVO.class),
          "回复帖子失败,请检查相应参数!");
    }
  }

  /**
   * 查询自己的所有回复
   *
   * @param form
   * @return
   */
  @GetMapping("/app")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<ImmutableMap<String, Object>> getOwnReply(@Valid InvitationReplyForm form) {
    Long userId = getCurrentUserId();
    IPage<InvitationReplyDO> page = baseService.getOwnReply(form.newPage(), userId);
    List<InvitationReplyVO> invitationReplyVOS =
        Transformer.fromList(page.getRecords(), InvitationReplyVO.class);
    //查出所有未看过的回复更新为已观看
    List<InvitationReply> invitationReplies = baseService.list(WrapperProvider.queryWrapper(InvitationReply::getOwnerId, userId).eq(InvitationReply::getWatched, 0));
    invitationReplies = invitationReplies.stream().peek(invitationReply -> invitationReply.setWatched(true)).collect(Collectors.toList());
    if (isNotNull(invitationReplies)) {
      CheckHelper.trueOrThrow(baseService.updateBatchById(invitationReplies), BizException::new,"修改失败!");
    }
    return toPageMap(invitationReplyVOS, page.getTotal());
  }
}
