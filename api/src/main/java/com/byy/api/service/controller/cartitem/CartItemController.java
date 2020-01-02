package com.byy.api.service.controller.cartitem;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.cartitem.CartItemForm;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.vo.action.DeletionVO;
import com.byy.api.service.vo.cartitem.CartItemVO;
import com.byy.biz.service.cartitem.CartItemService;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.cartitem.CartItem;
import com.byy.dal.entity.dos.cartitem.CartItemDO;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.common.utils.helper.CheckHelper.*;

/**
 * @author: goblin
 * @date: 2019-06-17 15:26:38
 */
@RestController
@RequestMapping("/cart/item")
public class CartItemController extends CommonController<CartItemService> {

  /**
   * 增
   *
   * @param form CartItemForm
   * @return ResponseObject
   */
  @PostMapping
  public ResponseObject<CartItemVO> saveCartItem(@RequestBody CartItemForm form) {
    form.setUserId(getCurrentUserId());
    CartItem cartItem = baseService.getOne(
        WrapperProvider.queryWrapper(CartItem::getSkuId, form.getSkuId())
            .eq(CartItem::getUserId, getCurrentUserId()));
    if (isNotNull(cartItem)) {
      //相同商品已存在购物车
      int quantity = form.getQuantity() + cartItem.getQuantity();
      cartItem.setQuantity(quantity);
      return trueOrError(baseService.updateById(cartItem),
          Transformer.fromBean(form, CartItemVO.class),
          "保存失败");
    } else {
      return trueOrError(baseService.save(form), Transformer.fromBean(form, CartItemVO.class),
          "保存失败");
    }
  }


  /**
   * * 批量删除购物车该信息
   */
  @DeleteMapping
  public ResponseObject<List<Long>> deleteCartItem(@RequestParam("ids") List<Long> ids) {
    return trueOrError(baseService.removeByIds(ids), ids, "删除失败");
  }

  /**
   * * 清空该用户的购物车
   */
  @DeleteMapping("/clear")
  public ResponseObject<Boolean> clearCartItem() {
    return success(
        baseService.remove(
            WrapperProvider.queryWrapper(CartItem::getUserId, getCurrentUserId())));
  }

  /**
   * 查
   *
   * @return ResponseObject
   */
  @GetMapping
  public ResponseObject<ImmutableMap<String, Object>> getCartItem(IPageForm pageForm) {
    IPage<CartItemDO> page = baseService.pageByCartItem(pageForm.newPage(),
        getCurrentUserId());
    return success(ImmutableMap
        .of("list", Transformer.fromList(page.getRecords(), CartItemVO.class), "totalPrice",
            BigDecimal.ZERO, "total",
            page.getTotal()));
  }

  @GetMapping("/total")
  public ResponseObject<BigDecimal> getCartItemTotalPrice(
      @RequestParam(value = "ids") List<Long> ids) {
    List<CartItemDO> list = baseService.listByCartItemIds(ids);
    BigDecimal totalPrice = list.stream()
        .map(oms -> oms.getSalePrice() == null ? BigDecimal.ZERO
            : oms.getSalePrice().multiply(BigDecimal.valueOf(oms.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return success(totalPrice);
  }

  /**
   * 改
   *
   * @param form CartItemForm
   * @return ResponseObject
   */
  @PutMapping
  public ResponseObject<CartItemVO> modifyCartItem(@RequestBody CartItemForm form) {
    return trueOrError(
        baseService.updateById(form), Transformer.fromBean(form, CartItemVO.class), "修改失败");
  }
}

