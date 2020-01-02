package com.byy.api.service.controller.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.order.ConfirmOrderForm;
import com.byy.api.service.form.order.ListOrderForm;
import com.byy.api.service.form.order.SubmitOrderForm;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.vo.address.UserAddressVO;
import com.byy.api.service.vo.order.ConfirmOrderVO;
import com.byy.api.service.vo.order.MainOrderVO;
import com.byy.api.service.vo.order.OrderVO;
import com.byy.biz.service.address.UserAddressService;
import com.byy.biz.service.location.AddressChainService;
import com.byy.biz.service.order.MainOrderService;
import com.byy.biz.service.price.params.PromotionContext;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.address.UserAddress;
import com.byy.dal.entity.beans.order.MainOrder;
import com.byy.dal.entity.dos.order.OrderDO;
import com.byy.dal.enums.OrderStatus;
import com.byy.dal.enums.PromotionType;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.byy.api.response.ResponseObject.success;

/**
 * 订单
 *
 * @author: yyc
 * @date: 19-6-17 下午5:37
 */
@RestController
@RequestMapping("/order")
public class MainOrderController extends CommonController<MainOrderService> {

  private final UserAddressService userAddressService;
  private final AddressChainService addressChainService;

  @Autowired
  public MainOrderController(
      UserAddressService userAddressService, AddressChainService addressChainService) {
    this.userAddressService = userAddressService;
    this.addressChainService = addressChainService;
  }

  /**
   * 确认订单
   *
   * @param form ConfirmOrderForm
   * @return ResponseObject
   */
  @PostMapping("/confirm")
  public ResponseObject<ConfirmOrderVO> confirmOrder(@RequestBody ConfirmOrderForm form) {
    form.setUserId(getCurrentUserId());
    return success(obtainConfirmOrderVO(baseService.confirmOrder(form)));
  }

  /**
   * 提交订单
   *
   * @param form SubmitOrderForm
   * @return MainOrderVO
   */
  @PostMapping("/submit")
  public ResponseObject<MainOrderVO> submitOrder(@RequestBody SubmitOrderForm form) {
    form.setUserId(getCurrentUserId());
    MainOrder mainOrder = baseService.submitOrder(form);
    return success(Transformer.fromBean(mainOrder, MainOrderVO.class));
  }

  /**
   * 取消订单
   *
   * @param orderNo Long
   * @return ResponseObject
   */
  @PostMapping("/cancel/{orderNo}")
  public ResponseObject<MainOrderVO> cancelOrder(@PathVariable String orderNo) {
    MainOrder mainOrder = baseService.cancelOrder(orderNo, getCurrentUserId());
    return success(Transformer.fromBean(mainOrder, MainOrderVO.class));
  }

  /**
   * 查询我的订单列表(包含订单详情)
   *
   * @param pageForm IPageForm
   * @param form ListOrderForm
   * @return ResponseObject
   */
  @GetMapping("/list")
  public ResponseObject<ImmutableMap<String, Object>> getOrders(
      IPageForm pageForm, ListOrderForm form) {
    IPage<OrderDO> page =
        baseService.loadOrders(pageForm.newPage(), convertParams(form, getCurrentUserId()));
    return success(
        ImmutableMap.of(
            "list",
            Transformer.fromList(page.getRecords(), OrderVO.class),
            "total",
            page.getTotal()));
  }

  /**
   * 确认收货
   *
   * @param orderNo String
   * @return ResponseObject
   */
  @PutMapping("/receive/{orderNo}")
  public ResponseObject<OrderVO> receiveOrder(@PathVariable String orderNo) {
    return success(
        Transformer.fromBean(
            baseService.modifyOrderStatus(orderNo, OrderStatus.RECEIVED, getCurrentUserId()),
            OrderVO.class));
  }

  /**
   * 归还订单
   *
   * @param orderNo String
   * @return ResponseObject
   */
  @PutMapping("/return/{orderNo}")
  public ResponseObject<OrderVO> returnOrder(@PathVariable String orderNo) {
    return success(
        Transformer.fromBean(
            baseService.modifyOrderStatus(orderNo, OrderStatus.RETURNING, getCurrentUserId()),
            OrderVO.class));
  }

  /**
   * 订单列表查询参数封装
   *
   * @param form ListOrderForm
   * @param currentUserId Long
   * @return Map
   */
  private Map<String, Object> convertParams(ListOrderForm form, Long currentUserId) {
    Map<String, Object> params = new HashMap<>();
    params.put("status", form.getStatus());
    params.put("orderType", form.getOrderType());
    params.put("userId", currentUserId);
    return params;
  }

  /**
   * 封装确认订单参数
   *
   * @param context PromotionContext
   * @return ConfirmOrderVO
   */
  private ConfirmOrderVO obtainConfirmOrderVO(PromotionContext context) {
    ConfirmOrderVO confirmOrderVO =
        Transformer.fromBean(context, ConfirmOrderVO.class)
            .withCanUsePointDiscount( // 设置可用积分抵扣
                context.getCanUsePoint().divide(context.getRatio(), 1, BigDecimal.ROUND_DOWN))
            .withTotalFee( // 设置订单合计金额
                context
                    .getGoodsFee()
                    .add(context.getServiceFee())
                    .add(context.getInstallFee())
                    .add(context.getDepositFee())
                    .subtract(context.getTotalDiscount()));
    // 设置优惠详情
    context
        .getUsePromotionMap()
        .forEach(
            (k, v) -> {
              BigDecimal promotionDiscount = v.getPromotionDiscount();
              if (k == PromotionType.POINT) {
                confirmOrderVO.withPointDiscount(promotionDiscount);
              } else if (k == PromotionType.COUPON) {
                confirmOrderVO.withCouponDiscount(promotionDiscount);
              }
            });
    return obtainUserAddress(confirmOrderVO, context.getUserId());
  }

  /**
   * 查询订单地址(如果有默认地址使用默认地址)
   *
   * @param confirmOrderVO ConfirmOrderVO
   * @param userId Long
   * @return ConfirmOrderVO
   */
  private ConfirmOrderVO obtainUserAddress(ConfirmOrderVO confirmOrderVO, Long userId) {
    UserAddress userAddress =
        userAddressService.getOne(
            WrapperProvider.oneQueryWrapper(UserAddress::getUserId, userId)
                .eq(UserAddress::getOnDefault, true));
    if (userAddress == null) {
      userAddress =
          userAddressService.getOne(
              WrapperProvider.oneQueryWrapper(UserAddress::getUserId, userId));
    }
    if (userAddress != null) {
      UserAddressVO userAddressVO = Transformer.fromBean(userAddress, UserAddressVO.class);
      userAddressVO.setAddressChain(addressChainService.loadAddressChain(userAddress.getAreaId()));
      confirmOrderVO.withUserAddressVO(userAddressVO);
    }
    return confirmOrderVO;
  }

}
