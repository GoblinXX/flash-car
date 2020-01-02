package com.byy.api.service.controller.commission;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.common.utils.helper.CheckHelper.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.commission.CommissionRecordsForm;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.page.PageForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.vo.commission.CommissionRecordsVO;
import com.byy.biz.service.commission.CommissionRecordsService;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.commission.CommissionRecords;
import com.byy.dal.entity.dos.commission.CommissionRecordsDO;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: flash-car
 * @description: 佣金记录controller
 * @author: Goblin
 * @create: 2019-06-14 10:43
 **/
@RestController
@RequestMapping("/commission/records")
@Slf4j
public class CommissionRecordsController extends CommonController<CommissionRecordsService> {

  /**
   * 后台查询用户的佣金明细
   */
  @GetMapping("/list")
  public ResponseObject<ImmutableMap<String, Object>> getList(SearchForm searchForm,
      IPageForm pageForm) {
    IPage<CommissionRecords> iPage = baseService
        .page(pageForm.newPage(), convertParams(searchForm));
    return success(ImmutableMap
        .of("list", Transformer.fromList(iPage.getRecords(), CommissionRecordsVO.class), "total",
            iPage.getTotal()));
  }
  //小程序端收益明细，需要下级名称，根据订单编号关联查询--在已完成订单计算分销佣金

  /**
   * 小程序端分页查询我的佣金明细
   */
  @GetMapping("/app")
  public ResponseObject<ImmutableMap<String, Object>> getAppList(IPageForm pageForm) {
    IPage<CommissionRecordsDO> iPage = baseService
        .listBySearch(pageForm.newPage(), getCurrentUserId());
    return success(ImmutableMap
        .of("list", Transformer.fromList(iPage.getRecords(), CommissionRecordsVO.class), "total",
            iPage.getTotal()));
  }

  /**
   * 后台佣金记录参数封装
   */
  private Wrapper<CommissionRecords> convertParams(@Valid SearchForm form) {
    LambdaQueryWrapper<CommissionRecords> queryWrapper =
        Wrappers.<CommissionRecords>lambdaQuery().orderByDesc(CommissionRecords::getCreatedAt);
    if (isNotNull(form.getKeyWord())) {
      queryWrapper.eq(CommissionRecords::getUserId, form.getKeyWord());
    }
    if (isNotNull(form.getStartTime())) {
      queryWrapper.ge(CommissionRecords::getCreatedAt, form.of(form.getStartTime()));
    }
    if (isNotNull(form.getEndTime())) {
      queryWrapper.le(CommissionRecords::getCreatedAt, form.of(form.getEndTime()));
    }
    return queryWrapper;
  }
}
