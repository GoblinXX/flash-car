package com.byy.biz.service.distribution.impl;

import static com.byy.dal.common.utils.helper.CheckHelper.isNotNull;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.commission.CommissionRecordsService;
import com.byy.biz.service.distribution.DistributionCenterService;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.commission.CommissionRecords;
import com.byy.dal.entity.beans.distribution.DistributionCenter;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.beans.product.Product;
import com.byy.dal.entity.beans.product.RentProduct;
import com.byy.dal.entity.beans.product.RentProductTime;
import com.byy.dal.entity.beans.product.Sku;
import com.byy.dal.entity.dos.distribution.DistributionCenterDO;
import com.byy.dal.enums.CommissionType;
import com.byy.dal.enums.OrderType;
import com.byy.dal.mapper.distribution.DistributionCenterMapper;
import com.byy.dal.mapper.product.ProductMapper;
import com.byy.dal.mapper.product.RentProductMapper;
import com.byy.dal.mapper.product.RentProductTimeMapper;
import com.byy.dal.mapper.product.SkuMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: flash-car
 * @description: 分销中心实现类
 * @author: Goblin
 * @create: 2019-06-13 15:19
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class DistributionCenterServiceImpl extends
    ServiceImpl<DistributionCenterMapper, DistributionCenter> implements
    DistributionCenterService {

  @Autowired
  private DistributionCenterMapper distributionCenterMapper;
  @Autowired
  private SkuMapper skuMapper;
  @Autowired
  private ProductMapper productMapper;
  @Autowired
  private CommissionRecordsService commissionRecordsService;
  @Autowired
  private RentProductTimeMapper rentProductTimeMapper;
  @Autowired
  private RentProductMapper rentProductMapper;

  @Override
  public DistributionCenterDO selectByDisId(Long id) {
    return distributionCenterMapper.selectByDisId(id);
  }

  @Override
  public void computationOfCommissionRebate(Order order) {
    DistributionCenter distributionCenter = getOne(
        WrapperProvider.oneQueryWrapper(DistributionCenter::getUserId, order.getUserId()));
    if (isNotNull(distributionCenter) && isNotNull(distributionCenter.getSuperiorId())) {
      //计算上级
      Long superiorId = distributionCenter.getSuperiorId();
      DistributionCenter fatherDistribution = getOne(
          WrapperProvider.oneQueryWrapper(DistributionCenter::getUserId, superiorId));
      if (order.getOrderType().equals(OrderType.RENT)) {
        RentProductTime rentProductTime = rentProductTimeMapper.selectOne(
            WrapperProvider.oneQueryWrapper(RentProductTime::getId, order.getSkuId()).or()
                .eq(RentProductTime::getId, order.getSkuId()));
        RentProduct rentProduct = rentProductMapper.selectOne(
            WrapperProvider.oneQueryWrapper(RentProduct::getId, rentProductTime.getRentProductId())
                .or().eq(RentProduct::getId, rentProductTime.getRentProductId()));
        BigDecimal parentCommissionRatio = rentProduct.getParentCommissionRatio();
        BigDecimal grandpaCommissionRatio = rentProduct.getGrandpaCommissionRatio();
        //计算第一级返佣
        BigDecimal subtract = order.getTotalFee().subtract(rentProduct.getCostPrice());
        BigDecimal firstCommission = subtract.multiply(parentCommissionRatio)
            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal secondCommission = subtract.multiply(grandpaCommissionRatio)
            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        insertRecords(order, fatherDistribution, firstCommission);
        DistributionCenter grapaUser = distributionCenterMapper.selectOne(WrapperProvider
            .oneQueryWrapper(DistributionCenter::getUserId, fatherDistribution.getSuperiorId()));
        if (isNotNull(grapaUser) && isNotNull(fatherDistribution.getSuperiorId())) {
          //计算上级的上级佣金
          insertRecords(order, grapaUser, secondCommission);
        }
      } else {
        Sku sku = skuMapper.selectOne(
            WrapperProvider.oneQueryWrapper(Sku::getId, order.getSkuId()).or()
                .eq(Sku::getId, order.getSkuId()));
        Product product = productMapper.selectOne(
            WrapperProvider.oneQueryWrapper(Product::getId, sku.getProductId()).or()
                .eq(Product::getId, sku.getProductId()));
        BigDecimal parentCommissionRatio = product.getParentCommissionRatio();
        BigDecimal grandpaCommissionRatio = product.getGrandpaCommissionRatio();
        //计算第一级返佣
        BigDecimal subtract = order.getTotalFee().subtract(sku.getCostPrice());
        BigDecimal firstCommission = subtract.multiply(parentCommissionRatio)
            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal secondCommission = subtract.multiply(grandpaCommissionRatio)
            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        insertRecords(order, fatherDistribution, firstCommission);
        DistributionCenter grapaUser = distributionCenterMapper.selectOne(WrapperProvider
            .oneQueryWrapper(DistributionCenter::getUserId, fatherDistribution.getSuperiorId()));
        if (isNotNull(grapaUser) && isNotNull(fatherDistribution.getSuperiorId())) {
          //计算上级的上级佣金
          insertRecords(order, grapaUser, secondCommission);
        }
      }


    }
  }

  public void insertRecords(Order order, DistributionCenter distributionCenter,
      BigDecimal firstCommission) {
    BigDecimal currentBalance = distributionCenter.getCurrentBalance();
    CommissionRecords commissionRecords = new CommissionRecords();
    commissionRecords.setBeforeCommission(currentBalance);
    commissionRecords.setAfterCommission(currentBalance.add(firstCommission));
    commissionRecords.setNum(firstCommission);
    commissionRecords.setOrderNo(order.getOrderNo());
    commissionRecords.setType(CommissionType.GET);
    commissionRecords.setUserId(distributionCenter.getUserId());
    //生成佣金记录
    commissionRecordsService.save(commissionRecords);

    distributionCenter.setAccumulatedCommission(
        distributionCenter.getAccumulatedCommission().add(firstCommission));
    distributionCenter
        .setCurrentBalance(currentBalance.add(firstCommission));
    //修改用户佣金
    distributionCenterMapper.updateById(distributionCenter);
  }
}
