package com.byy.api.service.form.product;

import com.byy.api.service.form.page.IPageForm;
import com.byy.dal.entity.beans.product.RentProductTime;
import com.byy.dal.entity.beans.product.Sku;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 03:12
 * @Description:
 */
@Getter
@Setter
@ToString
public class RentProductForm implements IPageForm {
    /** 当前页(从1开始) */
    @NotNull private Long page = 1L;
    /** 分页大小 */
    @NotNull private Long size = 8L;
    /** 商品名模糊字段 */
    private String keyword;
    /** 上下架状态(上架:true,下架:false) */
    private Boolean onSale;
    /** 租赁商品id */
    private Long id;
    /** 商品名 */
    private String name;
    /** 商品主图 */
    private String image;
    /** 商品详情图 */
    private List<String> rentProductPics;
    /** 租期集合 */
    private List<RentProductTime> rentProductTimeList;
    /** 成本价(元/天) */
    private BigDecimal costPrice;
    /** 库存 */
    private Integer amount;
    /** 押金 */
    private BigDecimal deposit;
    /** 上级返佣比 */
    private BigDecimal parentCommissionRatio;
    /** 上上级返佣比 */
    private BigDecimal grandpaCommissionRatio;
    /** 租赁商品详情 */
    private String content;

}
