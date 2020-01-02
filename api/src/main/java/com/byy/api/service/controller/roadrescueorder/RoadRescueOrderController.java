package com.byy.api.service.controller.roadrescueorder;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.common.utils.helper.CheckHelper.isNotNull;
import static com.byy.dal.common.utils.helper.CheckHelper.trueOrThrow;
import static com.byy.dal.common.utils.provider.UniqueNoProvider.UniqueNoType;
import static com.byy.dal.common.utils.provider.UniqueNoProvider.next;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.roadrescueorder.RoadRescueOrderForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.vo.action.DeletionVO;
import com.byy.api.service.vo.roadrescueorder.RoadRescueOrderVO;
import com.byy.biz.service.roadrescueorder.RoadRescueOrderService;
import com.byy.biz.service.roadrescuepic.RoadRescuePicService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import com.byy.dal.entity.beans.roadrescuepic.RoadRescuePic;
import com.byy.dal.entity.dos.roadrescueorder.RoadRescueOrderDO;
import com.byy.dal.enums.RoadType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: goblin
 * @date: 2019-06-18 15:09:34
 */
@RestController
@RequestMapping("/road/order")
public class RoadRescueOrderController extends CommonController<RoadRescueOrderService> {

  private final RoadRescuePicService roadRescuePicService;

  public RoadRescueOrderController(RoadRescuePicService roadRescuePicService) {
    this.roadRescuePicService = roadRescuePicService;
  }

  /**
   * 提交订单
   *
   * @param form RoadRescueOrderForm
   * @return ResponseObject
   */
  @PostMapping
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<RoadRescueOrderVO> saveRoadRescueOrder(
      @RequestBody RoadRescueOrderForm form) {
    form.setUserId(getCurrentUserId());
    form.setStatus(RoadType.SUBMITTED);
    form.setOrderNo(next(UniqueNoType.RO));
    form.setTotalFee(form.getGoodsFee());
    trueOrThrow(baseService.save(form), BizException::new, "新增道路救援订单失败");
    List<String> images = form.getImages();
    images.stream().peek(image -> {
      RoadRescuePic roadRescuePic = new RoadRescuePic();
      roadRescuePic.setImage(image);
      roadRescuePic.setRoadRescueId(form.getId());
      trueOrThrow(roadRescuePicService.save(roadRescuePic), BizException::new, "新增道路救援故障图失败");
    }).collect(Collectors.toList());
    return success(Transformer.fromBean(form, RoadRescueOrderVO.class));
  }


  /**
   * 后台删除订单，小程序端还可以展示
   *
   * @param id Long
   * @return ResponseObject
   */
  @DeleteMapping("/{id}")
  public ResponseObject<DeletionVO> removeRoadRescueOrder(@PathVariable Long id) {
    RoadRescueOrder roadRescueOrder = new RoadRescueOrder();
    roadRescueOrder.setId(id);
    roadRescueOrder.setShowBack(false);
    roadRescueOrder.setUpdatedAt(LocalDateTime.now());
    return trueOrError(baseService.updateById(roadRescueOrder), DeletionVO.withId(id), "删除失败");
  }

  /**
   * 小程序删除取消订单，前后端都不展示
   *
   * @param id Long
   * @return ResponseObject
   */
  @DeleteMapping("/app/{id}")
  public ResponseObject<DeletionVO> remove(@PathVariable Long id) {
    return trueOrError(
        baseService.update(WrapperProvider.removeWrapper(RoadRescueOrder::getId, id)),
        DeletionVO.withId(id), "删除失败");
  }

  /**
   * 小程序端查看个人道路救援订单
   *
   * @return ResponseObject
   */
  @GetMapping("/app")
  public ResponseObject<ImmutableMap<String, Object>> getRoadRescueOrder(
      @RequestParam(required = false) RoadType roadType,
      IPageForm pageForm) {
    HashMap<String, Object> params = Maps.newHashMap();
    params.put("roadType", roadType);
    params.put("userId", getCurrentUserId());
    IPage<RoadRescueOrderDO> roadRescueToApp = baseService
        .selectRoadRescueToApp(pageForm.newPage(), params);
    return success(ImmutableMap
        .of("list", Transformer.fromList(roadRescueToApp.getRecords(), RoadRescueOrderVO.class),
            "total", roadRescueToApp.getTotal()));
  }

  /**
   * 总/分后台分页条件查询道路救援订单 分后台查询必带storeId
   * @param form
   * @param pageForm
   * @param searchForm
   * @return
   */
  @GetMapping("/back/list")
  public ResponseObject<ImmutableMap<String, Object>> getBackRoadRescueOrder(
      RoadRescueOrderForm form, IPageForm pageForm,
      SearchForm searchForm) {
    IPage<RoadRescueOrderDO> orderDOIPage = baseService
        .selectRoadRescueToBack(pageForm.newPage(), convertParams(form, searchForm));
    return success(ImmutableMap
        .of("list", Transformer.fromList(orderDOIPage.getRecords(), RoadRescueOrderVO.class),
            "total", orderDOIPage.getTotal()));
  }

  /**
   * 后台查看订单详情
   */
  @GetMapping("/{id}")
  public ResponseObject<RoadRescueOrderVO> getOne(@PathVariable("id") Long id) {
    RoadRescueOrderVO roadRescueOrderVO = Transformer
        .fromBean(baseService.selectRoadRescue(id), RoadRescueOrderVO.class);
    roadRescueOrderVO.setImages(roadRescuePicService
        .list(WrapperProvider.queryWrapper(RoadRescuePic::getRoadRescueId, id)));
    return success(roadRescueOrderVO);
  }

  /**
   * 后台接单
   */
  @PutMapping("/processing")
  public ResponseObject<RoadRescueOrderVO> modifyRoadRescueOrderStatus(
      @RequestBody RoadRescueOrderForm form) {
    RoadRescueOrder roadRescueOrder = new RoadRescueOrder();
    roadRescueOrder.setId(form.getId());
    roadRescueOrder.setStatus(RoadType.PROCESSING);
    roadRescueOrder.setUpdatedAt(LocalDateTime.now());
    trueOrThrow(baseService.updateById(roadRescueOrder), BizException::new, "修改订单状态失败");
    return success(Transformer.fromBean(form, RoadRescueOrderVO.class));
  }

  /**
   * 取消订单
   */
  @PutMapping("/cancel")
  public ResponseObject<RoadRescueOrderVO> cancelOrder(@RequestBody RoadRescueOrderForm form) {
    RoadRescueOrder roadRescueOrder = new RoadRescueOrder();
    roadRescueOrder.setId(form.getId());
    roadRescueOrder.setStatus(RoadType.CANCELLED);
    roadRescueOrder.setUpdatedAt(LocalDateTime.now());
    trueOrThrow(baseService.updateById(roadRescueOrder), BizException::new, " 取消订单失败");
    return success(Transformer.fromBean(form, RoadRescueOrderVO.class));
  }

  /**
   * 小程序确认完成订单
   */
  @PutMapping("/success")
  public ResponseObject<RoadRescueOrderVO> finishedRescueOrder(
      @RequestBody RoadRescueOrderForm form) {
    RoadRescueOrder roadRescueOrder = new RoadRescueOrder();
    roadRescueOrder.setId(form.getId());
    roadRescueOrder.setStatus(RoadType.SUCCESS);
    roadRescueOrder.setUpdatedAt(LocalDateTime.now());
    trueOrThrow(baseService.updateById(roadRescueOrder), BizException::new, "修改订单状态失败");
    return success(Transformer.fromBean(form, RoadRescueOrderVO.class));
  }

  /**
   * 道路救援订单列表参数封装
   */
  private Map<String, Object> convertParams(RoadRescueOrderForm form, SearchForm searchForm) {
    Map<String, Object> params = Maps.newHashMap();
    if (isNotNull(form.getOrderNo())) {
      params.put("orderNo", form.getOrderNo());
    }
    if (isNotNull(searchForm.getStartTime())) {
      params.put("startTime", searchForm.of(searchForm.getStartTime()));
    }
    if (isNotNull(searchForm.getEndTime())) {
      params.put("endTime", searchForm.of(searchForm.getEndTime()));
    }
    if (isNotNull(form.getStatus())) {
      params.put("status", form.getStatus());
    }
    if (isNotNull(form.getStoreId())) {
      params.put("storeId", form.getStoreId());
    }
    return params;
  }

}

