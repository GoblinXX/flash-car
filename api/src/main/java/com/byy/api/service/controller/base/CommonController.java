package com.byy.api.service.controller.base;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基本service控制层
 *
 * @author: yyc
 * @date: 19-3-31 下午5:15
 */
public abstract class CommonController<S extends IService<?>> implements IBaseController {

  @Autowired(required = false)
  protected S baseService;
}
