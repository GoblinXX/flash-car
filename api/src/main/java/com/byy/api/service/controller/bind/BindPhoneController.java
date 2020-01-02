package com.byy.api.service.controller.bind;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.security.wechat.WeChatApiService;
import com.byy.api.security.wechat.WeChatBindPhoneData;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.wechat.WeChatUserForm;
import com.byy.api.service.vo.wechat.WeChatUserVO;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.entity.beans.wechat.WeChatUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信绑定手机号
 *
 * @author yyc
 * @date 19-3-30 上午12:53
 */
@RestController
@RequestMapping("/bind-phone")
@Slf4j
public class BindPhoneController extends CommonController<WeChatUserService> {

  private final WeChatApiService weChatApiService;

  @Autowired
  public BindPhoneController(WeChatApiService weChatApiService) {
    this.weChatApiService = weChatApiService;
  }

  /**
   * 绑定手机号
   *
   * @param form WeChatUserForm
   * @return ResponseObject
   */
  @PostMapping
  public ResponseObject<WeChatUserVO> bindPhone(@RequestBody WeChatUserForm form) {
    WeChatUser weChatUser = baseService.getById(getCurrentUserId());
    WeChatBindPhoneData weChatBindPhoneData =
        weChatApiService.decrypt(
            form.getEncryptedData(),
            weChatUser.getSessionKey(),
            form.getIv(),
            WeChatBindPhoneData.class);
    boolean ret =
        baseService.update(
            Wrappers.<WeChatUser>lambdaUpdate()
                .eq(WeChatUser::getId, getCurrentUserId())
                .set(WeChatUser::getPhone, weChatBindPhoneData.getPhone()));
    weChatUser.setPhone(weChatBindPhoneData.getPhone());
    return trueOrError(ret, Transformer.fromBean(weChatUser, WeChatUserVO.class), "绑定手机失败");
  }
}
