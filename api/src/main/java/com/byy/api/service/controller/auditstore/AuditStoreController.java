package com.byy.api.service.controller.auditstore;


import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.common.utils.helper.CheckHelper.isNotNull;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.auditstore.AuditStoreForm;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.vo.auditstore.AuditStoreVO;
import com.byy.biz.service.auditstore.AuditStoreService;
import com.byy.biz.service.store.StoreService;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.auditstore.AuditStore;
import com.byy.dal.entity.beans.store.Store;
import com.byy.dal.enums.StoreCheckType;
import com.google.common.collect.ImmutableMap;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: flash-car
 * @description: 门店审核Controller
 * @author: Goblin
 * @create: 2019-06-11 16:41
 **/
@RestController
@RequestMapping("/audit/store")
@Slf4j
public class AuditStoreController extends CommonController<AuditStoreService> {

  private final StoreService storeService;

  public AuditStoreController(StoreService storeService) {
    this.storeService = storeService;
  }

  /**
   * 新增门店审批
   */
  @PostMapping
  public ResponseObject<AuditStoreVO> saveAuditStore(@RequestBody AuditStoreForm form) {
    form.setUserId(getCurrentUserId());
    return trueOrError(baseService.save(form), Transformer.fromBean(form, AuditStoreVO.class),
        "门店申请失败,请重试！");
  }

  /**
   * 查询当前用户的status
   */
  @GetMapping("/status")
  public ResponseObject<AuditStoreVO> getAuditStoreStatus() {
    AuditStore auditStore = baseService.getOne(
        WrapperProvider.queryWrapper(AuditStore::getUserId, getCurrentUserId())
            .notIn(AuditStore::getStatus,
                StoreCheckType.FAILED).last("limit 1"));
    if (auditStore == null) {
      return success(null);
    } else {
      return success(Transformer.fromBean(auditStore, AuditStoreVO.class));
    }
  }

  /**
   * 修改审核状态--如果审核通过则新增到门店列表里
   */
  @PutMapping
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<AuditStoreVO> updateStatus(@RequestBody AuditStoreForm form) {
    if (StoreCheckType.SUCCESS.equals(form.getStatus())) {
      Store store = new Store();
      store.setAuditStoreId(form.getId());
      storeService.save(store);
    }
    return trueOrError(baseService.updateById(form), Transformer.fromBean(form, AuditStoreVO.class),
        "门店审批失败,请重试！");
  }

  /**
   * 编辑
   */
  @GetMapping("/{id}")
  public ResponseObject<AuditStoreVO> getOne(@PathVariable("id") Long id) {
    return success(Transformer.fromBean(baseService.getById(id), AuditStoreVO.class));
  }

  /**
   * 后台分页条件查询审核网点
   */
  @GetMapping("/list")
  public ResponseObject<ImmutableMap<String, Object>> getList(AuditStoreForm form,
      IPageForm pageForm) {
    IPage<AuditStore> page = baseService.page(pageForm.newPage(), convertParams(form));
    return success(ImmutableMap.of("list", page.getRecords(), "total", page.getTotal()));
  }


  /**
   * 后台门店审核参数封装
   */
  private Wrapper<AuditStore> convertParams(@Valid AuditStoreForm form) {
    LambdaQueryWrapper<AuditStore> queryWrapper =
        Wrappers.<AuditStore>lambdaQuery().orderByDesc(AuditStore::getCreatedAt);
    if (isNotNull(form.getKeyWord())) {
      queryWrapper.like(AuditStore::getName, form.getKeyWord()).or()
          .like(AuditStore::getPhone, form.getKeyWord());
    }
    if (isNotNull(form.getStatus())) {
      queryWrapper.eq(AuditStore::getStatus, form.getStatus());
    }

    return queryWrapper;
  }


}
