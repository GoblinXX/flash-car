package com.byy.biz.config;

import com.byy.oss.config.CosConfig;
import com.byy.oss.provider.CosProvider;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @program: flash-car
 * @description:
 * @author: Goblin
 * @create: 2019-06-13 09:53
 */
@SpringBootConfiguration
public class OssConfig {

  @Value("${CosConfig.secretId}")
  private String SECRET_ID;

  @Value("${CosConfig.secretKey}")
  private String SECRET_KEY;

  @Value("${CosConfig.appId}")
  private String APP_ID;

  @Value("${CosConfig.bucketName}")
  private String BUCKET;

  @Bean
  public CosProvider cosProvider() {
    CosConfig cosConfig = new CosConfig();
    cosConfig.setAppId(APP_ID);
    cosConfig.setSecretKey(SECRET_KEY);
    cosConfig.setSecretId(SECRET_ID);
    cosConfig.setBucket(BUCKET);
    ClientConfig clientConfig = new ClientConfig(new Region("ap-chongqing"));
    cosConfig.setClientConfig(clientConfig);
    return new CosProvider(cosConfig);
  }
}
