package com.byy.api.service.controller.storecash;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.cash.CashForm;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.search.SearchForm;
import com.byy.api.service.form.storecash.StoreCashForm;
import com.byy.api.service.vo.action.DeletionVO;
import com.byy.api.service.vo.storecash.StoreCashVO;
import com.byy.biz.service.store.StoreService;
import com.byy.biz.service.storecash.StoreCashService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.store.Store;
import com.byy.dal.entity.beans.storecash.StoreCash;
import com.byy.dal.entity.dos.storecash.StoreCashDO;
import com.byy.dal.enums.CashType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.common.utils.helper.CheckHelper.isNotNull;
import static com.byy.dal.common.utils.helper.CheckHelper.trueOrThrow;

/**
 * @author: goblin
 * @date: 2019-06-27 15:44:29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/store/cash")
public class StoreCashController extends CommonController<StoreCashService> {

  private final StoreService storeService;

  /**
   * 增
   *
   * @param form StoreCashForm
   * @return ResponseObject
   */
  @PostMapping
  public ResponseObject<StoreCashVO> saveStoreCash(@RequestBody StoreCashForm form) {
    Store store = storeService.getById(form.getStoreId());
    trueOrThrow(form.getCashAmount().compareTo(store.getAvailableAmount()) < 1, BizException::new,
        "提现金额超出可用金额");
    return trueOrError(baseService.save(form), Transformer.fromBean(form, StoreCashVO.class),
        "保存失败");
  }

  /**
   * 删
   *
   * @param id Long
   * @return ResponseObject
   */
  @DeleteMapping("/{id}")
  public ResponseObject<DeletionVO> removeStoreCash(@PathVariable Long id) {
    return trueOrError(
        baseService.update(WrapperProvider.removeWrapper(StoreCash::getId, id)),
        DeletionVO.withId(id),
        "删除失败");
  }

  /**
   * 后台分页条件查询
   */
  @GetMapping("/list")
  public ResponseObject<ImmutableMap<String, Object>> getStoreCash(SearchForm searchForm,
      IPageForm pageForm, StoreCashForm form) {
    IPage<StoreCashDO> page = baseService
        .listBySearch(pageForm.newPage(), convertParams(form, searchForm));
    return success(ImmutableMap
        .of("list", Transformer.fromList(page.getRecords(), StoreCashVO.class), "total",
            page.getTotal()));
  }

  /**
   * 改
   *
   * @param form StoreCashForm
   * @return ResponseObject
   */
  @PutMapping
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<StoreCashVO> modifyStoreCash(@RequestBody StoreCashForm form) {
    if (CashType.SUCCESS.equals(form.getStatus())) {
      StoreCash storeCash = baseService.getById(form.getId());
      Store store = storeService.getById(storeCash.getStoreId());
      store.setAvailableAmount(store.getAvailableAmount().subtract(storeCash.getCashAmount()));
      trueOrThrow(storeService.updateById(store), BizException::new, "修改门店营业额失败");
    }
    return trueOrError(
        baseService.updateById(form), Transformer.fromBean(form, StoreCashVO.class), "修改失败");
  }

  /**
   * 提现列表参数封装
   */
  private Map<String, Object> convertParams(StoreCashForm storeCashForm, SearchForm searchForm) {
    Map<String, Object> params = Maps.newHashMap();
    if (isNotNull(searchForm.getKeyWord())) {
      params.put("keyWord", searchForm.getKeyWord());
    }
    if (isNotNull(searchForm.getStartTime())) {
      params.put("startTime", searchForm.getLocalStartTime());
    }
    if (isNotNull(searchForm.getEndTime())) {
      params.put("endTime", searchForm.getLocalEndTime());
    }
    if (isNotNull(storeCashForm.getStatus())) {
      params.put("status", storeCashForm.getStatus());
    }
    return params;
  }
}

