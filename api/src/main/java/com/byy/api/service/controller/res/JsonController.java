package com.byy.api.service.controller.res;

import com.byy.api.response.ResponseObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 容器健康检查(勿删)
 *
 * @author: yyc
 * @date: 19-5-15 上午12:44
 */
@RestController
@RequestMapping("/json")
public class JsonController {

  @PostMapping
  public ResponseObject<Object> ret() {
    return ResponseObject.success(null);
  }
}
