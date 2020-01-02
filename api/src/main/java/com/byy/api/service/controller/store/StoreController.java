package com.byy.api.service.controller.store;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.common.utils.helper.CheckHelper.isNotNull;
import static com.byy.dal.common.utils.helper.CheckHelper.trueOrThrow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.store.StoreForm;
import com.byy.api.service.vo.auditstore.AuditStoreVO;
import com.byy.api.service.vo.store.StoreVO;
import com.byy.api.service.vo.store.StoresVO;
import com.byy.biz.service.auditstore.AuditStoreService;
import com.byy.biz.service.store.StoreService;
import com.byy.biz.service.storepic.StorePicService;
import com.byy.biz.service.sys.SysAuthorityService;
import com.byy.biz.service.sys.SysUserService;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.auditstore.AuditStore;
import com.byy.dal.entity.beans.store.Store;
import com.byy.dal.entity.beans.storepic.StorePic;
import com.byy.dal.entity.beans.sys.SysAuthority;
import com.byy.dal.entity.beans.sys.SysUser;
import com.byy.dal.entity.beans.wechat.WeChatUser;
import com.byy.dal.entity.dos.store.StoreDO;
import com.byy.dal.enums.StoreCheckType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: flash-car
 * @description: 门店controller
 * @author: Goblin
 * @create: 2019-06-11 15:35
 **/
@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController extends CommonController<StoreService> {

  private List<String> menus = Lists.newArrayList();

  {
    menus.add("orderList");
    menus.add("refundList");
    menus.add("profile");
    menus.add("authorityManagement");
  }

  private final AuditStoreService auditStoreService;
  private final WeChatUserService weChatUserService;
  private final StorePicService storePicService;
  private final SysUserService sysUserService;
  private final SysAuthorityService sysAuthorityService;

  public StoreController(AuditStoreService auditStoreService,
      WeChatUserService weChatUserService,
      StorePicService storePicService, SysUserService sysUserService,
      SysAuthorityService sysAuthorityService) {
    this.auditStoreService = auditStoreService;
    this.weChatUserService = weChatUserService;
    this.storePicService = storePicService;
    this.sysUserService = sysUserService;
    this.sysAuthorityService = sysAuthorityService;
  }

  /**
   * 后台新增门店
   */
  @PostMapping
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<StoreVO> saveStore(@RequestBody StoreForm form) {
    //后台添加需要给客户添加userId,这里负责人手机号一定要跟wechatUser的phone一致
    WeChatUser weChatUser = weChatUserService.getOne(
        WrapperProvider.oneQueryWrapper(WeChatUser::getPhone, form.getAuditStoreForm().getPhone()));
    if (isNotNull(weChatUser)) {
      form.getAuditStoreForm().setUserId(weChatUser.getId());
    }
    //新增审核门店信息
    if (isNotNull(form.getAuditStoreForm())) {
      form.getAuditStoreForm().setStatus(StoreCheckType.SUCCESS);
      trueOrThrow(auditStoreService.save(form.getAuditStoreForm()), BizException::new, "新增审核门店失败");
    }
    //新增门店
    form.setAuditStoreId(form.getAuditStoreForm().getId());
    trueOrThrow(baseService.save(form), BizException::new, "新增门店失败");
    //新增门店后台用户
    SysUser sysUser = sysUserService
        .getOne(WrapperProvider.oneQueryWrapper(SysUser::getUsername, form.getUsername()));
    trueOrThrow(sysUser == null, BizException::new, "后台账户用户名已存在！");
    SysUser storeUser = new SysUser();
    storeUser.setUsername(form.getUsername());
    storeUser.setPassword(form.getPassword());
    storeUser.setStoreId(form.getId());
    trueOrThrow(sysUserService.save(storeUser), BizException::new, "新增门店后台管理用户失败");
    //新增门店后台用户权限--超级管理员
    List<SysAuthority> authorities = menus.stream().map(m -> {
      SysAuthority sysAuthority = new SysAuthority();
      sysAuthority.setMenuName(m);
      sysAuthority.setUserId(storeUser.getId());
      return sysAuthority;
    }).collect(Collectors.toList());
    sysAuthorityService.saveBatch(authorities);

    //新增门店详情图
    List<String> images = form.getImages();
    List<StorePic> picList = getStorePics(form, images);
    trueOrThrow(storePicService.saveBatch(picList), BizException::new, "新增门店详情图失败");
    return success(Transformer.fromBean(form, StoreVO.class));
  }

  /**
   * 更改门店上下架状态
   *
   * @param form id和onSale
   */
  @PutMapping("/sale")
  public ResponseObject<StoreVO> updateOnSale(@RequestBody StoreForm form) {
    return trueOrError(baseService.updateById(form), Transformer.fromBean(form, StoreVO.class),
        "门店上下架失败,请重试！");
  }


  /**
   * 删除门店--同时要删除关联的门店账户和门店申请和门店详情图
   */
  @DeleteMapping("/{id}")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<Long> deleteStore(@PathVariable("id") Long id) {
    Store store = baseService.getById(id);

    trueOrThrow(baseService.removeById(id) &&
            storePicService.remove(WrapperProvider.queryWrapper(StorePic::getStoreId, id))
            && auditStoreService
            .remove(WrapperProvider.queryWrapper(AuditStore::getId, store.getAuditStoreId())),
        BizException::new, "删除门店失败,请重试！");
    return success(id);
  }


  /**
   * 编辑某个门店
   */
  @GetMapping("/{id}")
  public ResponseObject<StoreVO> getOne(@PathVariable("id") Long id) {
    Store store = baseService.getById(id);
    AuditStore auditStore = auditStoreService.getById(store.getAuditStoreId());
    //获取子门店用户列表的第一个，按时间降序
    SysUser storeUser = sysUserService
        .getOne(WrapperProvider.oneQueryWrapper(SysUser::getStoreId, id)
            .orderByDesc(SysUser::getCreatedAt));

    StoreVO storeVO = Transformer.fromBean(store, StoreVO.class);
    if (isNotNull(storeUser)) {
      storeVO.setUsername(storeUser.getUsername());
      storeVO.setPassword(storeUser.getPassword());
    }
    storeVO.setAuditStoreVO(Transformer.fromBean(auditStore, AuditStoreVO.class));
    List<StorePic> picList = storePicService
        .list(WrapperProvider.queryWrapper(StorePic::getStoreId, id));
    if (isNotNull(picList)) {
      storeVO.setImages(picList);
    }
    return success(storeVO);
  }

  /**
   * 后台分页条件查询门店列表
   */
  @GetMapping("/list")
  public ResponseObject<ImmutableMap<String, Object>> getList(StoreForm form, IPageForm pageForm) {
    IPage<StoreDO> storeDOIPage = baseService.listBySearch(pageForm.newPage(), convertParams(form));
    return success(ImmutableMap
        .of("list", Transformer.fromList(storeDOIPage.getRecords(), StoresVO.class), "total",
            storeDOIPage.getTotal()));
  }

  /**
   * 修改门店信息
   */
  @PutMapping
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<StoreVO> updatedStore(@RequestBody StoreForm form) {

    if (isNotNull(form.getImages())) {
      storePicService.remove(WrapperProvider.queryWrapper(StorePic::getStoreId, form.getId()));
      //修改门店详情图
      List<String> images = form.getImages();
      List<StorePic> picList = getStorePics(form, images);
      trueOrThrow(storePicService.saveBatch(picList), BizException::new, "修改门店详情图失败");
    }
    SysUser sysUser = Transformer.fromBean(sysUserService
        .getOne(WrapperProvider.oneQueryWrapper(SysUser::getStoreId, form.getId())
            .orderByDesc(SysUser::getCreatedAt)), SysUser.class);
    if (isNotNull(sysUser)) {
      if (sysUser.getUsername().equals(form.getUsername())) {
        sysUser.setUsername(form.getUsername());
        sysUser.setPassword(form.getPassword());
        trueOrThrow(sysUserService.updateById(sysUser), BizException::new, "修改门店用户失败");
      } else {
        SysUser backUser = sysUserService
            .getOne(WrapperProvider.oneQueryWrapper(SysUser::getUsername, form.getUsername()));
        trueOrThrow(backUser == null, BizException::new, "后台账户用户名已存在！");
        sysUser.setUsername(form.getUsername());
        sysUser.setPassword(form.getPassword());
        trueOrThrow(sysUserService.updateById(sysUser), BizException::new, "修改门店用户失败");
      }
    } else {
      SysUser storeUser = new SysUser();
      storeUser.setUsername(form.getUsername());
      storeUser.setPassword(form.getPassword());
      storeUser.setStoreId(form.getId());
      trueOrThrow(sysUserService.save(storeUser), BizException::new, "新增门店后台管理用户失败");
    }
    if (isNotNull(form.getAuditStoreForm())) {
      trueOrThrow(auditStoreService.updateById(form.getAuditStoreForm()), BizException::new,
          "修改审核门店失败");
    }
    trueOrThrow(baseService.updateById(Transformer.fromBean(form, Store.class)), BizException::new,
        "修改门店失败");
    return success(Transformer.fromBean(form, StoreVO.class));

  }

  /**
   * 后台门店列表参数封装
   */
  private Map<String, Object> convertParams(StoreForm form) {
    Map<String, Object> params = Maps.newHashMap();
    if (isNotNull(form.getName())) {
      params.put("name", form.getName());
    }
    if (isNotNull(form.getOnSale())) {
      params.put("onSale", form.getOnSale());
    }
    return params;
  }

  /**
   * 获取每个详情图
   */
  private List<StorePic> getStorePics(StoreForm form, List<String> images) {
    return images.stream().map(image -> {
      StorePic storePic = new StorePic();
      storePic.setStoreId(form.getId());
      storePic.setImage(image);
      return storePic;
    }).collect(Collectors.toList());
  }
}
