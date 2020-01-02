package com.byy.api.service.controller.home;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.BaseController;
import com.byy.api.service.form.home.HomeForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.utils.SkuUtils;
import com.byy.api.service.vo.home.CarouselImgVO;
import com.byy.api.service.vo.home.HomeVO;
import com.byy.api.service.vo.home.LineChartVO;
import com.byy.api.service.vo.product.ProductVO;
import com.byy.api.service.vo.store.StoreVO;
import com.byy.biz.service.coupon.CouponService;
import com.byy.biz.service.coupon.UserCouponService;
import com.byy.biz.service.home.CarouselImgService;
import com.byy.biz.service.order.OrderService;
import com.byy.biz.service.product.ProductService;
import com.byy.biz.service.store.StoreService;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.coupon.Coupon;
import com.byy.dal.entity.beans.product.Product;
import com.byy.dal.entity.beans.store.Store;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.byy.api.response.ResponseObject.success;

/** @Author: xcf @Date: 17/06/19 下午 07:55 @Description: */
@RestController
@AllArgsConstructor
@RequestMapping("/home")
public class HomeController extends BaseController {

  private SkuUtils skuUtils;

  private ProductService productService;

  private UserCouponService userCouponService;

  private CouponService couponService;

  private StoreService storeService;

  private CarouselImgService carouselImgService;

  private OrderService orderService;

  /**
   * 小程序首页
   *
   * @param form
   * @return
   */
  @PostMapping
  public ResponseObject<ImmutableMap<String, Object>> getHomePage(@RequestBody HomeForm form) {
    //Long userId = getCurrentUserId();
    List<CarouselImgVO> carouselImgVOS =
        Transformer.fromList(carouselImgService.list(), CarouselImgVO.class);
  /*  List<UserCoupon> userCouponList =
        userCouponService.list(
            Wrappers.<UserCoupon>lambdaQuery().eq(UserCoupon::getUserId, userId));
    List<Long> couponIds =
        userCouponList.stream().map(UserCoupon::getCouponId).collect(Collectors.toList());
    List<Coupon> list;
    LocalDateTime now = LocalDateTime.now();
    if (couponIds.size() == 0) {
      list =
          couponService.list(
              Wrappers.<Coupon>lambdaQuery()
                  .ge(Coupon::getValidTo, now)
                  .orderByDesc(Coupon::getCreatedAt)
                  .last("limit 3"));
    } else {
      list =
          couponService.list(
              Wrappers.<Coupon>lambdaQuery()
                  .notIn(Coupon::getId, couponIds)
                  .ge(Coupon::getValidTo, now)
                  .orderByDesc(Coupon::getCreatedAt)
                  .last("limit 3"));
    }
    List<CouponVO> couponVOList = Transformer.fromList(list, CouponVO.class);
    couponVOList =
        couponVOList.stream()
            .peek(couponVO -> couponVO.setPossession(false))
            .collect(Collectors.toList());*/
    List<Coupon> couponList = couponService.list(Wrappers.<Coupon>lambdaQuery().last("limit 3"));
    List<Product> productList =
        productService.list(
            WrapperProvider.queryWrapper(Product::getOnHot, 1)
                .eq(Product::getOnSale, 1)
                .orderByDesc(Product::getUpdatedAt)
                .last("limit 6"));
    List<ProductVO> productVOList = Transformer.fromList(productList, ProductVO.class);
    productVOList =
        productVOList.stream()
            .peek(productVO -> skuUtils.setOne(productVO))
            .collect(Collectors.toList());
    IPage<Store> page = storeService.getStoreToHome(form.newPage());
    List<StoreVO> storeVOList = Transformer.fromList(page.getRecords(), StoreVO.class);
    ImmutableMap<String, Object> map =
        ImmutableMap.of(
            "carouselImgList",
            carouselImgVOS,
            "couponList",
            couponList,
            "productList",
            productVOList,
            "storeList",
            storeVOList,
            "storeTotal",
            page.getTotal());
    return success(map);
  }

  /**
   * 后台首页
   *
   * @param storeId
   * @param searchForm
   * @return
   */
  @GetMapping("/back/{storeId}")
  public ResponseObject<HomeVO> backHome(
      @PathVariable("storeId") Long storeId, SearchForm searchForm) {
    HomeVO homeVO = new HomeVO();
    // 查询昨日订单数
    Integer yesterdayOrders = orderService.getYesterdayOrders(storeId);
    // 昨日订单总额
    BigDecimal yesterdayAmount = orderService.getYesterdayAmount(storeId);
    // 总订单数
    Integer totalOrders = orderService.getTotalOrders(storeId);
    // 总订单额
    BigDecimal totalAmount = orderService.getTotalAmount(storeId);
    Store store = storeService.getById(storeId);
    if (storeId != 0) {
      // 门店id
      homeVO.setStoreId(store.getId());
      // 门店累计金额
      homeVO.setCumulativeAmount(store.getCumulativeAmount());
      // 门店可用金额
      homeVO.setAvailableAmount(store.getAvailableAmount());
    }
    // 处理时间
    Map<String, LocalDate> time = getTime(searchForm);
    // 获取折线图数据
    Map<String, Object> chartDataMap =
        orderService.getChartData(storeId, time.get("startTime"), time.get("endTime"));
    // 将数据塞入VO
    homeVO.setTotalAmount(totalAmount);
    homeVO.setTotalOrders(totalOrders);
    homeVO.setYesterdayOrders(yesterdayOrders);
    homeVO.setYesterdayAmount(yesterdayAmount);
    homeVO.setDateList(cast(chartDataMap.get("dateList")));
    homeVO.setOrderList(cast(chartDataMap.get("orderList")));
    homeVO.setAmountList(cast(chartDataMap.get("amountList")));
    return success(homeVO);
  }

  /**
   * 后台首页导出
   *
   * @param storeId
   * @param searchForm
   * @return
   */
  @GetMapping("/back/export/{storeId}")
  public ResponseObject<List<LineChartVO>> exportData(
      @PathVariable("storeId") Long storeId, SearchForm searchForm) {
    // 处理时间
    Map<String, LocalDate> time = getTime(searchForm);
    // 获取折线图数据
    List<LineChartVO> lineChartVOS =
        Transformer.fromList(
            orderService.getChartDataExport(storeId, time.get("startTime"), time.get("endTime")),
            LineChartVO.class);
    return success(lineChartVOS);
  }

  /**
   * 判断开始时间和结束时间,并做处理
   *
   * @param searchForm
   * @return
   */
  private Map<String, LocalDate> getTime(SearchForm searchForm) {
    Map<String, LocalDate> map = Maps.newHashMap();
    LocalDate startTime;
    LocalDate endTime;
    if (searchForm.getStartTime() == null || searchForm.getEndTime() == null) {
      endTime = LocalDateTime.now().toLocalDate();
      startTime = endTime.minusDays(7);
    } else {
      startTime = searchForm.getLocalStartTime().toLocalDate();
      endTime = searchForm.getLocalEndTime().toLocalDate().plusDays(1);
    }
    map.put("startTime", startTime);
    map.put("endTime", endTime);
    return map;
  }

  /**
   * 将Object类型转化为对应类型(解决强转警告)
   *
   * @param obj
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  private <T> T cast(Object obj) {
    return (T) obj;
  }
}
