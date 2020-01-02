package com.byy.api.service.controller.address;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.address.UserAddressForm;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.vo.action.DeletionVO;
import com.byy.api.service.vo.address.UserAddressVO;
import com.byy.biz.service.address.UserAddressService;
import com.byy.biz.service.location.AddressChainService;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.address.UserAddress;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.byy.api.response.ResponseObject.success;

/**
 * @author: yyc
 * @date: 2019-06-17 15:51:22
 */
@RestController
@RequestMapping("/user-address")
public class UserAddressController extends CommonController<UserAddressService> {

  private final AddressChainService addressChainService;

  @Autowired
  public UserAddressController(AddressChainService addressChainService) {
    this.addressChainService = addressChainService;
  }

  /**
   * 增
   *
   * @param form UserAddressForm
   * @return ResponseObject
   */
  @Transactional(rollbackFor = Exception.class)
  @PostMapping
  public ResponseObject<UserAddressVO> saveUserAddress(@RequestBody UserAddressForm form) {
    Long userId = getCurrentUserId();
    updateDefaultAddress(userId, form.getOnDefault());
    form.setUserId(userId);
    return trueOrError(
        baseService.save(form), Transformer.fromBean(form, UserAddressVO.class), "保存失败");
  }

  /**
   * 查询当前用户的默认地址,如果没有则查询第一条地址
   */
  @GetMapping("/default")
  public ResponseObject<UserAddressVO> getDefaultUserAddress() {
    UserAddress userAddress =
        baseService.getOne(
            WrapperProvider.oneQueryWrapper(UserAddress::getUserId, getCurrentUserId())
                .eq(UserAddress::getOnDefault, true));
    if (userAddress == null) {
      userAddress =
          baseService.getOne(
              WrapperProvider.oneQueryWrapper(UserAddress::getUserId, getCurrentUserId()));
    }
    return success(Transformer.fromBean(userAddress, UserAddressVO.class));
  }

  /**
   * 删
   *
   * @param id Long
   * @return ResponseObject
   */
  @DeleteMapping("/{id}")
  public ResponseObject<DeletionVO> removeUserAddress(@PathVariable Long id) {
    return trueOrError(
        baseService.remove(WrapperProvider.removeWrapper(UserAddress::getId, id)),
        DeletionVO.withId(id),
        "删除失败");
  }

  /**
   * 我的地址列表
   *
   * @return ResponseObject
   */
  @GetMapping("/page")
  public ResponseObject<ImmutableMap<String, Object>> getUserAddresses(IPageForm pageForm) {
    IPage<UserAddress> page =
        baseService.page(
            pageForm.newPage(),
            WrapperProvider.queryWrapper(UserAddress::getUserId, getCurrentUserId()));
    List<UserAddressVO> addressVOs =
        page.getRecords().stream()
            .map(
                userAddress -> {
                  UserAddressVO addressVO = Transformer.fromBean(userAddress, UserAddressVO.class);
                  addressVO.setAddressChain(
                      addressChainService.loadAddressChain(addressVO.getAreaId()));
                  return addressVO;
                })
            .collect(Collectors.toList());
    return success(ImmutableMap.of("list", addressVOs, "total", page.getTotal()));
  }

  /**
   * 改
   *
   * @param form UserAddressForm
   * @return ResponseObject
   */
  @Transactional(rollbackFor = Exception.class)
  @PutMapping
  public ResponseObject<UserAddressVO> modifyUserAddress(@RequestBody UserAddressForm form) {
    updateDefaultAddress(getCurrentUserId(), form.getOnDefault());
    return trueOrError(
        baseService.updateById(form), Transformer.fromBean(form, UserAddressVO.class), "修改失败");
  }

  /**
   * 更新默认地址
   *
   * @param userId Long
   * @param onDefault Boolean
   */
  private void updateDefaultAddress(Long userId, Boolean onDefault) {
    if (onDefault != null && onDefault) {
      baseService.update(
          WrapperProvider.updateWrapper(UserAddress::getUserId, userId)
              .eq(UserAddress::getOnDefault, true)
              .set(UserAddress::getOnDefault, false));
    }
  }
}
