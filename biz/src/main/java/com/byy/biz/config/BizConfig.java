package com.byy.biz.config;

import com.byy.biz.service.Scanned;
import com.byy.biz.service.coupon.UserCouponService;
import com.byy.biz.service.point.UserPointService;
import com.byy.biz.service.price.chain.CouponPromotionPriceChain;
import com.byy.biz.service.price.chain.PointPromotionPriceChain;
import com.byy.biz.service.price.chain.AbstractPromotionPriceChain;
import com.byy.dal.DalConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * @author: yyc
 * @date: 19-6-10 上午10:07
 */
@SpringBootConfiguration
@Import({DalConfig.class, RedisCacheConfig.class, OssConfig.class})
@ComponentScan(basePackageClasses = {Scanned.class})
@EnableAspectJAutoProxy
public class BizConfig {

  /**
   * 注入密码加密
   *
   * @return 密码加密生成器
   */
  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(11);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public AbstractPromotionPriceChain promotionPriceChain(
      UserCouponService userCouponService, UserPointService userPointService) {
    CouponPromotionPriceChain couponPromotionPriceChain =
        new CouponPromotionPriceChain(userCouponService);
    // 积分链
    PointPromotionPriceChain pointPromotionPriceChain =
        new PointPromotionPriceChain(userPointService);
    // 优惠券链
    couponPromotionPriceChain.setNextChain(pointPromotionPriceChain);
    // 返回第一个开始计算的链
    return couponPromotionPriceChain;
  }
}
