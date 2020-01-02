package com.byy.api.service.controller.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.page.IPageForm;
import com.byy.api.service.form.sys.SaveSysUserForm;
import com.byy.api.service.form.sys.SysUserForm;
import com.byy.api.service.vo.action.DeletionVO;
import com.byy.api.service.vo.sys.SysUserVO;
import com.byy.biz.service.sys.SysAuthorityService;
import com.byy.biz.service.sys.SysUserService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.sys.SysAuthority;
import com.byy.dal.entity.beans.sys.SysUser;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.byy.api.response.ResponseObject.success;

/**
 * 后台用户
 *
 * @author: yyc
 * @date: 19-6-10 下午5:31
 */
@RestController
@RequestMapping("/sys-user")
public class SysUserController extends CommonController<SysUserService> {

  private final SysAuthorityService sysAuthorityService;

  @Autowired
  public SysUserController(SysAuthorityService sysAuthorityService) {
    this.sysAuthorityService = sysAuthorityService;
  }

  /**
   * 分页获取后台用户角色列表
   *
   * @param pageForm IPageForm
   * @param storeId Long
   * @return ResponseObject
   */
  @GetMapping("/page/{storeId}")
  public ResponseObject<ImmutableMap<String, Object>> getSysUser(
      IPageForm pageForm, @PathVariable Long storeId) {
    IPage<SysUser> page =
        baseService.page(
            pageForm.newPage(),
            WrapperProvider.queryWrapper(SysUser::getStoreId, storeId)
                .orderByDesc(SysUser::getUpdatedAt));
    List<SysUserVO> sysUserVOs =
        page.getRecords().stream()
            .map(
                sysUser -> {
                  SysUserVO sysUserVO = Transformer.fromBean(sysUser, SysUserVO.class);
                  List<String> sysAuthorities =
                      sysAuthorityService
                          .list(
                              WrapperProvider.queryWrapper(
                                  SysAuthority::getUserId, sysUser.getId()))
                          .stream()
                          .map(SysAuthority::getMenuName)
                          .collect(Collectors.toList());
                  sysUserVO.setSysAuthorities(sysAuthorities);
                  return sysUserVO;
                })
            .collect(Collectors.toList());
    return toPageMap(sysUserVOs, page.getTotal());
  }

  /**
   * 保存后台用户
   *
   * @param form SysUserForm
   * @return ResponseObject
   */
  @Transactional(rollbackFor = Exception.class)
  @PostMapping
  public ResponseObject<SysUserVO> saveSysUser(@Valid @RequestBody SaveSysUserForm form) {
    checkUserNameExists(form.getUsername(), form.getStoreId(), null);
    // 保存用户
    CheckHelper.trueOrThrow(baseService.save(form), BizException::new, "添加用户失败");
    // 保存角色权限
    if (!CollectionUtils.isEmpty(form.getSysAuthorities())) {
      saveSysAuthorities(form);
    }
    SysUserVO sysUserVO = Transformer.fromBean(form, SysUserVO.class);
    sysUserVO.setSysAuthorities(form.getSysAuthorities());
    return success(sysUserVO);
  }

  /**
   * 修改后台用户角色
   *
   * @param form SysUserForm
   * @return ResponseObject
   */
  @Transactional(rollbackFor = Exception.class)
  @PutMapping
  public ResponseObject<SysUserVO> modifySysUser(@RequestBody SysUserForm form) {
    checkUserNameExists(form.getUsername(), form.getStoreId(), form.getId());
    // 更新用户
    CheckHelper.trueOrThrow(baseService.updateById(form), BizException::new, "修改用户失败");
    // 清空用户权限
    sysAuthorityService.update(
        WrapperProvider.removeWrapper(SysAuthority::getUserId, form.getId()));
    if (!CollectionUtils.isEmpty(form.getSysAuthorities())) {
      saveSysAuthorities(form);
    }
    SysUserVO sysUserVO = Transformer.fromBean(form, SysUserVO.class);
    sysUserVO.setSysAuthorities(form.getSysAuthorities());
    return success(sysUserVO);
  }

  /**
   * 检查用户是否存在
   *
   * @param username String
   * @param userId Long
   * @param storeId Long
   */
  private void checkUserNameExists(String username, Long storeId, Long userId) {
    SysUser one =
        baseService.getOne(
            WrapperProvider.oneQueryWrapper(SysUser::getUsername, username)
                .eq(SysUser::getStoreId, storeId)
                .ne(userId != null, SysUser::getId, userId)
                .select(SysUser::getId));
    CheckHelper.trueOrThrow(one == null, BizException::new, username + "用户已存在");
  }

  /**
   * 删除后台用户
   *
   * @param id Long
   * @return ResponseObject
   */
  @DeleteMapping("/{id}")
  public ResponseObject<DeletionVO> removeSysUser(@PathVariable Long id) {
    return trueOrError(
        baseService.update(WrapperProvider.removeWrapper(SysUser::getId, id)),
        DeletionVO.withId(id),
        "删除用户失败");
  }

  /**
   * 保存用户权限
   *
   * @param form SysUserForm
   */
  private void saveSysAuthorities(SysUserForm form) {
    List<SysAuthority> sysAuthorities =
        form.getSysAuthorities().stream()
            .map(
                t -> {
                  SysAuthority sysAuthority = new SysAuthority();
                  sysAuthority.setMenuName(t);
                  sysAuthority.setUserId(form.getId());
                  return sysAuthority;
                })
            .collect(Collectors.toList());
    CheckHelper.trueOrThrow(
        sysAuthorityService.saveBatch(sysAuthorities), BizException::new, "保存用户权限失败");
  }
}
