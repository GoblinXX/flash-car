package com.byy.api.service.controller.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.order.BackOrderForm;
import com.byy.api.service.form.product.ProductForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.utils.MapParamsUtils;
import com.byy.api.service.utils.PointUtils;
import com.byy.api.service.vo.order.BackOrderVO;
import com.byy.api.service.vo.order.OrderVO;
import com.byy.biz.service.distribution.DistributionCenterService;
import com.byy.biz.service.order.BackOrderService;
import com.byy.biz.service.order.MainOrderService;
import com.byy.biz.service.point.PointConfigService;
import com.byy.biz.service.product.RentProductTimeService;
import com.byy.biz.service.product.SkuService;
import com.byy.biz.service.store.StoreService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.distribution.DistributionCenter;
import com.byy.dal.entity.beans.order.MainOrder;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.beans.point.PointConfig;
import com.byy.dal.entity.beans.product.RentProductTime;
import com.byy.dal.entity.beans.product.Sku;
import com.byy.dal.entity.beans.store.Store;
import com.byy.dal.entity.dos.order.BackOrderDO;
import com.byy.dal.entity.dos.order.OrderDO;
import com.byy.dal.enums.OrderStatus;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;
import static com.byy.api.response.ResponseObject.error;
import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.enums.OrderStatus.*;
import static com.byy.dal.enums.OrderType.*;
import static com.byy.dal.enums.SceneType.NEW_USER_FIRST_SHOPPING;

/** @Author: xcf @Date: 26/06/19 下午 02:20 @Description:后台订单 */
@RestController
@AllArgsConstructor
@RequestMapping("/show/order")
public class BackOrderController extends CommonController<BackOrderService> {

  private MapParamsUtils mapParamsUtils;

  private StoreService storeService;

  private MainOrderService mainOrderService;

  private PointConfigService pointConfigService;

  private SkuService skuService;

  private DistributionCenterService distributionCenterService;

  private PointUtils pointUtils;

  /**
   * 后台查询订单
   *
   * @param form
   * @param searchForm
   * @return
   */
  @GetMapping("/back/page")
  public ResponseObject<ImmutableMap<String, Object>> getOrderSys(
      @Valid BackOrderForm form, SearchForm searchForm) {
    Map<String, Object> params = mapParams(form, searchForm);
    IPage<BackOrderDO> page = baseService.getOrder(form.newPage(), params);
    List<BackOrderVO> backOrderVOS = Transformer.fromList(page.getRecords(), BackOrderVO.class);
    return toPageMap(backOrderVOS, page.getTotal());
  }

  /**
   * 通过id查询订单详情
   *
   * @param orderId
   * @return
   */
  @GetMapping("/back/{orderId}")
  public ResponseObject<BackOrderVO> getOrderById(@PathVariable("orderId") Long orderId) {
    return success(Transformer.fromBean(baseService.getOrderById(orderId), BackOrderVO.class));
  }

  /**
   * 条件获取订单不分页,导出用
   *
   * @param form
   * @param searchForm
   * @return
   */
  @GetMapping("/back/export")
  public ResponseObject<List<BackOrderVO>> getOrderExport(
      @Valid BackOrderForm form, SearchForm searchForm) {
    return success(
        Transformer.fromList(
            baseService.getOrderExport(mapParams(form, searchForm)), BackOrderVO.class));
  }

  /**
   * 后台删除订单
   *
   * @param orderId
   * @return
   */
  @DeleteMapping("/back/{orderId}")
  public ResponseObject<BackOrderVO> modifyOrderShowBack(@PathVariable("orderId") Long orderId) {
    Order order = baseService.getById(orderId);
    order.setShowBack(true);
    CheckHelper.trueOrThrow(baseService.updateById(order), BizException::new, "后台删除失败,请检查相应参数!");
    return success(Transformer.fromBean(order, BackOrderVO.class));
  }

  /**
   * 修改订单状态(发货)
   *
   * @return
   */
  @PutMapping("/back/order/{orderId}")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<BackOrderVO> modifyOrderStatus(@PathVariable("orderId") Long orderId) {
    Order order = baseService.getById(orderId);
    Store store = storeService.getById(order.getStoreId());
    // 判断用户是否是第一次下单
    Long userId = order.getUserId();
    List<OrderStatus> orderStatuses =
        Arrays.asList(SHIPPED, RECEIVED, RETURNING, RETURNED, SUCCESS);
    List<Order> list =
        baseService.list(
            WrapperProvider.queryWrapper(Order::getUserId, userId)
                .in(Order::getStatus, orderStatuses));
    // 如果用户是新用户,将获取积分
    if (list == null || list.size() == 0) {
      BigDecimal pointChange =
          pointConfigService
              .getOne(WrapperProvider.queryWrapper(PointConfig::getScene, NEW_USER_FIRST_SHOPPING))
              .getAmount();
      // 查出该用户上级id,上级获取积分
      Long superiorId =
          distributionCenterService
              .getOne(WrapperProvider.oneQueryWrapper(DistributionCenter::getUserId, userId))
              .getSuperiorId();
      if (superiorId != null) {
        pointUtils.savePointAndRecord(
            superiorId, NEW_USER_FIRST_SHOPPING, pointChange, order.getOrderNo());
      }
    }
    // 修改商品sku销量(租赁商品没有销量字段)
    if (order.getOrderType().equals(HOME) || order.getOrderType().equals(STORE)) {
      Sku sku = skuService.getById(order.getSkuId());
      sku.setSaleAmount(sku.getSaleAmount() + order.getAmount());
      CheckHelper.trueOrThrow(skuService.updateById(sku), BizException::new, "修改商品销量失败,请检查相应参数!");
    }
    store.setCumulativeAmount(store.getCumulativeAmount().add(order.getTotalFee()));
    store.setAvailableAmount(store.getAvailableAmount().add(order.getTotalFee()));
    CheckHelper.trueOrThrow(storeService.updateById(store), BizException::new, "修改门店金额失败,请检查相应参数!");
    return trueOrError(
        baseService.update(
            WrapperProvider.updateWrapper(Order::getId, orderId).set(Order::getStatus, SHIPPED)),
        Transformer.fromBean(order, BackOrderVO.class),
        "修改订单状态失败,请检查相应参数!");
  }

  /**
   * 小程序端删除订单
   *
   * @param orderId
   * @return
   */
  @DeleteMapping("/app/{orderId}")
  public ResponseObject<Object> deleteOrderApp(@PathVariable("orderId") Long orderId) {
    MainOrder mainOrder = mainOrderService.getById(orderId);
    if (mainOrder.getStatus().equals(CANCELLED)) {
      return trueOrError(
          mainOrderService.update(WrapperProvider.removeWrapper(MainOrder::getId, orderId)),
          orderId,
          "订单删除错误,请检查相应参数!");
    } else {
      return error(-1, "订单状态错误,请检查相应参数!");
    }
  }

  /**
   * 后台查询参数封装 将查询条件存入map
   *
   * @param form
   * @return
   */
  private Map<String, Object> mapParams(BackOrderForm form, SearchForm searchForm) {
    Map<String, Object> params = mapParamsUtils.getMapParams(searchForm);
    if (isNotEmpty(form.getOrderType())) {
      params.put("orderType", form.getOrderType());
    }
    if (isNotEmpty(form.getStatus())) {
      params.put("status", form.getStatus());
    }
    if (isNotEmpty(form.getStoreId())) {
      params.put("storeId", form.getStoreId());
    }
    return params;
  }
}
