package com.byy.api.service.controller.upload;

import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.BaseController;
import com.byy.api.service.form.upload.UploadForm;
import com.byy.oss.client.CosStsClient;
import com.byy.oss.provider.CosProvider;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import static com.byy.api.response.ResponseObject.success;

/**
 * @author: yyc
 * @date: 19-4-11 上午11:14
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController extends BaseController {

  @Value("${CosConfig.secretId}")
  private String secretId;

  @Value("${CosConfig.secretKey}")
  private String secretKey;

  @Value("${CosConfig.bucketName}")
  private String bucket;

  @Value("${CosConfig.appId}")
  private String appId;

  @Value("${CosConfig.region}")
  private String region;

  private final CosProvider cosProvider;

  @Autowired
  public UploadController(CosProvider cosProvider) {
    this.cosProvider = cosProvider;
  }

  @PostMapping("/cos")
  public ResponseObject<String> cosUpload(@Valid UploadForm uploadForm) {
    try {
      return success(
          cosProvider.uploadInputStream(
              uploadForm.getFile().getInputStream(), UUID.randomUUID().toString()));
    } catch (IOException e) {
      throw new RuntimeException("文件上传失败", e);
    }
  }

  @GetMapping
  public ResponseObject<Map<String, Object>> testGetCredential() {

    TreeMap<String, Object> config = new TreeMap<String, Object>();

    try {

      // 固定密钥
      config.put("SecretId", secretId);
      // 固定密钥
      config.put("SecretKey", secretKey);

      // 临时密钥有效时长，单位是秒
      config.put("durationSeconds", 3000);

      // 换成你的 bucket
      config.put("bucket", bucket + "-" + appId);
      // 换成 bucket 所在地区
      config.put("region", region);

      // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的目录，例子：* 或者 a/* 或者 a.jpg
      config.put("allowPrefix", "*");

      // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
      String[] allowActions =
          new String[] {
            // 简单上传
            "name/cos:PutObject",
            // 分片上传
            "name/cos:InitiateMultipartUpload",
            "name/cos:ListMultipartUploads",
            "name/cos:ListParts",
            "name/cos:UploadPart",
            "name/cos:CompleteMultipartUpload"
          };
      config.put("allowActions", allowActions);

      JSONObject credential = CosStsClient.getCredential(config);
      return success(credential.toMap());
    } catch (Exception e) {
      throw new IllegalArgumentException("no valid secret !");
    }
  }
}
