package com.byy.api.service.controller.wechatuser;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.common.utils.helper.CheckHelper.isNotNull;
import static com.byy.dal.enums.OrderStatus.RECEIVED;
import static com.byy.dal.enums.OrderStatus.RETURNED;
import static com.byy.dal.enums.OrderStatus.RETURNING;
import static com.byy.dal.enums.OrderStatus.SHIPPED;
import static com.byy.dal.enums.OrderStatus.SUCCESS;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.form.wechat.SubordinateForm;
import com.byy.api.service.vo.wechat.SubordinateVO;
import com.byy.api.service.vo.wechat.WeChatUserVO;
import com.byy.biz.service.order.OrderService;
import com.byy.biz.service.roadrescueorder.RoadRescueOrderService;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import com.byy.dal.entity.beans.wechat.WeChatUser;
import com.byy.dal.entity.dos.wechat.WeChatUserDO;
import com.byy.dal.enums.OrderStatus;
import com.byy.dal.enums.RoadType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: flash-car
 * @description: 微信用户列表
 * @author: Goblin
 * @create: 2019-06-21 10:04
 **/
@RestController
@RequestMapping("/wechat/user")
@AllArgsConstructor
@Slf4j
public class WeChatUserController extends CommonController<WeChatUserService> {

  private OrderService orderService;
  private RoadRescueOrderService roadRescueOrderService;

  @GetMapping
  public ResponseObject<ImmutableMap<String, Object>> getList(SearchForm searchForm,
      IPageForm pageForm) {
    IPage<WeChatUserDO> iPage = baseService
        .listUserBySys(pageForm.newPage(), convertParams(searchForm));
    List<WeChatUserVO> list = Transformer.fromList(iPage.getRecords(), WeChatUserVO.class);
    List<WeChatUserVO> weChatUserVOList = list.stream().map(R -> {
      String superiorId = R.getSuperiorId();
      if (isNotNull(superiorId)) {
        WeChatUser superiorUser = baseService
            .getOne(WrapperProvider.oneQueryWrapper(WeChatUser::getId, superiorId).or()
                .eq(WeChatUser::getId, superiorId));
        R.setSuperiorName(superiorUser.getNickname());
      } else {
        R.setSuperiorName("系统");
      }
      return R;
    }).collect(Collectors.toList());
    return success(ImmutableMap
        .of("list", weChatUserVOList, "total",
            iPage.getTotal()));
  }

  @GetMapping("/subordinate")
  public ResponseObject<ImmutableMap<String, Object>> getSubordinateList(SearchForm searchForm,
      IPageForm pageForm, SubordinateForm form) {
    IPage<WeChatUserDO> iPage = baseService
        .checkSubordinateUserInfo(pageForm.newPage(), convertParams2Subordinate(searchForm, form));
    return success(ImmutableMap
        .of("list", Transformer.fromList(iPage.getRecords(), WeChatUserVO.class), "total",
            iPage.getTotal()));
  }

  /**
   * 小程序查看下级列表
   */
  @GetMapping("/app/subordinate")
  public ResponseObject<ImmutableMap<String, Object>> getAppSubordinateList(IPageForm pageForm) {
    IPage<WeChatUser> userIPage = baseService
        .selectSubordinateInfo(pageForm.newPage(), getCurrentUserId());
    List<SubordinateVO> subordinateVOS = Transformer
        .fromList(userIPage.getRecords(), SubordinateVO.class);
    if (isNotNull(subordinateVOS)) {
      getTotalPaid(subordinateVOS);
    }
    return success(ImmutableMap
        .of("list", subordinateVOS, "total", userIPage.getTotal()));
  }


  /**
   * 后台用户列表参数封装
   */
  private Map<String, Object> convertParams(SearchForm form) {
    Map<String, Object> params = Maps.newHashMap();
    if (isNotNull(form.getKeyWord())) {
      params.put("keyWord", form.getKeyWord());
    }
    return params;
  }

  /**
   * 后台用户查看下级列表参数封装
   */
  private Map<String, Object> convertParams2Subordinate(SearchForm form,
      SubordinateForm subordinateForm) {
    Map<String, Object> params = Maps.newHashMap();
    if (isNotNull(form.getKeyWord())) {
      params.put("keyWord", form.getKeyWord());
    }
    if (isNotNull(subordinateForm.getUserId())) {
      params.put("userId", subordinateForm.getUserId());
    }
    return params;
  }

  private void getTotalPaid(List<SubordinateVO> subordinateVOS) {
    subordinateVOS.stream().forEach(subordinateVO -> {
      List<OrderStatus> collections =
          Lists.newArrayList(SHIPPED, RECEIVED, RETURNING, RETURNED, SUCCESS);
      List<Order> orders = orderService
          .list(WrapperProvider.queryWrapper(Order::getUserId, subordinateVO.getId())
              .in(Order::getStatus, collections));
      BigDecimal totalOrderFee = orders.stream()
          .map(order -> order.getTotalFee() == null ? BigDecimal.ZERO : order.getTotalFee())
          .reduce(
              BigDecimal.ZERO, BigDecimal::add);

      List<RoadType> roadTypes = Lists.newArrayList(RoadType.PROCESSING, RoadType.SUCCESS);
      BigDecimal totalRoadFee = roadRescueOrderService.list(
          WrapperProvider.queryWrapper(RoadRescueOrder::getUserId, subordinateVO.getId())
              .in(RoadRescueOrder::getStatus, roadTypes)).stream().map(
          roadOrder -> roadOrder.getTotalFee() == null ? BigDecimal.ZERO
              : roadOrder.getTotalFee()).reduce(BigDecimal.ZERO, BigDecimal::add);
      subordinateVO.setTotalPaid(totalRoadFee.add(totalOrderFee));
    });
  }
}
