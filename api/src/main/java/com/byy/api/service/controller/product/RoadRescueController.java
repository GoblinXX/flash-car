package com.byy.api.service.controller.product;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.product.RoadRescueForm;
import com.byy.api.service.vo.product.RoadRescueVO;
import com.byy.biz.service.product.RoadRescueService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.entity.beans.product.RoadRescue;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.byy.api.response.ResponseObject.success;
import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;

import java.util.List;

/** @Author: xcf @Date: 12/06/19 下午 08:32 @Description: */
@AllArgsConstructor
@RestController
@RequestMapping("/rescue")
public class RoadRescueController extends CommonController<RoadRescueService> {

  /**
   * 后台查询道路救援
   *
   * @return
   */
  @GetMapping("/back")
  public ResponseObject<RoadRescueVO> getRoadRescueSys() {
    return success(getRoadRescue());
  }

  /**
   * 更新
   *
   * @param form
   * @return
   */
  @PutMapping("/back")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<RoadRescueVO> updateRoadRescueSys(@RequestBody RoadRescueForm form) {
    RoadRescue roadRescue = Transformer.fromBean(form, RoadRescue.class);
    CheckHelper.trueOrThrow(
        baseService.updateById(roadRescue), BizException::new, "保存道路救援信息失败,请检查相应参数!");
    return success(getRoadRescue());
  }

  /**
   * 小程序查询道路救援
   */
  @GetMapping("/app")
  public ResponseObject<RoadRescueVO> getRoadRescueApp() {
    return success(getRoadRescue());
  }

  /**
   * 查询道路救援信息
   *
   * @return
   */
  private RoadRescueVO getRoadRescue() {
    RoadRescue roadRescue = baseService.getOne(Wrappers.<RoadRescue>lambdaQuery().last("limit 1"));
    if(isNotEmpty(roadRescue)){
      return Transformer.fromBean(roadRescue, RoadRescueVO.class);
    }else{
      return null;
    }

  }
}
