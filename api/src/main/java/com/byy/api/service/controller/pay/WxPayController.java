package com.byy.api.service.controller.pay;

import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.BaseController;
import com.byy.api.service.form.payForm.PayForm;
import com.byy.biz.service.order.MainOrderService;
import com.byy.biz.service.order.OrderService;
import com.byy.biz.service.refundorder.RefundOrderService;
import com.byy.biz.service.roadrescueorder.RoadRescueOrderService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.OrderHelper;
import com.byy.dal.common.utils.provider.UniqueNoProvider;
import com.byy.dal.common.utils.provider.UniqueNoProvider.UniqueNoType;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.order.MainOrder;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import com.byy.dal.enums.OrderType;
import com.byy.pay.common.api.PayService;
import com.byy.pay.common.bean.PayOrder;
import com.byy.pay.common.bean.RefundOrder;
import com.byy.pay.common.http.HttpConfig;
import com.byy.pay.common.util.helper.SignHelper;
import com.byy.pay.wx.api.WeChatPayConfig;
import com.byy.pay.wx.api.WeChatPayService;
import com.byy.pay.wx.bean.WeChatTransactionType;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

import static com.byy.api.response.ResponseObject.error;
import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.enums.OrderStatus.RETURNED;

/**
 * @program: qugai
 * @description: 微信支付接口
 * @author: Goblin
 * @create: 2019-04-08 16:06
 */
@RestController
@RequestMapping("/wx")
@Slf4j
public class WxPayController extends BaseController {

  private PayService service = null;
  private final MainOrderService mainOrderService;
  private final RoadRescueOrderService roadRescueOrderService;
  private final OrderService orderService;
  private final RefundOrderService refundOrderService;

  @Value("${wxPayConfig.appId}")
  private String appId;

  @Value("${wxPayConfig.mchId}")
  private String mchId;

  @Value("${wxPayConfig.secretKey}")
  private String secretKey;

  @Value("${wxPayConfig.notifyUrl}")
  private String notifyUrl;

  @Value("${wxPayConfig.refundPath}")
  private String refundPath;

  @Autowired
  public WxPayController(
      MainOrderService mainOrderService,
      RoadRescueOrderService roadRescueOrderService,
      OrderService orderService,
      RefundOrderService refundOrderService) {
    this.mainOrderService = mainOrderService;
    this.roadRescueOrderService = roadRescueOrderService;
    this.orderService = orderService;
    this.refundOrderService = refundOrderService;
  }

  @PostConstruct
  public void init() {
    WeChatPayConfig wxPayConfigStorage = new WeChatPayConfig();
    // 公众账号ID
    wxPayConfigStorage.setAppId(appId);
    // 合作者id（商户号）
    wxPayConfigStorage.setMchId(mchId);

    wxPayConfigStorage.setSecretKey(secretKey);
    wxPayConfigStorage.setNotifyUrl(notifyUrl);
    wxPayConfigStorage.setSignType(SignHelper.MD5.name());
    wxPayConfigStorage.setInputCharset("utf-8");
    // 沙箱测试，正式上注释掉
    // wxPayConfigStorage.setTest(true);

    service = new WeChatPayService(wxPayConfigStorage);

    HttpConfig httpConfigStorage = new HttpConfig();

    // ssl 退款证书相关 不使用可注释
    if (!"ssl 退款证书".equals(refundPath)) {
      // TODO 这里也支持输入流的入参。
      httpConfigStorage.setKeystore(refundPath);
      httpConfigStorage.setStorePassword(mchId);
      httpConfigStorage.setOnPath(true);
    }

    // 请求连接池配置
    // 最大连接数
    httpConfigStorage.setMaxTotal(20);
    // 默认的每个路由的最大连接数
    httpConfigStorage.setDefaultMaxPerRoute(10);
    service.setRequestTemplateConfigStorage(httpConfigStorage);
  }

  /**
   * 公众号支付
   *
   * @param order 支付订单详情
   * @return 返回jsapi所需参数
   */
  @GetMapping("/pay")
  public ResponseObject<ImmutableMap<String, Object>> toPay(@Valid PayOrder order) {
    order.setTransactionType(WeChatTransactionType.JSAPI);
    String outTradeNo = order.getOutTradeNo();
    if (outTradeNo.contains(UniqueNoType.RO.name())) {
      RoadRescueOrder roadRescueOrder =
          roadRescueOrderService.getOne(
              WrapperProvider.oneQueryWrapper(RoadRescueOrder::getOrderNo, outTradeNo));
      order.setPrice(roadRescueOrder.getTotalFee());
    } else {
      MainOrder mainOrder =
          mainOrderService.getOne(
              WrapperProvider.oneQueryWrapper(MainOrder::getMainOrderNo, outTradeNo));
      order.setPrice(mainOrder.getTotalFee());
    }
    Map orderInfo = service.orderInfo(order);
    orderInfo.put("code", 0);

    return success(ImmutableMap.of("payOrder", orderInfo));
  }

  /**
   * 申请退款接口
   *
   * @param order 订单的请求体
   * @return 返回支付方申请退款后的结果
   */
  @PostMapping("/refund")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<ImmutableMap<String, Object>> refund(
      @Valid @RequestBody RefundOrder order) {
    com.byy.dal.entity.beans.refundorder.RefundOrder refundOrder = refundOrderService
        .getOne(WrapperProvider.oneQueryWrapper(
            com.byy.dal.entity.beans.refundorder.RefundOrder::getOutRefundNo, order.getRefundNo()));
    if (OrderType.RENT.equals(refundOrder.getOrderType())) {
      //续租订单退款
      Order originOrder = orderService
          .getOne(WrapperProvider.oneQueryWrapper(Order::getOrderNo, refundOrder.getOrderNo()));
      if (originOrder.getRentAgain()) {
        //续租 先退押金，再退支付金额
        MainOrder mainOrder = mainOrderService.getOne(WrapperProvider
            .oneQueryWrapper(MainOrder::getMainOrderNo, originOrder.getOriginMainOrderNo()));
        RefundOrder depositOrder = new RefundOrder();
        depositOrder.setRefundAmount(originOrder.getDepositFee());
        depositOrder.setTotalAmount(mainOrder.getTotalFee());
        depositOrder.setOutTradeNo(originOrder.getOriginMainOrderNo());
        depositOrder.setRefundNo(UniqueNoProvider.next(UniqueNoType.R));
        Map depositRefund = service.refund(depositOrder);
        log.info("depositRefund information is :{}" + depositRefund);
        //退支付金额
        Map payOrder = service.refund(order);
        log.info("payOrder information is :{}" + payOrder);
        return success(ImmutableMap.of("depositRefund", depositRefund, "payOrder", payOrder));
      }
    }
    Map refund = service.refund(order);
    log.info("refund information is :{}" + refund);
    return success(ImmutableMap.of("refund", refund));
  }

  /**
   * 退押金接口
   *
   * @param form 含子订单编号 退押金金额
   * @return 返回支付方申请退款后的结果
   */
  @PostMapping("/deposit")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<ImmutableMap<String, Object>> deposit(@RequestBody PayForm form) {
    RefundOrder refundOrder = new RefundOrder();
    Order order =
        orderService.getOne(WrapperProvider.queryWrapper(Order::getOrderNo, form.getOrderNo()));
    CheckHelper.trueOrThrow(CheckHelper.isNotNull(order), BizException::new, "订单号错误,请检查!");
    MainOrder mainOrder;
    // 判断该订单是否为续租订单
    if (order.getRentAgain()) {
      mainOrder =
          mainOrderService.getOne(
              WrapperProvider.queryWrapper(
                  MainOrder::getMainOrderNo, order.getOriginMainOrderNo()));
    } else {
      mainOrder = mainOrderService.getById(order.getMainOrderId());
    }
    // 比较输入押金与子订单押金大小(必须小于等于子订单押金才能退)
    if (form.getDepositAmount().compareTo(order.getDepositFee()) < 1) {
      refundOrder.setRefundAmount(form.getDepositAmount());
      refundOrder.setOutTradeNo(mainOrder.getMainOrderNo());
      refundOrder.setRefundNo(UniqueNoProvider.next(UniqueNoType.RJ));
      refundOrder.setTotalAmount(mainOrder.getTotalFee());
      order.setStatus(RETURNED);
      CheckHelper.trueOrThrow(
          orderService.updateById(order), BizException::new, "订单状态修改失败,请检查相应参数!");
      Map refund = service.refund(refundOrder);
      log.info("refund information is :{}" + refund);
      return success(ImmutableMap.of("refund", refund));
    } else {
      return error(-1, "押金不能大于总订单押金");
    }
  }

  /**
   * 支付回调地址 方式一 是属于简化方式， 试用与简单的业务场景
   */
  @Deprecated
  @RequestMapping(value = "/payBackBefore.json")
  public String payBackBefore(HttpServletRequest request) throws IOException {

    // 获取支付方返回的对应参数
    Map<String, Object> params =
        service.getParameter2Map(request.getParameterMap(), request.getInputStream());

    if (null == params) {

      return service.getPayOutMessage("fail", "失败").toMessage();
    }

    // 校验
    if (service.verify(params)) {
      String tradeNo = String.valueOf(params.get("out_trade_no"));
      log.info("tradeNo:{}", tradeNo);
      if (OrderHelper.isMainOrderNo(tradeNo)) {
        //发送短信
        mainOrderService.sendSms(tradeNo);
        // 主订单支付回调
        mainOrderService.asyncPayBack(tradeNo);
      } else {

        //发送短信
        roadRescueOrderService.sendSms(tradeNo);
        // 道路救援订单回调
        roadRescueOrderService.asyncPayBack(tradeNo);

      }
      return service.successPayOutMessage(null).toMessage();
    }

    return service.getPayOutMessage("fail", "失败").toMessage();
  }
}
