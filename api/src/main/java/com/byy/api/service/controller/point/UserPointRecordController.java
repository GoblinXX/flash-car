package com.byy.api.service.controller.point;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.point.UserPointRecordForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.vo.point.UserPointRecordVO;
import com.byy.biz.service.point.UserPointRecordService;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.entity.beans.point.UserPointRecord;
import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;
import static com.byy.api.response.ResponseObject.success;

/** @Author: xcf @Date: 14/06/19 下午 04:24 @Description: */
@RestController
@RequestMapping("/point/record")
public class UserPointRecordController extends CommonController<UserPointRecordService> {

  /**
   * 后台查询用户积分明细
   *
   * @param form
   * @return
   */
  @GetMapping("/back")
  public ResponseObject<ImmutableMap<String, Object>> getUserPointRecordSys(
      @Valid UserPointRecordForm form, SearchForm searchForm) {
    LambdaQueryWrapper<UserPointRecord> queryWrapper = convertParams(form, searchForm);
    IPage<UserPointRecord> page = baseService.page(form.newPage(), queryWrapper);
    List<UserPointRecordVO> userPointRecordVOS =
        Transformer.fromList(page.getRecords(), UserPointRecordVO.class);
    return success(ImmutableMap.of("list", userPointRecordVOS, "total", page.getTotal()));
  }

  /**
   * 后台查询参数封装 将查询条件拼接
   *
   * @param form
   * @return
   */
  private LambdaQueryWrapper<UserPointRecord> convertParams(
      UserPointRecordForm form, SearchForm searchForm) {
    LambdaQueryWrapper<UserPointRecord> queryWrapper =
        Wrappers.<UserPointRecord>lambdaQuery().eq(UserPointRecord::getUserId, form.getUserId());
    if (isNotEmpty(searchForm.getStartTime())) {
      queryWrapper.ge(UserPointRecord::getCreatedAt, searchForm.of(searchForm.getStartTime()));
    }
    if (isNotEmpty(searchForm.getEndTime())) {
      queryWrapper.le(UserPointRecord::getCreatedAt, searchForm.of(searchForm.getEndTime()));
    }
    return queryWrapper.orderByDesc(UserPointRecord::getCreatedAt);
  }
}
