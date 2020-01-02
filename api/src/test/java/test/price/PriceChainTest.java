package test.price;

import com.byy.biz.config.BizConfig;
import com.byy.biz.service.price.cart.SkuItem;
import com.byy.biz.service.price.chain.AbstractPromotionPriceChain;
import com.byy.biz.service.price.params.PromotionContext;
import com.byy.biz.service.price.promotion.CouponPromotion;
import com.byy.biz.service.price.promotion.Promotion;
import com.byy.dal.enums.PromotionType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BizConfig.class)
public class PriceChainTest {

  @Autowired
  AbstractPromotionPriceChain promotionPriceChain;

  @Test
  public void test_price() {
    SkuItem c1 = new SkuItem();
    c1.withSkuId(1L)
        .withAmount(BigDecimal.valueOf(5))
        .withCostPrice(BigDecimal.valueOf(40))
        .withPrice(BigDecimal.valueOf(100))
        .withPromotionTotalDiscount(BigDecimal.ZERO)
        .withItemTotalPrice(c1.getPrice().multiply(c1.getAmount()))
        .withSkuPromotionDetails(Lists.newArrayList());
    SkuItem c2 = new SkuItem();
    c2.withSkuId(1L)
        .withAmount(BigDecimal.valueOf(1))
        .withCostPrice(BigDecimal.valueOf(40))
        .withPrice(BigDecimal.valueOf(200))
        .withPromotionTotalDiscount(BigDecimal.ZERO)
        .withItemTotalPrice(c2.getPrice().multiply(c2.getAmount()))
        .withSkuPromotionDetails(Lists.newArrayList());
    List<SkuItem> cartItems = Lists.newArrayList();
    cartItems.add(c1);
    cartItems.add(c2);

    BigDecimal total =
        cartItems.stream()
            .map(cartItem -> cartItem.getPrice().multiply(cartItem.getAmount()))
            .reduce(BigDecimal::add)
            .orElse(null);
    BigDecimal cost =
        cartItems.stream()
            .map(cartItem -> cartItem.getCostPrice().multiply(cartItem.getAmount()))
            .reduce(BigDecimal::add)
            .orElse(null);
    ImmutableMap<PromotionType, Promotion> promotionTypeMap =
        ImmutableMap.of(
//            PromotionType.POINT,
//            new PointPromotion().withPoint(BigDecimal.valueOf(60)),
            PromotionType.COUPON,
            new CouponPromotion().withPromotionId(6L));
    PromotionContext promotionParam =
        new PromotionContext()
            .withSkuItems(cartItems)
            .withGoodsFee(total)
            .withCostFee(cost)
            .withTotalDiscount(BigDecimal.ZERO)
            .withUserId(4L)
            .withSelectPromotionMap(promotionTypeMap);
    promotionPriceChain.calculate(promotionParam);
    System.out.println();
  }
}
