package com.byy.biz.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.cartitem.CartItemService;
import com.byy.biz.service.coupon.UserCouponService;
import com.byy.biz.service.order.MainOrderService;
import com.byy.biz.service.order.OrderService;
import com.byy.biz.service.order.param.OrderParam;
import com.byy.biz.service.order.param.SubmitOrderParam;
import com.byy.biz.service.point.UserPointService;
import com.byy.biz.service.price.cart.SkuItem;
import com.byy.biz.service.price.chain.AbstractPromotionPriceChain;
import com.byy.biz.service.price.params.PromotionContext;
import com.byy.biz.service.price.promotion.CouponPromotion;
import com.byy.biz.service.price.promotion.PointPromotion;
import com.byy.biz.service.price.promotion.Promotion;
import com.byy.biz.service.product.ProductService;
import com.byy.biz.service.product.RentProductService;
import com.byy.biz.service.product.RentProductTimeService;
import com.byy.biz.service.product.SkuService;
import com.byy.biz.service.sys.SysUserService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.OrderHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.UniqueNoProvider;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.cartitem.CartItem;
import com.byy.dal.entity.beans.coupon.UserCoupon;
import com.byy.dal.entity.beans.order.MainOrder;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.beans.order.OrderSku;
import com.byy.dal.entity.beans.point.UserPoint;
import com.byy.dal.entity.beans.product.Product;
import com.byy.dal.entity.beans.product.RentProduct;
import com.byy.dal.entity.beans.product.RentProductTime;
import com.byy.dal.entity.beans.product.Sku;
import com.byy.dal.entity.beans.sys.SysUser;
import com.byy.dal.entity.dos.order.OrderDO;
import com.byy.dal.enums.OrderDimension;
import com.byy.dal.enums.OrderStatus;
import com.byy.dal.enums.OrderType;
import com.byy.dal.enums.PromotionType;
import com.byy.dal.mapper.order.MainOrderMapper;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: yyc
 * @date: 2019-06-20 10:39:12
 */
@Slf4j
@Service
public class MainOrderServiceImpl extends ServiceImpl<MainOrderMapper, MainOrder>
    implements MainOrderService {

  @Value("${sms.appId}")
  private int appId;

  @Value("${sms.appKey}")
  private String appKey;

  @Value("${sms.templateId1}")
  private int templateId1;

  @Value("${sms.templateId2}")
  private int templateId2;

  @Value("${sms.templateId3}")
  private int templateId3;

  @Value("${sms.smsSign}")
  private String smsSign;


  private final AbstractPromotionPriceChain promotionPriceChain;
  private final SkuService skuService;
  private final ProductService productService;
  private final RentProductService rentProductService;
  private final RentProductTimeService rentProductTimeService;
  private final OrderService orderService;
  private final CartItemService cartItemService;
  private final UserPointService userPointService;
  private final UserCouponService userCouponService;
  private final SysUserService sysUserService;

  @Autowired
  public MainOrderServiceImpl(
      AbstractPromotionPriceChain promotionPriceChain,
      SkuService skuService,
      ProductService productService,
      RentProductService rentProductService,
      RentProductTimeService rentProductTimeService,
      OrderService orderService,
      CartItemService cartItemService,
      UserPointService userPointService,
      UserCouponService userCouponService, SysUserService sysUserService) {
    this.promotionPriceChain = promotionPriceChain;
    this.skuService = skuService;
    this.productService = productService;
    this.rentProductService = rentProductService;
    this.rentProductTimeService = rentProductTimeService;
    this.orderService = orderService;
    this.cartItemService = cartItemService;
    this.userPointService = userPointService;
    this.userCouponService = userCouponService;
    this.sysUserService = sysUserService;
  }

  @Override
  public PromotionContext confirmOrder(OrderParam param) {
    // 初始化优惠计算上下文参数
    PromotionContext context;
    if (param.getOrderType() == OrderType.RENT) {
      context = initRentPromotionContext(param);
    } else {
      context = initPromotionContext(param);
    }
    // 积分优惠券等优惠计算
    promotionPriceChain.calculate(context);
    return context;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public MainOrder submitOrder(SubmitOrderParam submitOrderParam) {
    // 调用优惠计算
    PromotionContext promotionContext = confirmOrder(submitOrderParam);
    // 清空购物车
    clearCartItems(submitOrderParam, promotionContext.getUserId());
    // 保存订单
    return saveMainOrder(promotionContext, submitOrderParam);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public MainOrder cancelOrder(String orderNo, Long userId) {
    // 必须为主订单号才可以取消
    CheckHelper.trueOrThrow(OrderHelper.isMainOrderNo(orderNo), BizException::new, "订单不符合取消条件");
    MainOrder mainOrder =
        getOne(WrapperProvider.oneQueryWrapper(MainOrder::getMainOrderNo, orderNo));
    // 检查工单流是否正确
    OrderHelper.checkFlow(mainOrder.getStatus(), OrderStatus.CANCELLED, mainOrder.getOrderType());
    CheckHelper.trueOrThrow(userId.equals(mainOrder.getUserId()), BizException::new, "当前用户无法取消订单");
    // 更改订单状态为取消
    List<SkuItem> skuItems =
        orderService.list(WrapperProvider.oneQueryWrapper(Order::getMainOrderId, mainOrder.getId()))
            .stream()
            .map(
                order -> {
                  SkuItem skuItem = new SkuItem();
                  skuItem.setSkuId(order.getSkuId());
                  skuItem.setAmount(BigDecimal.valueOf(order.getAmount()));
                  return skuItem;
                })
            .collect(Collectors.toList());
    // 修改订单状态为已取消
    resetOrderStatus(mainOrder.getId(), OrderStatus.CANCELLED);
    // 归还库存
    resetSkuAmount(skuItems, mainOrder.getOrderType(), true);
    // 归还积分或优惠券
    resetUserPointOrCoupon(mainOrder, true);
    mainOrder.setStatus(OrderStatus.CANCELLED);
    return mainOrder;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void asyncPayBack(String orderNo) {
    MainOrder mainOrder =
        getOne(
            WrapperProvider.oneQueryWrapper(MainOrder::getMainOrderNo, orderNo)
                .select(
                    MainOrder::getId,
                    MainOrder::getOrderType,
                    MainOrder::getStatus,
                    MainOrder::getRentAgain));
    if (OrderStatus.SUBMITTED.equals(mainOrder.getStatus())) {
      // 修改订单状态为已付款
      resetOrderStatus(mainOrder.getId(), OrderStatus.PAID);
    }
    // 如果是续租订单
    if (mainOrder.getOrderType() == OrderType.RENT && mainOrder.getRentAgain()) {
      updateOriginOrder(mainOrder.getId());
    }
  }

  @Override
  public void sendSms(String orderNo) {
    MainOrder mainOrder =
        getOne(
            WrapperProvider.oneQueryWrapper(MainOrder::getMainOrderNo, orderNo));
    OrderType orderType = mainOrder.getOrderType();
    List<SysUser> sysUsers = sysUserService
        .list(WrapperProvider.queryWrapper(SysUser::getStoreId, mainOrder.getStoreId()));
    String[] phoneNumbers = new String[sysUsers.size()];
    List<String> phones = sysUsers.stream().map(sysUser -> {
      String username = sysUser.getUsername();
      return username;
    }).collect(Collectors.toList());
    phoneNumbers = phones.toArray(phoneNumbers);
    if (OrderStatus.SUBMITTED.equals(mainOrder.getStatus())) {
      if (OrderStatus.SUBMITTED.equals(mainOrder.getStatus())) {
        if (OrderType.RENT.equals(orderType)) {
          senderResult(templateId2, phoneNumbers);
        }
        if (OrderType.HOME.equals(orderType)) {
          senderResult(templateId3, phoneNumbers);
        }
        if (OrderType.STORE.equals(orderType)) {
          senderResult(templateId1, phoneNumbers);
        }
      }
    }
  }


  /***
   * 指定模板ID单发短信
   * @param phoneNumbers
   * @return
   */
  public SmsMultiSenderResult senderResult(int templateId, String[] phoneNumbers) {
    SmsMultiSenderResult result = null;
    String[] params = new String[]{};
    try {
      SmsMultiSender msender = new SmsMultiSender(appId, appKey);
      result = msender.sendWithParam("86", phoneNumbers,
          // 签名参数未提供或者为空时，会使用默认签名发送短信
          templateId, params, smsSign, "", "");
      System.out.print(result);
    } catch (HTTPException e) {
      // HTTP响应码错误
      e.printStackTrace();
    } catch (JSONException e) {
      // json解析错误
      e.printStackTrace();
    } catch (IOException e) {
      // 网络IO错误
      e.printStackTrace();
    }
    return result;
  }


  /**
   * 更新原订单状态和当前订单的押金
   *
   * @param mainOrderId Long
   */
  private void updateOriginOrder(Long mainOrderId) {
    // 根据主订单id找到当前的子订单
    Order order =
        orderService.getOne(
            WrapperProvider.oneQueryWrapper(Order::getMainOrderId, mainOrderId)
                .select(Order::getId, Order::getOriginOrderId));
    // 根据当前子单续租的原订单id找到原订单
    Order originOrder =
        CheckHelper.nonEmptyOrThrow(
            orderService.getOne(
                WrapperProvider.oneQueryWrapper(Order::getId, order.getOriginOrderId())),
            BizException::new,
            "原租赁订单不存在");
    // 更新原租赁订单的状态为待评价(已归还)
    boolean updateOriginOrder =
        orderService.update(
            WrapperProvider.updateWrapper(Order::getId, order.getOriginOrderId())
                .set(Order::getStatus, OrderStatus.RETURNED));
    // 从原租赁订单上转移押金到当前订单
    boolean updateOrder =
        orderService.update(
            WrapperProvider.updateWrapper(Order::getId, order.getId())
                .set(Order::getDepositFee, originOrder.getDepositFee()));
    CheckHelper.trueOrThrow(updateOriginOrder && updateOrder, BizException::new, "更新订单失败");
  }

  @Override
  public IPage<OrderDO> loadOrders(IPage<OrderDO> page, Map<String, Object> params) {
    IPage<OrderDO> iPage = baseMapper.selectPageByParams(page, params);
    iPage
        .getRecords()
        .forEach(
            orderDO -> {
              if (orderDO.getOrderDimension() == OrderDimension.ORDER) {
                OrderSku orderSku = Transformer.fromBean(orderDO, OrderSku.class);
                orderDO.getOrderSkus().add(orderSku);
              } else if (orderDO.getOrderDimension() == OrderDimension.MAIN_ORDER) {
                orderDO.setOrderSkus(
                    baseMapper.selectListByParams(ImmutableMap.of("mainOrderId", orderDO.getId())));
              }
            });
    return iPage;
  }

  @Override
  public Order modifyOrderStatus(String orderNo, OrderStatus newStatus, Long userId) {
    Order order = orderService.getOne(WrapperProvider.oneQueryWrapper(Order::getOrderNo, orderNo));
    CheckHelper.trueOrThrow(userId.equals(order.getUserId()), BizException::new, "当前用户无法操作订单");
    // 订单流确认
    OrderHelper.checkFlow(order.getStatus(), newStatus, order.getOrderType());
    CheckHelper.trueOrThrow(
        orderService.update(
            WrapperProvider.updateWrapper(Order::getId, order.getId())
                .set(Order::getStatus, newStatus)),
        BizException::new,
        "订单状态更新失败");
    order.setStatus(newStatus);
    return order;
  }

  /**
   * 初始化上门和到店参数
   *
   * @param orderParam OrderParam
   * @return PromotionContext
   */
  private PromotionContext initPromotionContext(OrderParam orderParam) {
    BigDecimal goodsFee = BigDecimal.ZERO;
    BigDecimal costFee = BigDecimal.ZERO;
    BigDecimal serviceFee = BigDecimal.ZERO;
    BigDecimal installFee = BigDecimal.ZERO;
    OrderType orderType = orderParam.getOrderType();
    List<SkuItem> skuItems = orderParam.getSkuItems();
    for (SkuItem skuItem : skuItems) {
      Sku sku =
          CheckHelper.nonEmptyOrThrow(
              skuService.getById(skuItem.getSkuId()), BizException::new, "商品规格不存在");
      BigDecimal itemAmount = skuItem.getAmount();
      if (itemAmount.compareTo(BigDecimal.valueOf(sku.getAmount())) > 0) {
        throw new BizException(sku.getName() + "库存不足");
      }
      BigDecimal itemTotalPrice = sku.getSalePrice().multiply(itemAmount);
      Product product =
          productService.getOne(
              WrapperProvider.oneQueryWrapper(Product::getId, sku.getProductId())
                  .eq(Product::getOnSale, true)
                  .select(
                      Product::getServiceFee,
                      Product::getInstallFee,
                      Product::getName,
                      Product::getImage));
      CheckHelper.trueOrThrow(product != null, BizException::new, "商品不存在");
      // 累加商品总价
      goodsFee = goodsFee.add(itemTotalPrice);
      // 累加商品成本价
      costFee = costFee.add(sku.getCostPrice().multiply(itemAmount));
      // 累加商品安装费
      installFee = installFee.add(product.getInstallFee().multiply(itemAmount));
      // 累加商品服务费
      serviceFee = serviceFee.add(product.getServiceFee().multiply(itemAmount));
      skuItem
          .withPrice(sku.getSalePrice())
          .withCostPrice(sku.getCostPrice())
          .withItemTotalPrice(itemTotalPrice)
          .withSkuName(sku.getName())
          .withProductName(product.getName())
          .withProductPic(product.getImage());
      if (orderType == OrderType.HOME) {
        skuItem.withServiceFee(product.getServiceFee().multiply(itemAmount));
      } else if (orderType == OrderType.STORE) {
        skuItem.withInstallFee(product.getInstallFee().multiply(itemAmount));
      }
    }
    PromotionContext context =
        new PromotionContext()
            .withSkuItems(skuItems)
            .withGoodsFee(goodsFee)
            .withCostFee(costFee)
            .withUserId(orderParam.getUserId())
            .withSelectPromotionMap(obtainSelectPromotionMap(orderParam));
    if (orderType == OrderType.HOME) {
      context.withServiceFee(serviceFee);
    } else if (orderType == OrderType.STORE) {
      context.withInstallFee(installFee);
    }
    return context;
  }

  /**
   * 初始化租赁下单参数
   *
   * @param orderParam OrderParam
   * @return PromotionContext
   */
  private PromotionContext initRentPromotionContext(OrderParam orderParam) {
    BigDecimal goodsFee = BigDecimal.ZERO;
    BigDecimal costFee = BigDecimal.ZERO;
    BigDecimal depositFee = BigDecimal.ZERO;
    // 商品是否续租
    Boolean rentAgain = orderParam.getRentAgain();
    if (rentAgain) {
      // 续租时检查是否已经续租过
      Order one =
          orderService.getOne(
              WrapperProvider.oneQueryWrapper(
                  Order::getOriginOrderId, orderParam.getOriginOrderId())
                  .eq(Order::getStatus, OrderStatus.SUBMITTED)
                  .select(Order::getId));
      CheckHelper.trueOrThrow(one == null, BizException::new, "请勿重复续租");
    }
    List<SkuItem> skuItems = orderParam.getSkuItems();
    for (SkuItem skuItem : skuItems) {
      RentProductTime rentProductTime =
          CheckHelper.nonEmptyOrThrow(
              rentProductTimeService.getById(skuItem.getSkuId()), BizException::new, "商品租期不存在");
      RentProduct rentProduct =
          rentProductService.getOne(
              WrapperProvider.oneQueryWrapper(
                  RentProduct::getId, rentProductTime.getRentProductId())
                  .eq(RentProduct::getOnSale, true));
      CheckHelper.trueOrThrow(rentProduct != null, BizException::new, "商品不存在");
      BigDecimal amount = skuItem.getAmount();
      if (amount.compareTo(BigDecimal.valueOf(rentProduct.getAmount())) > 0) {
        throw new BizException(rentProduct.getName() + "库存不足");
      }
      // 数量累计租期
      BigDecimal tenancy = BigDecimal.valueOf(rentProductTime.getTenancy());
      BigDecimal itemPrice = rentProductTime.getPrice().multiply(tenancy);
      BigDecimal itemCostPrice = rentProduct.getCostPrice().multiply(tenancy);
      // 累加商品总价
      goodsFee = goodsFee.add(itemPrice.multiply(amount));
      // 累加商品成本价
      costFee = costFee.add(itemCostPrice.multiply(amount));
      // 商品是否为续租，续租不交押金
      if (rentAgain) {
        rentProduct.setDeposit(BigDecimal.ZERO);
      }
      // 累加商品押金
      depositFee = depositFee.add(rentProduct.getDeposit().multiply(amount));
      skuItem
          .withPrice(itemPrice)
          .withCostPrice(itemCostPrice)
          .withItemTotalPrice(itemPrice.multiply(amount))
          .withDepositFee(rentProduct.getDeposit().multiply(amount))
          .withSkuName(String.valueOf(rentProductTime.getTenancy()))
          .withProductName(rentProduct.getName())
          .withProductPic(rentProduct.getImage());
    }
    return new PromotionContext()
        .withSkuItems(skuItems)
        .withGoodsFee(goodsFee)
        .withCostFee(costFee)
        .withDepositFee(depositFee)
        .withUserId(orderParam.getUserId())
        .withSelectPromotionMap(obtainSelectPromotionMap(orderParam));
  }

  /**
   * 修改订单状态
   *
   * @param mainOrderId Long
   * @param status OrderStatus
   */
  private void resetOrderStatus(Long mainOrderId, OrderStatus status) {
    boolean isUpdateMainOrder =
        update(
            WrapperProvider.updateWrapper(MainOrder::getId, mainOrderId)
                .set(MainOrder::getStatus, status)
                .set(status == OrderStatus.PAID, MainOrder::getPaidAt, LocalDateTime.now()));
    boolean isUpdateOrder =
        orderService.update(
            WrapperProvider.updateWrapper(Order::getMainOrderId, mainOrderId)
                .set(Order::getStatus, status)
                .set(status == OrderStatus.PAID, Order::getPaidAt, LocalDateTime.now()));
    CheckHelper.trueOrThrow(isUpdateMainOrder && isUpdateOrder, BizException::new, "订单状态更新失败");
  }

  /**
   * 获取用户选择的优惠
   *
   * @param orderParam OrderParam
   * @return Map
   */
  private Map<PromotionType, Promotion> obtainSelectPromotionMap(OrderParam orderParam) {
    Map<PromotionType, Promotion> selectPromotionMap = Maps.newHashMap();
    Long couponId = orderParam.getCouponId();
    BigDecimal usePoint = orderParam.getUsePoint();
    if (couponId != null) {
      selectPromotionMap.put(PromotionType.COUPON, new CouponPromotion().withPromotionId(couponId));
    }
    if (usePoint != null && usePoint.compareTo(BigDecimal.ZERO) > 0) {
      selectPromotionMap.put(PromotionType.POINT, new PointPromotion().withPoint(usePoint));
    }
    return selectPromotionMap;
  }

  /**
   * 保存主订单
   *
   * @param context PromotionContext
   * @param param SubmitOrderParam
   */
  private MainOrder saveMainOrder(PromotionContext context, SubmitOrderParam param) {
    MainOrder mainOrder = Transformer.fromBean(param, MainOrder.class);
    mainOrder.setMainOrderNo(UniqueNoProvider.next(UniqueNoProvider.UniqueNoType.ES));
    mainOrder.setStatus(OrderStatus.SUBMITTED);
    mainOrder.setCreatedAt(LocalDateTime.now());
    // 保存单之前设置主订单价格和优惠详情
    context
        .getUsePromotionMap()
        .forEach(
            (k, v) -> {
              if (k == PromotionType.COUPON) {
                CouponPromotion couponPromotion = (CouponPromotion) v;
                mainOrder.setCouponId(couponPromotion.getPromotionId());
                mainOrder.setCouponDiscount(couponPromotion.getPromotionDiscount());
              } else if (k == PromotionType.POINT) {
                PointPromotion pointPromotion = (PointPromotion) v;
                mainOrder.setUsePoint(pointPromotion.getPoint());
                mainOrder.setPointDiscount(pointPromotion.getPromotionDiscount());
              }
            });
    mainOrder.setGoodsFee(context.getGoodsFee());
    mainOrder.setTotalFee(
        context
            .getGoodsFee()
            .add(context.getServiceFee())
            .add(context.getInstallFee())
            .add(context.getDepositFee())
            .subtract(context.getTotalDiscount()));
    mainOrder.setServiceFee(context.getServiceFee());
    mainOrder.setInstallFee(context.getInstallFee());
    mainOrder.setDepositFee(context.getDepositFee());
    // 保存主单
    CheckHelper.trueOrThrow(save(mainOrder), BizException::new, "保存主订单失败");
    // 更新用户积分和优惠券
    resetUserPointOrCoupon(mainOrder, false);
    // 订单拆单
    separateOrder(mainOrder, context.getSkuItems(), context.getRatio(), param);
    return mainOrder;
  }

  /**
   * 更新用户积分或者优惠券
   *
   * @param mainOrder MainOrder
   * @param increased boolean 优惠券/积分增加还是减少
   */
  private void resetUserPointOrCoupon(MainOrder mainOrder, boolean increased) {
    // 扣减用户积分
    if (mainOrder.getUsePoint() != null && mainOrder.getUsePoint().compareTo(BigDecimal.ZERO) > 0) {
      UserPoint userPoint =
          userPointService.getOne(
              WrapperProvider.oneQueryWrapper(UserPoint::getUserId, mainOrder.getUserId()));
      BigDecimal restPoint =
          increased
              ? userPoint.getAvailablePoint().add(mainOrder.getUsePoint())
              : userPoint.getAvailablePoint().subtract(mainOrder.getUsePoint());
      CheckHelper.trueOrThrow(
          userPointService.update(
              WrapperProvider.updateWrapper(UserPoint::getId, userPoint.getId())
                  .set(UserPoint::getAvailablePoint, restPoint)),
          BizException::new,
          "更新用户积分失败");
    }
    // 扣减优惠券
    if (mainOrder.getCouponId() != null) {
      CheckHelper.trueOrThrow(
          userCouponService.update(
              WrapperProvider.updateWrapper(UserCoupon::getId, mainOrder.getCouponId())
                  .set(UserCoupon::getUsable, increased)),
          BizException::new,
          "更新用户优惠券失败");
    }
  }

  /**
   * 主订单拆单
   *
   * @param mainOrder MainOrder
   * @param skuItems List
   * @param ratio BigDecimal
   * @param param SubmitOrderParam 是否续租(续租不减库存)
   */
  private void separateOrder(
      MainOrder mainOrder, List<SkuItem> skuItems, BigDecimal ratio, SubmitOrderParam param) {
    Boolean rentAgain = param.getRentAgain();
    // 如果续租，取原主订单号,否则取新生成的主订单号(后期用于退款)
    String mainOrderNo = rentAgain ? param.getOriginMainOrderNo() : mainOrder.getMainOrderNo();
    List<Order> orders =
        skuItems.stream()
            .map(
                skuItem -> {
                  Order order = new Order();
                  order.setMainOrderId(mainOrder.getId());
                  order.setOriginMainOrderNo(mainOrderNo);
                  order.setOriginOrderId(param.getOriginOrderId());
                  order.setUserId(mainOrder.getUserId());
                  order.setBuyerName(mainOrder.getBuyerName());
                  order.setBuyerPhone(mainOrder.getBuyerPhone());
                  order.setStoreId(mainOrder.getStoreId());
                  order.setStatus(mainOrder.getStatus());
                  order.setOrderType(mainOrder.getOrderType());
                  order.setAddress(mainOrder.getAddress());
                  order.setOrderTime(mainOrder.getOrderTime());
                  order.setMessage(mainOrder.getMessage());
                  order.setRentAgain(mainOrder.getRentAgain());
                  order.setOrderNo(UniqueNoProvider.next(UniqueNoProvider.UniqueNoType.ESO));
                  order.setSkuId(skuItem.getSkuId());
                  order.setAmount(skuItem.getAmount().intValue());
                  order.setInstallFee(skuItem.getInstallFee());
                  order.setServiceFee(skuItem.getServiceFee());
                  order.setDepositFee(skuItem.getDepositFee());
                  order.setSkuPrice(skuItem.getPrice());
                  order.setSkuCostPrice(skuItem.getCostPrice());
                  order.setSkuName(skuItem.getSkuName());
                  order.setProductName(skuItem.getProductName());
                  order.setProductPic(skuItem.getProductPic());
                  // 设置订单价格和优惠
                  setOrderPriceAndDiscount(order, skuItem, ratio);
                  return order;
                })
            .collect(Collectors.toList());
    // 保存子订单
    CheckHelper.trueOrThrow(orderService.saveBatch(orders), BizException::new, "保存订单失败");
    // 更新库存(如果是续租,无需更新库存)
    if (!rentAgain) {
      resetSkuAmount(skuItems, mainOrder.getOrderType(), false);
    }
  }

  /**
   * 更新库存
   *
   * @param skuItems List
   * @param orderType OrderType
   * @param increased boolean 库存增加还是减少
   */
  private void resetSkuAmount(List<SkuItem> skuItems, OrderType orderType, boolean increased) {
    if (orderType == OrderType.HOME || orderType == OrderType.STORE) {
      resetHomeOrStoreAmount(skuItems, increased);
    } else if (orderType == OrderType.RENT) {
      resetRentAmount(skuItems, increased);
    }
  }

  /**
   * 重置上门或到店库存
   *
   * @param skuItems List
   * @param increased boolean
   */
  private void resetHomeOrStoreAmount(List<SkuItem> skuItems, boolean increased) {
    List<Sku> skus = Lists.newArrayList();
    Wrapper<Sku> queryWrapper;
    Sku sku;
    for (SkuItem skuItem : skuItems) {
      queryWrapper =
          WrapperProvider.oneQueryWrapper(Sku::getId, skuItem.getSkuId())
              .select(Sku::getId, Sku::getAmount);
      sku = skuService.getOne(queryWrapper);
      if (sku == null) {
        log.error("常规商品规格未找到:id={}", skuItem.getSkuId());
        continue;
      }
      sku.setAmount(obtainRestAmount(increased, skuItem, sku.getAmount()));
      skus.add(sku);
    }
    if (skus.size() == 0) {
      return;
    }
    CheckHelper.trueOrThrow(skuService.updateBatchById(skus), BizException::new, "更新库存失败");
  }

  /**
   * 重置租赁库存
   *
   * @param skuItems List
   * @param increased boolean
   */
  private void resetRentAmount(List<SkuItem> skuItems, boolean increased) {
    List<RentProduct> rentProducts = Lists.newArrayList();
    Wrapper<RentProduct> rentProductWrapper;
    for (SkuItem skuItem : skuItems) {
      RentProductTime rentProductTime = rentProductTimeService.getById(skuItem.getSkuId());
      if (rentProductTime == null) {
        log.error("重置租赁库存失败,租赁商品租期未找到:id={}", skuItem.getSkuId());
        continue;
      }
      rentProductWrapper =
          WrapperProvider.oneQueryWrapper(RentProduct::getId, rentProductTime.getRentProductId())
              .select(RentProduct::getId, RentProduct::getAmount);
      RentProduct rentProduct = rentProductService.getOne(rentProductWrapper);
      if (rentProduct == null) {
        log.error("重置租赁库存失败,租赁商品未找到:id={}", rentProductTime.getRentProductId());
        continue;
      }
      rentProduct.setAmount(obtainRestAmount(increased, skuItem, rentProduct.getAmount()));
      rentProducts.add(rentProduct);
    }
    if (rentProducts.size() == 0) {
      return;
    }
    CheckHelper.trueOrThrow(
        rentProductService.updateBatchById(rentProducts), BizException::new, "更新库存失败");
  }

  /**
   * 计算更新后的库存
   *
   * @param increased 增加还是减少
   * @param skuItem SkuItem
   * @param amount int(原库存)
   * @return int
   */
  private int obtainRestAmount(boolean increased, SkuItem skuItem, int amount) {
    int restAmount =
        increased
            ? amount + skuItem.getAmount().intValue()
            : amount - skuItem.getAmount().intValue();
    if (restAmount < 0) {
      throw new BizException("库存不足");
    }
    return restAmount;
  }

  /**
   * 设置子订单价格和优惠详情
   *
   * @param order Order
   * @param skuItem SkuItem
   * @param ratio BigDecimal
   */
  private void setOrderPriceAndDiscount(Order order, SkuItem skuItem, BigDecimal ratio) {
    BigDecimal goodsFee = skuItem.getPrice().multiply(skuItem.getAmount());
    order.setGoodsFee(goodsFee);
    order.setTotalFee(
        goodsFee
            .add(skuItem.getServiceFee())
            .add(skuItem.getInstallFee())
            .add(skuItem.getDepositFee())
            .subtract(skuItem.getPromotionTotalDiscount()));
    skuItem
        .getSkuPromotionDetails()
        .forEach(
            skuPromotionDetail -> {
              if (skuPromotionDetail.getPromotionType() == PromotionType.COUPON) {
                order.setCouponDiscount(skuPromotionDetail.getPromotionTotalDiscount());
              } else if (skuPromotionDetail.getPromotionType() == PromotionType.POINT) {
                order.setPointDiscount(skuPromotionDetail.getPromotionTotalDiscount());
                order.setUsePoint(skuPromotionDetail.getPromotionTotalDiscount().multiply(ratio));
              }
            });
  }

  /**
   * 清空购物车
   *
   * @param submitOrderParam SubmitOrderParam
   * @param userId Long
   */
  private void clearCartItems(SubmitOrderParam submitOrderParam, Long userId) {
    if (submitOrderParam.getFromCart()) {
      List<Long> skuIds =
          submitOrderParam.getSkuItems().stream()
              .map(SkuItem::getSkuId)
              .collect(Collectors.toList());
      CheckHelper.trueOrThrow(
          cartItemService.update(
              WrapperProvider.removeWrapper(CartItem::getUserId, userId)
                  .in(CartItem::getSkuId, skuIds)),
          BizException::new,
          "清空购物车失败");
    }
  }
}
