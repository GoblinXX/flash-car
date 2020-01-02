package com.byy.api.service.controller.punch;

import com.byy.api.response.ResponseObject;
import com.byy.api.service.controller.base.CommonController;
import com.byy.api.service.utils.PointUtils;
import com.byy.api.service.vo.punch.UserPunchVO;
import com.byy.biz.service.punch.UserPunchService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Transformer;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.punch.UserPunch;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.byy.api.response.ResponseObject.success;
import static com.byy.api.response.ResponseObject.error;
import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty;
import static com.byy.dal.enums.SceneType.PUNCH;

/** @Author: xcf @Date: 14/06/19 下午 02:46 @Description:用户签到Controller */
@RestController
@AllArgsConstructor
@RequestMapping("/punch")
public class UserPunchController extends CommonController<UserPunchService> {

  private PointUtils pointUtils;

  /**
   * 小程序端查询可获得积分
   *
   * @return
   */
  @GetMapping("/app")
  public ResponseObject<Object> getPunchRecordApp() {
    Long userId = getCurrentUserId();
    UserPunch userPunch =
        baseService.getOne(WrapperProvider.queryWrapper(UserPunch::getUserId, userId));
    if (isNotEmpty(userPunch)) {
      long index = LocalDateTime.now().toLocalDate().toEpochDay() - userPunch.getPunchTime().toLocalDate().toEpochDay();
      if (index == 0) {
        return success(userPunch.getAccumulatedDays());
      }else if (index == 1) {
        return success(userPunch.getAccumulatedDays() + 1);
      }else{
        return success(1);
      }
    } else {
      return success(1);
    }
  }

  /**
   * 小程序端签到
   *
   * @return
   */
  @PutMapping("/app")
  @Transactional(rollbackFor = Exception.class)
  public ResponseObject<UserPunchVO> modifyPunchRecordApp() {
    Long userId = getCurrentUserId();
    UserPunch userPunch =
        baseService.getOne(WrapperProvider.queryWrapper(UserPunch::getUserId, userId));
    if (isNotEmpty(userPunch)) {
      long days =
          LocalDate.now().toEpochDay() - userPunch.getPunchTime().toLocalDate().toEpochDay();
      if (days > 1) {
        userPunch.setPunchTime(LocalDateTime.now());
        userPunch.setAccumulatedDays(1);
      } else if (days == 1) {
        userPunch.setPunchTime(LocalDateTime.now());
        userPunch.setAccumulatedDays(userPunch.getAccumulatedDays() + 1);
      } else {
        if (userPunch.getAccumulatedDays() > 0) {
          return error(null);
        } else {
          userPunch.setPunchTime(LocalDateTime.now());
          userPunch.setAccumulatedDays(1);
        }
      }
      CheckHelper.trueOrThrow(
          baseService.updateById(userPunch), BizException::new, "签到失败,请检查相应参数!");
      // 修改用户积分,将积分记录存入明细表
      pointUtils.savePointAndRecord(
          userId, PUNCH, BigDecimal.valueOf(userPunch.getAccumulatedDays()), null);
    } else {
      UserPunch newUserPunch = new UserPunch();
      newUserPunch.setPunchTime(LocalDateTime.now());
      newUserPunch.setAccumulatedDays(1);
      newUserPunch.setUserId(userId);
      CheckHelper.trueOrThrow(baseService.save(newUserPunch), BizException::new, "签到失败,请检查相应参数!");
      // 修改用户积分,将积分记录存入明细表
      pointUtils.savePointAndRecord(
          userId, PUNCH, BigDecimal.valueOf(newUserPunch.getAccumulatedDays()), null);
    }
    return success(
        Transformer.fromBean(
            baseService.getOne(WrapperProvider.queryWrapper(UserPunch::getUserId, userId)),
            UserPunchVO.class));
  }
}
