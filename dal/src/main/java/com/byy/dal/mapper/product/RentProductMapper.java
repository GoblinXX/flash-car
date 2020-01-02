package com.byy.dal.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.dal.entity.beans.product.RentProduct;
import com.byy.dal.entity.dos.product.RentProductDO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 02:43
 * @Description:租赁商品Mapper
 */
public interface RentProductMapper extends BaseMapper<RentProduct> {
}
