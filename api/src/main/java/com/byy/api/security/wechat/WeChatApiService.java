package com.byy.api.security.wechat;

import static com.byy.api.security.wechat.WeChatAPI.WX_QR_CODE_API;
import static com.byy.dal.common.utils.helper.JsonHelper.str2Obj;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.biz.service.wechat.WeChatConfigService;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.common.utils.helper.AesCbcHelper;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.entity.beans.wechat.WeChatConfig;
import com.byy.dal.enums.GlobalErrorCode;
import com.byy.oss.provider.CosProvider;
import com.google.common.collect.Maps;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class WeChatApiService {


  private final RestTemplate restTemplate;
  private final WeChatConfigService weChatConfigService;
  private final WeChatUserService weChatUserService;
  private final CosProvider cosProvider;

  public WeChatApiService(
      RestTemplate restTemplate,
      WeChatConfigService weChatConfigService,
      WeChatUserService weChatUserService, CosProvider cosProvider) {
    this.restTemplate = restTemplate;
    this.weChatConfigService = weChatConfigService;
    this.weChatUserService = weChatUserService;
    this.cosProvider = cosProvider;
  }

  /**
   * 通过code获取sessionKey和openId
   *
   * @param code String
   * @return WeChatSessionData
   */
  public WeChatSessionData genWeChatSessionByCode(String code) {
    WeChatConfig config = obtainWeXinConfig();
    String appId = config.getAppId();
    String appSecret = config.getAppSecret();
    String url = String.format(WeChatAPI.JS_CODE2_SESSION_API, appId, appSecret, code);
    String retJson = restTemplate.getForObject(url, String.class);
    WeChatSessionData weChatSessionData = str2Obj(retJson, WeChatSessionData.class);
    boolean ret =
        weChatSessionData != null && Strings.isNotBlank(weChatSessionData.getSessionKey());
    CheckHelper.trueOrThrow(ret, UserAuthException::new, "通过code获取微信用户信息失败,失败信息:" + retJson);
    return weChatSessionData;
  }

  /**
   * 获取微信miniCode字节数组
   */
  public <T> String genWeChatMiniCode(String page, T scene, Integer width) {
    String accessToken = genAccessToken();
    String sendUrl = String.format(WX_QR_CODE_API, accessToken);

    Map<String, Object> params = Maps.newHashMap();
    params.put("page", page);
    params.put("scene", scene);
    params.put("width", width);
    HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(params, null);
    byte[] bytes = restTemplate.postForObject(sendUrl, httpEntity, byte[].class);
    return genFileUrlFromAli(bytes);
  }

  /**
   * 上传字节数组到腾讯云
   */
  public String genFileUrlFromAli(byte[] bytes) {
    return cosProvider.uploadInputStream(new ByteArrayInputStream(bytes),
        "shareMiniCode" + UUID.randomUUID() + ".png");
  }

  /**
   * 获取微信access_token
   */
  public String genAccessToken() {
    WeChatConfig config = obtainWeXinConfig();
    LocalDateTime expiredAt = config.getExpiredAt();
    // token没有过期直接使用
    if (expiredAt != null && expiredAt.isAfter(LocalDateTime.now())) {
      return config.getAccessToken();
    }
    // 否则调用微信api
    String appId = config.getAppId();
    String appSecret = config.getAppSecret();
    String sendUrl = String.format(WeChatAPI.ACCESS_TOKEN_API, appId, appSecret);
    String retJson = restTemplate.getForObject(sendUrl, String.class);
    WeChatAccessTokenData tokenData = str2Obj(retJson, WeChatAccessTokenData.class);
    // 将accessToken更新到数据库
    config.setAccessToken(tokenData.getAccessToken());
    config.setExpiredAt(tokenData.getExpiredAt());
    CheckHelper.trueOrThrow(
        weChatConfigService.updateById(config),
        BizException::new,
        GlobalErrorCode.ACCESS_TOKEN_UPDATE_ERROR);
    return config.getAccessToken();
  }


  /**
   * 获取三方配置
   *
   * @return WeChatConfig
   */
  private WeChatConfig obtainWeXinConfig() {
    WeChatConfig weChatConfig = weChatConfigService.getOne(Wrappers.emptyWrapper());
    return CheckHelper.nonEmptyOrThrow(
        weChatConfig, BizException::new, GlobalErrorCode.CONFIG_NOT_FOUND);
  }

  /**
   * 微信授权加密数据解析
   *
   * @param encryptedData String
   * @param sessionKey String
   * @param iv String
   * @param clazz Class
   * @return WeChatDecipherData
   */
  public <T> T decrypt(String encryptedData, String sessionKey, String iv, Class<T> clazz) {
    String ret = AesCbcHelper.decrypt(encryptedData, sessionKey, iv);
    return CheckHelper.nonEmptyOrThrow(
        str2Obj(ret, clazz), UserAuthException::new, "微信授权加密数据无法转化为对象");
  }
}
