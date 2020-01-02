package com.byy.biz.service.roadrescueorder.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.sys.SysUserService;
import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import com.byy.dal.entity.beans.sys.SysUser;
import com.byy.dal.entity.dos.roadrescueorder.RoadRescueOrderDO;
import com.byy.dal.enums.RoadType;
import com.byy.dal.mapper.roadrescueorder.RoadRescueOrderMapper;
import com.byy.biz.service.roadrescueorder.RoadRescueOrderService;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author: goblin
 * @date: 2019-06-18 15:09:34
 */
@Service
public class RoadRescueOrderServiceImpl extends ServiceImpl<RoadRescueOrderMapper, RoadRescueOrder>
    implements RoadRescueOrderService {

  @Value("${sms.appId}")
  private int appId;

  @Value("${sms.appKey}")
  private String appKey;

  @Value("${sms.templateId4}")
  private int templateId4;

  @Value("${sms.smsSign}")
  private String smsSign;
  private final SysUserService sysUserService;

  public RoadRescueOrderServiceImpl(SysUserService sysUserService) {
    this.sysUserService = sysUserService;
  }

  @Override
  public RoadRescueOrderDO selectRoadRescue(Long id) {
    return baseMapper.selectRoadRescue(id);
  }

  @Override
  public IPage<RoadRescueOrderDO> selectRoadRescueToApp(Page page, Map<String, Object> params) {
    return baseMapper.selectRoadRescueToApp(page, params);
  }

  @Override
  public IPage<RoadRescueOrderDO> selectRoadRescueToBack(Page page, Map<String, Object> params) {
    return baseMapper.selectRoadRescueToBack(page, params);
  }

  @Override
  public void asyncPayBack(String orderNo) {
    RoadRescueOrder roadRescueOrder = getOne(
        WrapperProvider.oneQueryWrapper(RoadRescueOrder::getOrderNo, orderNo));
    if (RoadType.SUBMITTED.equals(roadRescueOrder.getStatus())) {
      roadRescueOrder.setStatus(RoadType.PAID);
      roadRescueOrder.setPaidAt(LocalDateTime.now());
      CheckHelper.trueOrThrow(updateById(roadRescueOrder), BizException::new, "回调更改订单信息失败");
    }
  }

  @Override
  public void sendSms(String orderNo) {
    RoadRescueOrder roadRescueOrder = getOne(
        WrapperProvider.oneQueryWrapper(RoadRescueOrder::getOrderNo, orderNo));
    List<SysUser> sysUsers = sysUserService
        .list(WrapperProvider.queryWrapper(SysUser::getStoreId, roadRescueOrder.getStoreId()));
    String[] phoneNumbers = new String[sysUsers.size()];
    List<String> phones = sysUsers.stream().map(sysUser -> {
      String username = sysUser.getUsername();
      return username;
    }).collect(Collectors.toList());
    phoneNumbers = phones.toArray(phoneNumbers);
    if (RoadType.SUBMITTED.equals(roadRescueOrder.getStatus())) {
      senderResult(templateId4, phoneNumbers);
    }

  }

  /***
   * 指定模板ID单发短信
   * @param phoneNumbers
   * @return
   */
  public SmsMultiSenderResult senderResult(int templateId, String[] phoneNumbers) {
    SmsMultiSenderResult result = null;
    try {
      String[] params = new String[]{};
      SmsMultiSender msender = new SmsMultiSender(appId, appKey);
      result = msender.sendWithParam("86", phoneNumbers,
          // 签名参数未提供或者为空时，会使用默认签名发送短信
          templateId, params, smsSign, "", "");
      System.out.print(result);
    } catch (HTTPException e) {
      // HTTP响应码错误
      e.printStackTrace();
    } catch (JSONException e) {
      // json解析错误
      e.printStackTrace();
    } catch (IOException e) {
      // 网络IO错误
      e.printStackTrace();
    }
    return result;
  }
}
