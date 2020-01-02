package com.byy.api.service.utils;

import com.byy.biz.service.point.PointConfigService;
import com.byy.biz.service.point.UserPointRecordService;
import com.byy.biz.service.point.UserPointService;
import com.byy.biz.service.punch.UserPunchService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.provider.UniqueNoProvider;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.point.PointConfig;
import com.byy.dal.entity.beans.point.UserPoint;
import com.byy.dal.entity.beans.point.UserPointRecord;
import com.byy.dal.entity.beans.punch.UserPunch;
import com.byy.dal.enums.SceneType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import static com.byy.dal.enums.SceneType.*;

/** @Author: xcf @Date: 14/06/19 下午 04:34 @Description: */
@Component
public class PointUtils {
  private static PointUtils pointUtils;
  @Autowired private UserPointService userPointService;
  @Autowired private UserPointRecordService userPointRecordService;
  @Autowired private UserPunchService userPunchService;
  @Autowired private PointConfigService pointConfigService;

  @PostConstruct
  public void init() {
    pointUtils = this;
    pointUtils.userPointService = this.userPointService;
    pointUtils.userPointRecordService = this.userPointRecordService;
    pointUtils.userPunchService = this.userPunchService;
    pointUtils.pointConfigService = this.pointConfigService;
  }

  /**
   * 积分发生变动时 修改用户积分,添加积分明细
   *
   * @param userId 用户id 必传
   * @param sceneType 积分场景 必传
   * @param pointChange 正值 必传
   * @param orderNo 订单编号 没有就传null 不能传""
   */
  @Transactional(rollbackFor = Exception.class)
  public void savePointAndRecord(
      Long userId, SceneType sceneType, BigDecimal pointChange, String orderNo) {
    UserPoint userPoint =
        pointUtils.userPointService.getOne(
            WrapperProvider.queryWrapper(UserPoint::getUserId, userId));
    UserPointRecord userPointRecord = new UserPointRecord();
    userPointRecord.setUserId(userId);
    userPointRecord.setScene(sceneType);
    userPointRecord.setPreviousPoint(userPoint.getAvailablePoint());
    // 判断积分场景是否是积分抵扣 如果是积分抵扣,就应该减去积分变化量
    if (sceneType.equals(POINT_DEDUCTION)) {
      userPointRecord.setRecord(BigDecimal.ZERO.subtract(pointChange));
      userPointRecord.setCurrentPoint(userPoint.getAvailablePoint().subtract(pointChange));
      userPoint.setAvailablePoint(userPoint.getAvailablePoint().subtract(pointChange));
    } else {
      userPointRecord.setRecord(pointChange);
      userPointRecord.setCurrentPoint(userPoint.getAvailablePoint().add(pointChange));
      userPoint.setAvailablePoint(userPoint.getAvailablePoint().add(pointChange));
    }
    // 判断累计积分是否需要增加
    if (sceneType.equals(COMMENT)
        || sceneType.equals(PUNCH)
        || sceneType.equals(NEW_USER_FIRST_SHOPPING)) {
      userPoint.setCumulativePoint(userPoint.getCumulativePoint().add(pointChange));
    }
    if (orderNo != null) {
      userPointRecord.setOrderNo(orderNo);
    }
    CheckHelper.trueOrThrow(
        pointUtils.userPointService.updateById(userPoint), BizException::new, "用户积分更新失败,请检查相应参数!");
    CheckHelper.trueOrThrow(
        pointUtils.userPointRecordService.save(userPointRecord),
        BizException::new,
        "积分明细保存失败,请检查相应参数!");
  }
}
