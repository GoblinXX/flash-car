package com.byy.api.service.controller.storepic;

import com.byy.api.service.controller.base.CommonController;
import com.byy.biz.service.storepic.StorePicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: flash-car
 * @description: 门店详情图Controller
 * @author: Goblin
 * @create: 2019-06-11 16:01
 **/
@RestController
@RequestMapping("/store/pic")
@Slf4j
public class StorePicController extends CommonController<StorePicService> {

}
