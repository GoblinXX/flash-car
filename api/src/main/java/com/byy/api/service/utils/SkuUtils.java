package com.byy.api.service.utils;

import com.byy.api.service.vo.product.ProductVO;
import com.byy.biz.service.product.SkuService;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.product.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 19/06/19 下午 06:25
 * @Description:
 */
@Component
public class SkuUtils {

  private static SkuUtils SkuUtils;
  @Autowired
  private SkuService skuService;

  @PostConstruct
  public void init() {
    SkuUtils = this;
    SkuUtils.skuService = this.skuService;
  }


  //取出售价最小的规格存入VO中
  public void setOne(ProductVO productVO) {
    List<Sku> skuList =
            SkuUtils.skuService.list(
                    WrapperProvider.queryWrapper(Sku::getProductId, productVO.getId()));
    Sku sku =
            skuList.stream()
                    .min(Comparator.comparing(Sku::getSalePrice))
                    .orElse(new Sku());
    productVO.setSalePrice(sku.getSalePrice());
    productVO.setOriginalPrice(sku.getOriginalPrice());
    productVO.setCostPrice(sku.getCostPrice());
    productVO.setAmount(sku.getAmount());
  }
}
