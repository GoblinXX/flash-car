package com.byy.api.security.wechat;

/**
 * @author: yyc
 * @date: 19-2-27 上午11:47
 */
public interface WeChatAPI {

  /** 票据code换取openId和sessionKey */
  String JS_CODE2_SESSION_API =
      "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

  /** 获取accessToken */
  String ACCESS_TOKEN_API =
      "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

  /** 获取无限制的小程序码 */
  String WX_QR_CODE_API = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";
}
