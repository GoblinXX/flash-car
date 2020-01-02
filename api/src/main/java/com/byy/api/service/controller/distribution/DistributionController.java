package com.byy.api.service.controller.distribution;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.dal.common.utils.helper.CheckHelper.isNotBlank;
import static com.byy.dal.common.utils.helper.CheckHelper.isNotNull;
import static com.byy.dal.common.utils.helper.CheckHelper.trueOrThrow;

import com.byy.api.response.ResponseObject;
import com.byy.api.security.wechat.WeChatApiService;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.form.distribution.DistributionCenterForm;
import com.byy.api.service.vo.distribution.DistributionCenterVO;
import com.byy.biz.service.distribution.DistributionCenterService;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.distribution.DistributionCenter;
import com.byy.dal.entity.dos.distribution.DistributionCenterDO;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: flash-car
 * @description: 分销中心controller
 * @author: Goblin
 * @create: 2019-06-13 15:21
 **/
@RestController
@RequestMapping("/distribution")
@Slf4j
public class DistributionController extends CommonController<DistributionCenterService> {

  private final WeChatApiService weChatApiService;
  private final WeChatUserService weChatUserService;

  public DistributionController(WeChatApiService weChatApiService,
      WeChatUserService weChatUserService) {
    this.weChatApiService = weChatApiService;
    this.weChatUserService = weChatUserService;
  }

  /***
   * 邀请好友
   * @param form
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @PostMapping("/invite/friends")
  public ResponseObject<String> inviteFriends(@Valid @RequestBody DistributionCenterForm form) {
    String miniCode = weChatApiService.genWeChatMiniCode(form.getNextPage(), form.getScene(), 430);
    trueOrThrow(isNotBlank(miniCode), BizException::new, "邀请好友小程序码上传失败,用户id为:【%s】");
    return success(miniCode);
  }

  /**
   * 小程序查看个人分销中心信息
   */
  @GetMapping
  public ResponseObject<DistributionCenterVO> getDistributionCenter() {
    DistributionCenter distributionCenter = baseService
        .getOne(WrapperProvider.oneQueryWrapper(DistributionCenter::getUserId, getCurrentUserId()));
    DistributionCenterVO distributionCenterVO = Transformer
        .fromBean(baseService.selectByDisId(distributionCenter.getId()),
            DistributionCenterVO.class);
    if (isNotNull(distributionCenterVO)) {
      distributionCenterVO.setTotal(baseService.list(WrapperProvider
          .queryWrapper(DistributionCenter::getSuperiorId, distributionCenterVO.getUserId()))
          .size());
    }
    return success(distributionCenterVO);
  }

  /**
   * 新增 通过搜索进入小程序没有上级id,通过扫码进入则绑定上级id creatAt 用户登录时添加数据到distributionCenter中
   */
  @PostMapping
  public ResponseObject<Boolean> saveDistributionCenter(
      @Valid @RequestBody DistributionCenterForm form) {
    DistributionCenter distributionCenter = Transformer.fromBean(form, DistributionCenter.class);
    distributionCenter.setUserId(getCurrentUserId());
    boolean result =
        isNotNull(baseService.getOne(
            WrapperProvider.queryWrapper(DistributionCenter::getUserId, getCurrentUserId())));
    if (!result) {
      return success(baseService.save(distributionCenter));
    } else {
      return success(true);
    }

  }


}
