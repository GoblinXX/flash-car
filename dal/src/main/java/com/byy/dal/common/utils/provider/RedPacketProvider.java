package com.byy.dal.common.utils.provider;

import com.byy.dal.common.errors.BizException;
import java.math.BigDecimal;

/**
 * 红包算法
 *
 * @author: yyc
 * @date: 19-5-3 下午3:37
 */
public class RedPacketProvider {

  private static final BigDecimal factor = BigDecimal.valueOf(2);
  private static final BigDecimal min = BigDecimal.valueOf(0.01);

  /**
   * 检查红包是否可以生成
   *
   * @param restFee 红包剩余金额
   * @param restCount 红包剩余数量
   */
  public static void checkEven(BigDecimal restFee, Integer restCount, String errorMsg) {
    if (restFee == null
        || restFee.compareTo(BigDecimal.ZERO) <= 0
        || restCount == null
        || restCount <= 0) {
      throw new BizException(errorMsg);
    }
    BigDecimal even = restFee.divide(BigDecimal.valueOf(restCount), 2, BigDecimal.ROUND_DOWN);
    if (even.compareTo(min) < 0) {
      throw new BizException("当前红包金额无法满足每个人的最小限额");
    }
  }

  /**
   * 获取下一个红包(带上限的红包)
   *
   * @param restFee 红包剩余金额
   * @param restCount 红包剩余数量
   * @return 本次获取红包的金额
   */
  public static BigDecimal gainFee(BigDecimal restFee, Integer restCount) {
    checkEven(restFee, restCount, "红包已领完");
    // 红包只剩下一个直接返回
    if (restCount == 1) {
      return restFee;
    }
    // 否则开始计算应得的金额
    BigDecimal random =
        restFee
            .multiply(factor)
            .multiply(BigDecimal.valueOf(Math.random()))
            .divide(BigDecimal.valueOf(restCount), 2, BigDecimal.ROUND_DOWN);
    // 确保每个人获得的最小金额是0.01
    if (random.compareTo(min) < 0) {
      random = min;
    }
    // 本次发放后剩余的红包数量
    BigDecimal nextRestCount = BigDecimal.valueOf(restCount - 1);
    BigDecimal even = restFee.subtract(random).divide(nextRestCount, 2, BigDecimal.ROUND_DOWN);
    // 确保下次发放每个人最小分到0.01，否则需要重新计算
    if (even.compareTo(min) < 0) {
      random = restFee.subtract(min.multiply(nextRestCount));
    }
    return random;
  }

  public static void main(String[] args) {
    BigDecimal totalFee = BigDecimal.ZERO;
    int restCount = 10;
    int count = restCount + 1;
    BigDecimal restFee = BigDecimal.valueOf(50);
    System.out.println("发放总金额:restFee=" + restFee);
    System.out.println("发放红包数为:restCount=" + restCount);
    while (restCount > 0) {
      BigDecimal current = gainFee(restFee, restCount);
      System.out.println("第" + (count - restCount) + "次领取的金额为:current=" + current);
      totalFee = totalFee.add(current);
      restCount--;
      restFee = restFee.subtract(current);
    }
    System.out.println("一共领取的总金额:totalFee=" + totalFee);
  }
}
