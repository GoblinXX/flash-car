package com.byy.api.service.controller.roadrescuepic;

import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.roadrescuepic.RoadRescuePicForm;
import com.byy.api.service.vo.action.DeletionVO;
import com.byy.api.service.vo.roadrescuepic.RoadRescuePicVO;
import com.byy.biz.service.roadrescuepic.RoadRescuePicService;
import com.byy.dal.common.utils.helper.Transformer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.byy.api.response.ResponseObject.success;

/**
 * @author: goblin
 * @date: 2019-06-18 15:24:33
 */
@RestController
@RequestMapping("/road-rescue-pic")
public class RoadRescuePicController extends CommonController<RoadRescuePicService> {

  /**
   * 增
   *
   * @param form RoadRescuePicForm
   * @return ResponseObject
   */
  @PostMapping
  public ResponseObject<RoadRescuePicVO> saveRoadRescuePic(@RequestBody RoadRescuePicForm form) {
    return trueOrError(baseService.save(form), Transformer.fromBean(form, RoadRescuePicVO.class),
        "保存失败");
  }

  /**
   * 删
   *
   * @param id Long
   * @return ResponseObject
   */
  @DeleteMapping("/{id}")
  public ResponseObject<DeletionVO> removeRoadRescuePic(@PathVariable Long id) {
    return trueOrError(baseService.removeById(id), DeletionVO.withId(id), "删除失败");
  }

  /**
   * 查
   *
   * @param id Long
   * @return ResponseObject
   */
  @GetMapping("/{id}")
  public ResponseObject<RoadRescuePicVO> getRoadRescuePic(@PathVariable Long id) {
    return success(Transformer.fromBean(baseService.getById(id), RoadRescuePicVO.class));
  }

  /**
   * 改
   *
   * @param form RoadRescuePicForm
   * @return ResponseObject
   */
  @PutMapping
  public ResponseObject<RoadRescuePicVO> modifyRoadRescuePic(@RequestBody RoadRescuePicForm form) {
    return trueOrError(
        baseService.updateById(form), Transformer.fromBean(form, RoadRescuePicVO.class), "修改失败");
  }
}

