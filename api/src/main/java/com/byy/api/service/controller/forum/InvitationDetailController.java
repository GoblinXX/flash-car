package com.byy.api.service.controller.forum;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.forum.InvitationDetailForm;
import com.byy.api.service.form.forum.InvitationReplyForm;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.utils.MapParamsUtils;
import com.byy.api.service.vo.forum.InvitationDetailVO;
import com.byy.api.service.vo.forum.InvitationReplyVO;
import com.byy.biz.service.forum.InvitationDetailService;
import com.byy.biz.service.forum.InvitationPicService;
import com.byy.biz.service.forum.InvitationReplyService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.forum.InvitationDetail;
import com.byy.dal.entity.beans.forum.InvitationPic;
import com.byy.dal.entity.beans.forum.InvitationReply;
import com.byy.dal.entity.dos.forum.InvitationDetailDO;
import com.byy.dal.entity.dos.forum.InvitationReplyDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.api.response.ResponseObject.error;
import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;
import static com.byy.dal.enums.ApprovalStatus.UNREVIEWED;
import static com.byy.dal.enums.ImgType.COVER;

/** @Author: xcf @Date: 20/06/19 下午 03:35 @Description: */
@RestController
@AllArgsConstructor
@RequestMapping("/invitation")
public class InvitationDetailController extends CommonController<InvitationDetailService> {

  private InvitationPicService invitationPicService;

  private InvitationReplyService invitationReplyService;

  private MapParamsUtils mapParamsUtils;

  /**
   * 后台分页条件查询所有已通过的帖子
   *
   * @param form
   * @param searchForm
   * @return
   */
  @GetMapping("/back/page")
  public ResponseObject<ImmutableMap<String, Object>> getPassInvitationDetailSys(
      @Valid InvitationDetailForm form, SearchForm searchForm) {
    Map<String, Object> params = mapParams(form, searchForm);
    IPage<InvitationDetailDO> page = baseService.getPassInvitationDetailSys(form.newPage(), params);
    List<InvitationDetailVO> invitationDetailVOS =
        Transformer.fromList(page.getRecords(), InvitationDetailVO.class);
    return success(ImmutableMap.of("list", invitationDetailVOS, "total", page.getTotal()));
  }

  /**
   * 查看详情(通过id查帖子详情)
   *
   * @param invitationDetailId
   * @return
   */
  @GetMapping("/back/{invitationDetailId}")
  public ResponseObject<InvitationDetailVO> getInvitationDetailByIdSys(
      @PathVariable("invitationDetailId") Long invitationDetailId) {
    return success(getInvitationDetailById(invitationDetailId));
  }

  /**
   * 修改帖子隐藏状态
   *
   * @param form
   * @return
   */
  @PutMapping("/back/hidden")
  public ResponseObject<InvitationDetailVO> modifyInvitationDetailHiddenStatus(
      @RequestBody InvitationDetailForm form) {
    return trueOrError(
        baseService.updateById(form),
        Transformer.fromBean(form, InvitationDetailVO.class),
        "修改帖子隐藏状态失败,请检查相应参数!");
  }

  /**
   * 删除帖子
   *
   * @param invitationDetailId
   * @return
   */
  @DeleteMapping("/back/{invitationDetailId}")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<Object> removeInvitationDetailById(
      @PathVariable("invitationDetailId") Long invitationDetailId) {
    return trueOrError(deleteInvitation(invitationDetailId), invitationDetailId, "删除帖子失败,请检查相应参数!");
  }

  /**
   * 分页查询所有状态的帖子
   *
   * @param form
   * @param searchForm
   * @return
   */
  @GetMapping("/back/approval/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllInvitationDetailSys(
      @Valid InvitationDetailForm form, SearchForm searchForm) {
    Map<String, Object> params = mapParams(form, searchForm);
    IPage<InvitationDetailDO> page = baseService.getAllInvitationDetailSys(form.newPage(), params);
    return toPageMap(
        Transformer.fromList(page.getRecords(), InvitationDetailVO.class), page.getTotal());
  }

  /**
   * 审核状态修改
   *
   * @param form
   * @return
   */
  @PutMapping("/back/approval")
  public ResponseObject<InvitationDetailVO> modifyInvitationDetailApprovalStatus(
      @RequestBody InvitationDetailForm form) {
    return trueOrError(
        baseService.updateById(form),
        Transformer.fromBean(form, InvitationDetailVO.class),
        "审核状态修改失败,请检查相应参数!");
  }

  /**
   * 小程序端发帖
   *
   * @param form
   * @return
   */
  @PostMapping("/app")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<InvitationDetailVO> saveInvitationDetailApp(
      @RequestBody InvitationDetailForm form) {
    Long userId = getCurrentUserId();
    form.setUserId(userId);
    CheckHelper.trueOrThrow(baseService.save(form), BizException::new, "发帖失败,请检查相应参数!");
    List<InvitationPic> invitationPics =
        form.getInvitationPics().stream()
            .peek(invitationPic -> invitationPic.setInvitationId(form.getId()))
            .collect(Collectors.toList());
    CheckHelper.trueOrThrow(
        invitationPicService.saveBatch(invitationPics), BizException::new, "帖子图片添加失败,请检查相应参数!");
    return success(Transformer.fromBean(form, InvitationDetailVO.class));
  }

  /**
   * 小程序端分页条件查询
   *
   * @return
   */
  @GetMapping("/app/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllInvitationDetailApp(
      @Valid InvitationDetailForm form, SearchForm searchForm) {
    Map<String, Object> params = mapParams(form, searchForm);
    IPage<InvitationDetailDO> page = baseService.getPassInvitationDetailApp(form.newPage(), params);
    List<InvitationDetailVO> invitationDetailVOS =
        Transformer.fromList(page.getRecords(), InvitationDetailVO.class);
    invitationDetailVOS.stream()
        .peek(
            invitationDetailVO ->
                invitationDetailVO.setInvitationPics(
                    invitationPicService.list(
                        WrapperProvider.queryWrapper(InvitationPic::getImgType, COVER)
                            .eq(InvitationPic::getInvitationId, invitationDetailVO.getId()))))
        .collect(Collectors.toList());
    return toPageMap(invitationDetailVOS, page.getTotal());
  }

  /**
   * 小程序端通过id查帖子详情
   *
   * @param invitationDetailId
   * @return
   */
  @GetMapping("/app/{invitationDetailId}")
  public ResponseObject<ImmutableMap<String, Object>> getInvitationDetailByIdApp(
      @PathVariable("invitationDetailId") Long invitationDetailId, IPageForm iPageForm) {
    InvitationDetailVO invitationDetailVO = getInvitationDetailById(invitationDetailId);
    IPage<InvitationReplyDO> page =
        invitationReplyService.getReplyByInvitationDetailId(
            iPageForm.newPage(), invitationDetailId);
    List<InvitationReplyVO> invitationReplyVOS =
        Transformer.fromList(page.getRecords(), InvitationReplyVO.class);
    invitationReplyVOS =
        invitationReplyVOS.stream()
            .peek(
                invitationReplyVO -> {
                  invitationReplyVO.setLowerInvitationReplies(
                      invitationReplyService.getSubordinateReplyBySuperId(
                          invitationReplyVO.getId()));
                  invitationReplyVO.setLowerCount(
                      invitationReplyService.count(
                          WrapperProvider.queryWrapper(
                              InvitationReply::getSuperReplyId, invitationReplyVO.getId())));
                })
            .collect(Collectors.toList());
    return success(
        ImmutableMap.of(
            "invitationDetail",
            invitationDetailVO,
            "invitationReplyList",
            invitationReplyVOS,
            "invitationReplyTotal",
            page.getTotal()));
  }

  /**
   * 查询自己的帖子
   *
   * @param form
   * @return
   */
  @GetMapping("/app/own")
  public ResponseObject<ImmutableMap<String, Object>> getOwnInvitation(
      @Valid InvitationDetailForm form) {
    Long userId = getCurrentUserId();
    LambdaQueryWrapper<InvitationDetail> wrapper;
    if (form.getApprovalStatus() != null) {
      wrapper =
          WrapperProvider.queryWrapper(
                  InvitationDetail::getApprovalStatus, form.getApprovalStatus())
              .eq(InvitationDetail::getUserId, userId);
    } else {
      wrapper =
          WrapperProvider.queryWrapper(InvitationDetail::getApprovalStatus, UNREVIEWED)
              .eq(InvitationDetail::getUserId, userId);
    }
    IPage<InvitationDetail> page = baseService.page(form.newPage(), wrapper);
    List<InvitationDetailVO> invitationDetailVOS =
        Transformer.fromList(page.getRecords(), InvitationDetailVO.class);
    invitationDetailVOS =
        invitationDetailVOS.stream()
            .peek(
                invitationDetailVO ->
                    invitationDetailVO.setInvitationPics(
                        invitationPicService.list(
                            WrapperProvider.queryWrapper(InvitationPic::getImgType, COVER)
                                .eq(InvitationPic::getInvitationId, invitationDetailVO.getId()))))
            .collect(Collectors.toList());
    return toPageMap(invitationDetailVOS, page.getTotal());
  }

  /**
   * 删除自己的帖子
   *
   * @param invitationDetailId
   * @return
   */
  @DeleteMapping("/app/{invitationDetailId}")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<Object> deleteInvitationById(
      @PathVariable("invitationDetailId") Long invitationDetailId) {
    return trueOrError(deleteInvitation(invitationDetailId), invitationDetailId, "删除帖子错误,请检查相应参数!");
  }

  /**
   * 删除帖子和帖子图片,回复
   *
   * @param invitationDetailId
   * @return
   */
  private boolean deleteInvitation(Long invitationDetailId) {
    invitationPicService.update(
        WrapperProvider.removeWrapper(InvitationPic::getInvitationId, invitationDetailId));
    invitationReplyService.update(
        WrapperProvider.removeWrapper(InvitationReply::getInvitationId, invitationDetailId));
    return baseService.update(
        WrapperProvider.removeWrapper(InvitationDetail::getId, invitationDetailId));
  }

  /**
   * 通过id查询帖子详情
   *
   * @param invitationDetailId
   * @return
   */
  private InvitationDetailVO getInvitationDetailById(Long invitationDetailId) {
    InvitationDetailVO invitationDetailVO =
        Transformer.fromBean(
            baseService.getInvitationDetailById(invitationDetailId), InvitationDetailVO.class);
    invitationDetailVO.setInvitationPics(
        invitationPicService.list(
            WrapperProvider.queryWrapper(InvitationPic::getInvitationId, invitationDetailId)));
    return invitationDetailVO;
  }

  /**
   * 后台查询参数封装 将查询条件存入map
   *
   * @param form
   * @param searchForm
   * @return
   */
  private Map<String, Object> mapParams(InvitationDetailForm form, SearchForm searchForm) {
    Map<String, Object> params = mapParamsUtils.getMapParams(searchForm);
    if (form.getInvitationType() != null) {
      params.put("invitationType", form.getInvitationType());
    }
    if (form.getSupplyType() != null) {
      params.put("supplyType", form.getSupplyType());
    }
    if (form.getReleaseType() != null) {
      params.put("releaseType", form.getReleaseType());
    }
    if (form.getApprovalStatus() != null) {
      params.put("approvalStatus", form.getApprovalStatus());
    }
    return params;
  }
}
