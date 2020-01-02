package com.byy.api.service.controller.refundorder;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.common.utils.helper.CheckHelper.isNotNull;
import static com.byy.dal.common.utils.helper.CheckHelper.trueOrThrow;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.refundorder.RefundOrderForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.vo.refundorder.RefundOrderVO;
import com.byy.biz.service.order.MainOrderService;
import com.byy.biz.service.order.OrderService;
import com.byy.biz.service.point.UserPointRecordService;
import com.byy.biz.service.point.UserPointService;
import com.byy.biz.service.product.RoadRescueService;
import com.byy.biz.service.product.SkuService;
import com.byy.biz.service.refundorder.RefundOrderService;
import com.byy.biz.service.roadrescueorder.RoadRescueOrderService;
import com.byy.biz.service.store.StoreService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.UniqueNoProvider;
import com.byy.dal.common.utils.provider.UniqueNoProvider.UniqueNoType;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.order.MainOrder;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.beans.point.UserPoint;
import com.byy.dal.entity.beans.point.UserPointRecord;
import com.byy.dal.entity.beans.product.RoadRescue;
import com.byy.dal.entity.beans.product.Sku;
import com.byy.dal.entity.beans.refundorder.RefundOrder;
import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import com.byy.dal.entity.beans.store.Store;
import com.byy.dal.enums.OrderStatus;
import com.byy.dal.enums.OrderType;
import com.byy.dal.enums.RefundStatus;
import com.byy.dal.enums.RoadType;
import com.byy.dal.enums.SceneType;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: goblin
 * @date: 2019-06-24 16:01:09
 */
@RestController
@RequestMapping("/refund")
public class RefundOrderController extends CommonController<RefundOrderService> {

  private final OrderService orderService;
  private final RoadRescueOrderService roadRescueOrderService;
  private final StoreService storeService;
  private final RoadRescueService roadRescueService;
  private final MainOrderService mainOrderService;
  private final SkuService skuService;
  private final UserPointService userPointService;
  private final UserPointRecordService userPointRecordService;

  public RefundOrderController(OrderService orderService,
      RoadRescueOrderService roadRescueOrderService,
      StoreService storeService, RoadRescueService roadRescueService,
      MainOrderService mainOrderService, SkuService skuService,
      UserPointService userPointService,
      UserPointRecordService userPointRecordService) {
    this.orderService = orderService;
    this.roadRescueOrderService = roadRescueOrderService;
    this.storeService = storeService;
    this.roadRescueService = roadRescueService;
    this.mainOrderService = mainOrderService;
    this.skuService = skuService;
    this.userPointService = userPointService;
    this.userPointRecordService = userPointRecordService;
  }

  /**
   * 小程序端新增退款订单 同时要修关联订单状态
   *
   * @param form RefundOrderForm
   * @return ResponseObject
   */
  @PostMapping
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<RefundOrderVO> saveRefundOrder(@RequestBody RefundOrderForm form) {
    form.setUserId(getCurrentUserId());
    form.setOutRefundNo(UniqueNoProvider.next(UniqueNoType.R));
    if (form.getOrderType().equals(OrderType.ROAD_RESCUE)) {
      trueOrThrow(
          roadRescueOrderService.update(WrapperProvider.updateWrapper(RoadRescueOrder::getOrderNo,
              form.getOrderNo()).set(RoadRescueOrder::getStatus, RoadType.REFUNDING)),
          BizException::new, "更新道路救援订单状态失败");
      RoadRescueOrder rescueOrder = roadRescueOrderService
          .getOne(WrapperProvider.oneQueryWrapper(RoadRescueOrder::getOrderNo, form.getOrderNo()));
      form.setGoodsFee(rescueOrder.getGoodsFee());
      form.setMainOrderNo(rescueOrder.getOrderNo());
    } else {
      trueOrThrow(orderService.update(
          WrapperProvider.updateWrapper(Order::getOrderNo, form.getOrderNo())
              .set(Order::getStatus,
                  OrderStatus.REFUNDING)), BizException::new, "更新订单状态失败");
      Order order = orderService
          .getOne(WrapperProvider.oneQueryWrapper(Order::getOrderNo, form.getOrderNo()));
      MainOrder mainOrder = mainOrderService
          .getById(order.getMainOrderId());
      form.setGoodsFee(mainOrder.getTotalFee());
      form.setMainOrderNo(mainOrder.getMainOrderNo());
    }
    ResponseObject<RefundOrderVO> refundOrderVO = trueOrError(baseService.save(form),
        Transformer.fromBean(form, RefundOrderVO.class),
        "保存失败");
    return refundOrderVO;
  }

  /**
   * 小程序端分页条件查询个人退款订单
   *
   * @return ResponseObject
   */
  @GetMapping
  public ResponseObject<ImmutableMap<String, Object>> getRefundOrder(RefundOrderForm form,
      IPageForm pageForm) {
    IPage<RefundOrder> refundOrderIPage = baseService
        .page(pageForm.newPage(),
            WrapperProvider.queryWrapper(RefundOrder::getUserId, getCurrentUserId())
                .eq(form.getStatus() != null, RefundOrder::getStatus, form.getStatus()));
    List<RefundOrderVO> refundOrderVOS = Transformer.fromList(refundOrderIPage.getRecords(),
        RefundOrderVO.class);
    refundOrderVOS.stream().peek(refundOrderVO -> {
      if (refundOrderVO.getOrderType().equals(OrderType.ROAD_RESCUE)) {
        RoadRescueOrder roadRescueOrder = roadRescueOrderService.getOne(
            WrapperProvider
                .oneQueryWrapper(RoadRescueOrder::getOrderNo, refundOrderVO.getOrderNo()));
        if (isNotNull(roadRescueOrder)) {
          refundOrderVO.setBuyerName(roadRescueOrder.getBuyerName());
          refundOrderVO.setBuyerPhone(roadRescueOrder.getBuyerPhone());
          refundOrderVO.setTotal(1);
          Long storeId = roadRescueOrder.getStoreId();
          Store store = storeService.getById(storeId);
          if (isNotNull(store)) {
            refundOrderVO.setStoreName(store.getName());
            refundOrderVO.setStoreImage(store.getImage());
          }
          Long roadRescueId = roadRescueOrder.getRoadRescueId();
          RoadRescue roadRescue = roadRescueService.getById(roadRescueId);
          if (isNotNull(roadRescue)) {
            refundOrderVO.setProductName(roadRescue.getName());
            refundOrderVO.setProductImage(roadRescue.getPicture());
          }
        }
      } else {
        Order order = orderService
            .getOne(WrapperProvider.oneQueryWrapper(Order::getOrderNo, refundOrderVO.getOrderNo()));
        if (isNotNull(order)) {
          setRefundVOInfo(refundOrderVO, order);
        }
      }
    }).collect(Collectors.toList());
    return success(ImmutableMap.of("list", refundOrderVOS, "total", refundOrderIPage.getTotal()));
  }

  private void setRefundVOInfo(RefundOrderVO refundOrderVO, Order order) {
    refundOrderVO.setBuyerName(order.getBuyerName());
    refundOrderVO.setBuyerPhone(order.getBuyerPhone());
    refundOrderVO.setProductName(order.getProductName());
    refundOrderVO.setProductImage(order.getProductPic());
    refundOrderVO.setAddress(order.getAddress());
    refundOrderVO.setTotal(order.getAmount());
    refundOrderVO.setSkuName(order.getSkuName());
    Long storeId = order.getStoreId();
    Store store = storeService.getById(storeId);
    if (isNotNull(store)) {
      refundOrderVO.setStoreName(store.getName());
      refundOrderVO.setStoreImage(store.getImage());
    }
  }

  /***
   * 后台端分页条件查询个人退款订单
   * @param form
   * @param pageForm
   * @param searchForm
   * @return
   */
  @GetMapping("/back")
  public ResponseObject<ImmutableMap<String, Object>> getRefundOrder(RefundOrderForm form,
      IPageForm pageForm, SearchForm searchForm) {
    IPage<RefundOrder> page = baseService.page(pageForm.newPage(), convertParams(form, searchForm));
    List<RefundOrderVO> refundOrderVOS = Transformer
        .fromList(page.getRecords(), RefundOrderVO.class);
    refundOrderVOS.forEach(refundOrderVO -> {
      if (refundOrderVO.getOrderType().equals(OrderType.ROAD_RESCUE)) {
        RoadRescueOrder roadRescueOrder = roadRescueOrderService.getOne(
            WrapperProvider
                .oneQueryWrapper(RoadRescueOrder::getOrderNo, refundOrderVO.getOrderNo()));
        if (isNotNull(roadRescueOrder)) {
          refundOrderVO.setBuyerName(roadRescueOrder.getBuyerName());
          refundOrderVO.setBuyerPhone(roadRescueOrder.getBuyerPhone());
          Store store = storeService.getById(refundOrderVO.getStoreId());
          if (isNotNull(store)) {
            refundOrderVO.setStoreName(store.getName());
            refundOrderVO.setStoreImage(store.getImage());
          }
          Long roadRescueId = roadRescueOrder.getRoadRescueId();
          RoadRescue roadRescue = roadRescueService.getById(roadRescueId);
          if (isNotNull(roadRescue)) {
            refundOrderVO.setProductName(roadRescue.getName());
            refundOrderVO.setProductImage(roadRescue.getPicture());
          }
        }
      } else {
        Order order = orderService
            .getOne(WrapperProvider.oneQueryWrapper(Order::getOrderNo, refundOrderVO.getOrderNo()));
        if (isNotNull(order)) {
          setRefundVOInfo(refundOrderVO, order);
        }
      }
    });
    return success(ImmutableMap.of("list", refundOrderVOS, "total", page.getTotal()));
  }

  /**
   * 取消退款
   */
  @PutMapping("/cancel")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<RefundOrderVO> cancelRefundOrder(@RequestBody RefundOrderForm form) {
    //恢复订单为代发货状态
    RefundOrder refundOrder = baseService.getById(form.getId());
    String orderNo = refundOrder.getOrderNo();
    if (refundOrder.getOrderType().equals(OrderType.ROAD_RESCUE)) {
      trueOrThrow(roadRescueOrderService
          .update(WrapperProvider.updateWrapper(RoadRescueOrder::getOrderNo, orderNo)
              .set(RoadRescueOrder::getStatus, RoadType.PAID)), BizException::new, "更新订单状态失败");
    } else {
      trueOrThrow(orderService
          .update(WrapperProvider.updateWrapper(Order::getOrderNo, orderNo)
              .set(Order::getStatus, OrderStatus.PAID)), BizException::new, "更新订单状态失败");
    }
    trueOrThrow(baseService.update(WrapperProvider.removeWrapper(RefundOrder::getId, form.getId())),
        BizException::new, "取消退款失败");
    return success(Transformer.fromBean(form, RefundOrderVO.class));
  }

  /**
   * 修改订单状态-- 同时要回归商品库存、生成退还积分记录、退还用户积分
   *
   * @param form RefundOrderForm
   * @return ResponseObject
   */
  @PutMapping
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<RefundOrderVO> modifyRefundOrder(@RequestBody RefundOrderForm form) {
    RefundOrder refundOrder = baseService.getById(form.getId());
    if (form.getStatus().equals(RefundStatus.SUCCESS)) {
      if (!refundOrder.getOrderType().equals(OrderType.ROAD_RESCUE)) {
        Order order = orderService
            .getOne(WrapperProvider.oneQueryWrapper(Order::getOrderNo, refundOrder.getOrderNo()));
        //回归商品库存
        Integer amount = order.getAmount();
        Sku productSku = skuService.getById(order.getSkuId());
        if (isNotNull(productSku)) {
          int backAmount = productSku.getAmount() + amount;
          productSku.setAmount(backAmount);
          trueOrThrow(skuService.updateById(productSku), BizException::new, "回归商品库存失败！");
        }

        BigDecimal usePoint = order.getUsePoint();
        UserPoint user = userPointService
            .getOne(WrapperProvider.oneQueryWrapper(UserPoint::getUserId, order.getUserId()));
        if (usePoint.compareTo(BigDecimal.ZERO) == 1) {
          //生成退还积分记录
          UserPointRecord userPointRecord = new UserPointRecord();
          userPointRecord.setScene(SceneType.REFUND);
          userPointRecord.setOrderNo(order.getOrderNo());
          userPointRecord.setRecord(usePoint);
          userPointRecord.setUserId(order.getUserId());
          userPointRecord.setPreviousPoint(user.getAvailablePoint());
          userPointRecord.setCurrentPoint(user.getAvailablePoint().add(usePoint));
          trueOrThrow(userPointRecordService.save(userPointRecord), BizException::new,
              "生成积分退还记录失败！");
        }
        //退还用户积分
        user.setAvailablePoint(usePoint.add(user.getAvailablePoint()));
        trueOrThrow(userPointService.updateById(user), BizException::new, "积分退还失败！");
      }
    } else {
      if (refundOrder.getOrderType().equals(OrderType.ROAD_RESCUE)) {
        trueOrThrow(roadRescueOrderService
                .update(
                    WrapperProvider.updateWrapper(RoadRescueOrder::getOrderNo, refundOrder.getOrderNo())
                        .set(RoadRescueOrder::getStatus, RoadType.PAID)), BizException::new,
            "更新订单状态失败");
      } else {
        trueOrThrow(orderService
            .update(WrapperProvider.updateWrapper(Order::getOrderNo, refundOrder.getOrderNo())
                .set(Order::getStatus, OrderStatus.PAID)), BizException::new, "更新订单状态失败");
      }
    }
    return trueOrError(
        baseService.updateById(form), Transformer.fromBean(form, RefundOrderVO.class), "修改失败");
  }

  /**
   * 退款参数封装
   */
  private Wrapper<RefundOrder> convertParams(RefundOrderForm form, SearchForm searchForm) {
    LambdaQueryWrapper<RefundOrder> queryWrapper =
        Wrappers.<RefundOrder>lambdaQuery().orderByDesc(RefundOrder::getCreatedAt);
    if (isNotNull(form.getOrderNo())) {
      queryWrapper.like(RefundOrder::getOrderNo, form.getOrderNo());
    }
    if (isNotNull(searchForm.getStartTime())) {
      queryWrapper.ge(RefundOrder::getOrderAt, searchForm.of(searchForm.getStartTime()));
    }
    if (isNotNull(searchForm.getEndTime())) {
      queryWrapper.le(RefundOrder::getOrderAt, searchForm.of(searchForm.getEndTime()));
    }
    if (isNotNull(form.getStatus())) {
      queryWrapper.eq(RefundOrder::getStatus, form.getStatus());
    }
    if (isNotNull(form.getOrderType())) {
      queryWrapper.eq(RefundOrder::getOrderType, form.getOrderType());
    }
    if (isNotNull(form.getStoreId())) {
      queryWrapper.eq(RefundOrder::getStoreId, form.getStoreId());
    }
    return queryWrapper;
  }
}

