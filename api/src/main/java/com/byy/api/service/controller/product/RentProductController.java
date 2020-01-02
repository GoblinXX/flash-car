package com.byy.api.service.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.product.RentProductForm;
import com.byy.api.service.vo.comment.CommentVO;
import com.byy.api.service.vo.product.RentProductVO;
import com.byy.biz.service.comment.CommentPicService;
import com.byy.biz.service.comment.CommentService;
import com.byy.biz.service.product.RentProductPicService;
import com.byy.biz.service.product.RentProductService;
import com.byy.biz.service.product.RentProductTimeService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.comment.CommentPic;
import com.byy.dal.entity.beans.product.RentProduct;
import com.byy.dal.entity.beans.product.RentProductPic;
import com.byy.dal.entity.beans.product.RentProductTime;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;
import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotNull;
import static com.byy.api.response.ResponseObject.error;
import static com.byy.api.response.ResponseObject.success;

/** @Author: xcf @Date: 13/06/19 下午 02:50 @Description: */
@RestController
@AllArgsConstructor
@RequestMapping("/rent")
public class RentProductController extends CommonController<RentProductService> {

  private RentProductPicService rentProductPicService;

  private RentProductTimeService rentProductTimeService;

  private CommentService commentService;

  private CommentPicService commentPicService;

  /**
   * 后台分页条件查询所有租赁商品
   *
   * @param form
   * @return
   */
  @GetMapping("/back/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllRentProductSys(
      @Valid RentProductForm form) {
    LambdaQueryWrapper<RentProduct> params = convertParams(form, "sys");
    IPage<RentProduct> page = baseService.page(form.newPage(), params);
    List<RentProductVO> rentProductVOS =
        Transformer.fromList(page.getRecords(), RentProductVO.class);
    rentProductVOS =
        rentProductVOS.stream()
            .map(rentProductVO -> getRentProductVOList(rentProductVO))
            .collect(Collectors.toList());
    ImmutableMap<String, Object> map =
        ImmutableMap.of("list", rentProductVOS, "total", page.getTotal());
    return success(map);
  }

  /**
   * 后台添加租赁商品
   *
   * @param form
   * @return
   */
  @PostMapping("/back")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<RentProductVO> saveRentProductSys(
      @Valid @RequestBody RentProductForm form) {
    RentProduct rentProduct = Transformer.fromBean(form, RentProduct.class);
    CheckHelper.trueOrThrow(baseService.save(rentProduct), BizException::new, "添加租赁商品失败,请检查相应参数!");
    Long rentProductId = rentProduct.getId();
    saveRentProductPic(form.getRentProductPics(), rentProductId);
    saveRentProductTime(form.getRentProductTimeList(), rentProductId);
    return success(Transformer.fromBean(form, RentProductVO.class));
  }

  /**
   * 后台根据id查询租赁商品
   *
   * @param rentProductId
   * @return
   */
  @GetMapping("/back/{rentProductId}")
  public ResponseObject<RentProductVO> getRentProductByPrimaryKeySys(
      @PathVariable("rentProductId") Long rentProductId) {
    RentProductVO rentProductVO = getRentProductById(rentProductId);
    return success(rentProductVO);
  }

  /**
   * 后台编辑租赁商品
   *
   * @param form
   * @return
   */
  @PutMapping("/back")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<RentProductVO> modifyRentProductSys(
      @Valid @RequestBody RentProductForm form) {
    RentProduct rentProduct = Transformer.fromBean(form, RentProduct.class);
    CheckHelper.trueOrThrow(
        baseService.updateById(rentProduct), BizException::new, "租赁商品更新失败,请检查相应参数@");
    Long rentProductId = rentProduct.getId();
    saveRentProductPic(form.getRentProductPics(), rentProductId);
    saveRentProductTime(form.getRentProductTimeList(), rentProductId);
    RentProductVO rentProductVO = Transformer.fromBean(form, RentProductVO.class);
    return success(rentProductVO);
  }

  /**
   * 后台修改上下架状态
   *
   * @param form
   * @return
   */
  @PutMapping("/back/sale")
  public ResponseObject<RentProductVO> setRentProductSaleSys(
      @Valid @RequestBody RentProductForm form) {
    RentProduct rentProduct = Transformer.fromBean(form, RentProduct.class);
    CheckHelper.trueOrThrow(
        baseService.updateById(rentProduct), BizException::new, "上下架状态修改失败,请检查相应参数!");
    return success(getRentProductById(rentProduct.getId()));
  }

  /**
   * 后台根据id删除租赁商品
   *
   * @param rentProductId
   * @return
   */
  @DeleteMapping("/back/{rentProductId}")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<Object> removeRentProductSys(
      @PathVariable("rentProductId") Long rentProductId) {
    boolean flag =
        baseService.removeById(rentProductId)
            && rentProductTimeService.remove(
                WrapperProvider.queryWrapper(RentProductTime::getRentProductId, rentProductId))
            && rentProductPicService.remove(
                WrapperProvider.queryWrapper(RentProductPic::getRentProductId, rentProductId));
    if (flag) {
      return success(rentProductId);
    } else {
      return error(null);
    }
  }

  /**
   * 小程序端分页查询
   *
   * @param form
   * @return
   */
  @GetMapping("/app/page")
  public ResponseObject<ImmutableMap<String, Object>> getAllRentProductApp(
      @Valid RentProductForm form) {
    LambdaQueryWrapper<RentProduct> params = convertParams(form, "app");
    IPage<RentProduct> page = baseService.page(form.newPage(), params);
    List<RentProductVO> rentProductVOS =
        Transformer.fromList(page.getRecords(), RentProductVO.class);
    ImmutableMap<String, Object> map =
        ImmutableMap.of(
            "list",
            rentProductVOS.stream()
                .map(rentProductVO -> getRentProductVOList(rentProductVO))
                .collect(Collectors.toList()),
            "total",
            page.getTotal());
    return success(map);
  }

  /**
   * 小程序端通过id查询租赁商品
   *
   * @param rentProductId
   * @return
   */
  @GetMapping("/app/{rentProductId}")
  public ResponseObject<RentProductVO> getRentProductByPrimaryKeyApp(
      @PathVariable("rentProductId") Long rentProductId) {
    RentProduct rentProduct = baseService.getById(rentProductId);
    RentProductVO rentProductVO = Transformer.fromBean(rentProduct, RentProductVO.class);
    rentProductVO = getRentProductVOList(rentProductVO);
    rentProductVO.setRentProductPics(
        rentProductPicService.list(
            WrapperProvider.queryWrapper(RentProductPic::getRentProductId, rentProductVO.getId())));
    rentProductVO.setRentProductTimes(
        rentProductTimeService.list(
            WrapperProvider.queryWrapper(
                RentProductTime::getRentProductId, rentProductVO.getId())));
    // 查询出商品评论
    CommentVO commentVO =
        Transformer.fromBean(commentService.getRentProductComment(rentProductId), CommentVO.class);
    if (isNotEmpty(commentVO)) {
      commentVO.setCommentPics(
          commentPicService.list(
              WrapperProvider.queryWrapper(CommentPic::getCommentId, commentVO.getId())));
      rentProductVO.setCommentVO(commentVO);
      rentProductVO.setCommentAmount(commentService.getRentProductCommentAmount(rentProductId));
    }
    return success(rentProductVO);
  }

  /**
   * 通过租期规格id查询租赁商品信息
   *
   * @param rentProductTimeId
   * @return
   */
  @GetMapping("/app/detail/{rentProductTimeId}")
  public ResponseObject<RentProductVO> getRentProductByTimeId(
      @PathVariable("rentProductTimeId") Long rentProductTimeId) {
    Long rentProductId = rentProductTimeService.getById(rentProductTimeId).getRentProductId();
    RentProductVO rentProductVO =
        Transformer.fromBean(baseService.getById(rentProductId), RentProductVO.class);
    rentProductVO.setRentProductTimes(
        rentProductTimeService.list(
            WrapperProvider.queryWrapper(
                RentProductTime::getRentProductId, rentProductVO.getId())));
    return success(rentProductVO);
  }

  /**
   * 根据id查询租赁商品 用于添加,编辑成功时返回 也可用作编辑时查询租赁商品
   *
   * @param rentProductId
   * @return
   */
  private RentProductVO getRentProductById(Long rentProductId) {
    RentProduct rentProduct = baseService.getById(rentProductId);
    RentProductVO rentProductVO = Transformer.fromBean(rentProduct, RentProductVO.class);
    rentProductVO.setRentProductPics(
        rentProductPicService.list(
            WrapperProvider.queryWrapper(RentProductPic::getRentProductId, rentProductId)));
    rentProductVO.setRentProductTimes(
        rentProductTimeService.list(
            WrapperProvider.queryWrapper(RentProductTime::getRentProductId, rentProductId)));
    return rentProductVO;
  }

  /**
   * 保存或更新该租赁商品规格
   *
   * @param rentProductTimeList 租赁商品规格集合
   * @param rentProductId 租赁商品id
   */
  private void saveRentProductTime(List<RentProductTime> rentProductTimeList, Long rentProductId) {
    if (isNotNull(rentProductTimeList)) {
      // 需要更新的租赁规格
      List<RentProductTime> updateRentProductTimes =
          rentProductTimeList.stream()
              .filter(rentProductTime -> rentProductTime.getId() != null)
              .collect(Collectors.toList());
      // 需要添加的租赁规格
      List<RentProductTime> saveRentProductTimes =
          rentProductTimeList.stream()
              .filter(rentProductTime -> rentProductTime.getId() == null)
              .collect(Collectors.toList());
      if (isNotNull(updateRentProductTimes)) {
        CheckHelper.trueOrThrow(
            rentProductTimeService.updateBatchById(updateRentProductTimes),
            BizException::new,
            "更新租赁商品租期失败,请检查相应参数!");
      }
      // 需要删除的租赁商品的规格
      List<RentProductTime> deleteRentProductTimes =
          rentProductTimeService
              .list(WrapperProvider.queryWrapper(RentProductTime::getRentProductId, rentProductId))
              .stream()
              .filter(
                  rentProductTime ->
                      updateRentProductTimes.stream()
                          .noneMatch(
                              updateRentProductTime ->
                                  updateRentProductTime.getId().equals(rentProductTime.getId())))
              .collect(Collectors.toList());
      if (isNotNull(deleteRentProductTimes)) {
        CheckHelper.trueOrThrow(
            deleteRentProductTimes.stream()
                .allMatch(
                    rentProductTime ->
                        rentProductTimeService.update(
                            WrapperProvider.removeWrapper(
                                RentProductTime::getId, rentProductTime.getId()))),
            BizException::new,
            "删除租赁商品租期失败,请检查相应参数!");
      }
      saveRentProductTimes =
          saveRentProductTimes.stream()
              .peek(rentProductTime -> rentProductTime.setRentProductId(rentProductId))
              .collect(Collectors.toList());
      CheckHelper.trueOrThrow(
          rentProductTimeService.saveBatch(saveRentProductTimes),
          BizException::new,
          "添加租赁商品租期失败,请检查相应参数!");
    }
  }

  /**
   * 保存该租赁商品详情图片
   *
   * @param list 图片地址集合
   * @param rentProductId 租赁商品id
   */
  private void saveRentProductPic(List<String> list, Long rentProductId) {
    CheckHelper.trueOrThrow(
            rentProductPicService.remove(
                    WrapperProvider.queryWrapper(RentProductPic::getRentProductId, rentProductId)),
            BizException::new,
            "删除详情图片失败,请检查相应参数!");
    if (isNotNull(list)) {
      List<RentProductPic> rps =
          list.stream()
              .map(
                  image -> {
                    RentProductPic rp = new RentProductPic();
                    rp.setPicture(image);
                    rp.setRentProductId(rentProductId);
                    return rp;
                  })
              .collect(Collectors.toList());
      CheckHelper.trueOrThrow(
          rentProductPicService.saveBatch(rps), BizException::new, "商品详情图保存失败,请检查相应参数!");
    }
  }

  /**
   * @param rentProductVO
   * @return
   */
  private RentProductVO getRentProductVOList(RentProductVO rentProductVO) {
    List<RentProductTime> rentProductTimeList =
        rentProductTimeService.list(
            WrapperProvider.queryWrapper(RentProductTime::getRentProductId, rentProductVO.getId()));
    RentProductTime rentProductTime =
        rentProductTimeList.stream()
            .min(Comparator.comparing(RentProductTime::getPrice))
            .orElse(new RentProductTime());
    rentProductVO.setPrice(rentProductTime.getPrice());
    return rentProductVO;
  }

  /**
   * 后台查询参数封装 将查询条件拼接
   *
   * @param form
   * @return
   */
  private LambdaQueryWrapper<RentProduct> convertParams(RentProductForm form, String str) {
    LambdaQueryWrapper<RentProduct> queryWrapper = Wrappers.<RentProduct>lambdaQuery();
    if (str.equals("app")) {
      queryWrapper.eq(RentProduct::getOnSale, 1);
    }
    if (isNotEmpty(form.getKeyword())) {
      queryWrapper.like(RentProduct::getName, form.getKeyword());
    }
    if (isNotEmpty(form.getOnSale())) {
      queryWrapper.eq(RentProduct::getOnSale, form.getOnSale());
    }
    if (str.equals("app")) {
      return queryWrapper.orderByDesc(RentProduct::getUpdatedAt);
    }
    return queryWrapper.orderByDesc(RentProduct::getCreatedAt);
  }
}
