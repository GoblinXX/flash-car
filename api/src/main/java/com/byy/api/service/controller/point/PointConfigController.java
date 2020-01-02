package com.byy.api.service.controller.point;

import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.point.PointConfigForm;
import com.byy.api.service.vo.point.PointConfigVO;
import com.byy.biz.service.point.PointConfigService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.entity.beans.point.PointConfig;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.api.response.ResponseObject.error;
import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;

/** @Author: xcf @Date: 14/06/19 下午 03:58 @Description: */
@RestController
@AllArgsConstructor
@RequestMapping("/point/config")
public class PointConfigController extends CommonController<PointConfigService> {

  /**
   * 后台查询积分配置
   *
   * @return
   */
  @GetMapping("/back")
  public ResponseObject<List<PointConfigVO>> getPointConfigSys() {
    return success(getPointConfig());
  }

  /**
   * 后台修改积分配置
   *
   * @param form
   * @return
   */
  @PutMapping("/back")
  public ResponseObject<List<PointConfigVO>> updatePointConfigSys(
      @RequestBody PointConfigForm form) {
    CheckHelper.trueOrThrow(
        baseService.updateBatchById(form.getPointConfigList()),
        BizException::new,
        "修改积分配置表失败,请检查相应参数!");
    return success(getPointConfig());
  }

  /**
   * 获取积分配置
   *
   * @return
   */
  private List<PointConfigVO> getPointConfig() {
    List<PointConfig> list = baseService.list();
    return Transformer.fromList(list, PointConfigVO.class);
  }
}
