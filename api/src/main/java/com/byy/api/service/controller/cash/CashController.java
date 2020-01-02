package com.byy.api.service.controller.cash;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.common.utils.helper.CheckHelper.isNotNull;
import static com.byy.dal.common.utils.helper.CheckHelper.trueOrThrow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.cash.CashForm;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.vo.cash.CashVO;
import com.byy.biz.service.cash.CashService;
import com.byy.biz.service.commission.CommissionRecordsService;
import com.byy.biz.service.distribution.DistributionCenterService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.cash.Cash;
import com.byy.dal.entity.beans.commission.CommissionRecords;
import com.byy.dal.entity.beans.distribution.DistributionCenter;
import com.byy.dal.entity.dos.cash.CashDO;
import com.byy.dal.enums.CashType;
import com.byy.dal.enums.CommissionType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: flash-car
 * @description: 提现申请
 * @author: Goblin
 * @create: 2019-06-14 16:58
 **/
@RestController
@RequestMapping("/cash")
@Slf4j
public class CashController extends CommonController<CashService> {

  private final DistributionCenterService distributionCenterService;
  private final CommissionRecordsService commissionRecordsService;

  public CashController(DistributionCenterService distributionCenterService,
      CommissionRecordsService commissionRecordsService) {
    this.distributionCenterService = distributionCenterService;
    this.commissionRecordsService = commissionRecordsService;
  }

  /**
   * 提现申请
   */
  @PostMapping
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<CashVO> save(@RequestBody CashForm form) {
    form.setUserId(getCurrentUserId());
    DistributionCenter distribution = distributionCenterService
        .getOne(WrapperProvider.oneQueryWrapper(DistributionCenter::getUserId, getCurrentUserId()));
    trueOrThrow(form.getCashAmount().compareTo(distribution.getCurrentBalance()) < 1,
        BizException::new, "提现数量不能大于当前佣金余额,请重新输入提现金额!");
    distribution.setCurrentBalance(distribution.getCurrentBalance().subtract(form.getCashAmount()));
    distributionCenterService.updateById(distribution);
    return trueOrError(baseService.save(form), Transformer.fromBean(form, CashVO.class),
        "提现申请失败,请重试！");
  }

  /**
   * 后台分页条件查询提现记录
   */
  @GetMapping("/back/list")
  public ResponseObject<ImmutableMap<String, Object>> getBackList(CashForm form,
      SearchForm searchForm, IPageForm pageForm) {
    IPage<CashDO> cashDOIPage = baseService
        .listBySearch(pageForm.newPage(), convertParams(form, searchForm));
    return success(ImmutableMap
        .of("list", Transformer.fromList(cashDOIPage.getRecords(), CashVO.class), "total",
            cashDOIPage.getTotal()));
  }

  /***
   * 修改提现状态
   * @param form
   * @return
   */
  @PutMapping
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<Boolean> modifyCashStatus(@Valid @RequestBody CashForm form) {
    Cash cash = baseService.getById(form.getId());
    //修改用户佣金余额
    DistributionCenter distributionCenter = distributionCenterService
        .getOne(WrapperProvider.oneQueryWrapper(DistributionCenter::getUserId, cash.getUserId()));
    if (form.getStatus().equals(CashType.SUCCESS)) {
      //insert佣金记录
      CommissionRecords commissionRecords = new CommissionRecords();
      commissionRecords.setUserId(cash.getUserId());
      commissionRecords.setType(CommissionType.CASH);
      commissionRecords.setNum(cash.getCashAmount());
      commissionRecords.setBeforeCommission(distributionCenter.getCurrentBalance());
      commissionRecords.setAfterCommission(
          distributionCenter.getCurrentBalance().subtract(cash.getCashAmount()));
      commissionRecordsService.save(commissionRecords);
    } else {
      //回退佣金
      distributionCenter
          .setCurrentBalance(distributionCenter.getCurrentBalance().add(cash.getCashAmount()));
      distributionCenterService.updateById(distributionCenter);
    }

    return success(baseService.updateById(form));
  }

  /**
   * 小程序端分页条件查询我的提现记录
   */
  @GetMapping("/list")
  public ResponseObject<ImmutableMap<String, Object>> getAppList(CashForm form,
      IPageForm pageForm) {
    IPage<Cash> page = null;
    if (isNotNull(form.getStatus())) {
      page = baseService
          .page(pageForm.newPage(), WrapperProvider.queryWrapper(Cash::getStatus, form.getStatus())
              .eq(Cash::getUserId, getCurrentUserId()));
    } else {
      page = baseService
          .page(pageForm.newPage(),
              WrapperProvider.queryWrapper(Cash::getUserId, getCurrentUserId()));
    }

    return success(ImmutableMap
        .of("list", Transformer.fromList(page.getRecords(), CashVO.class), "total",
            page.getTotal()));
  }

  /**
   * 提现列表参数封装
   */
  private Map<String, Object> convertParams(CashForm form, SearchForm searchForm) {
    Map<String, Object> params = Maps.newHashMap();
    if (isNotNull(searchForm.getKeyWord())) {
      params.put("keyWord", searchForm.getKeyWord());
    }
    if (isNotNull(searchForm.getStartTime())) {
      params.put("startTime", searchForm.of(searchForm.getStartTime()));
    }
    if (isNotNull(searchForm.getEndTime())) {
      params.put("endTime", searchForm.of(searchForm.getEndTime()));
    }
    if (isNotNull(form.getStatus())) {
      params.put("status", form.getStatus());
    }
    return params;
  }
}
