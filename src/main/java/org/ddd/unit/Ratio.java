package org.ddd.unit;

import java.math.BigDecimal;

/**
 * @author chenjx
 */
@SuppressWarnings("WeakerAccess")
public final class Ratio {
    static final Ratio ONE_RATIO = new Ratio(BigDecimal.ONE, BigDecimal.ONE);
    private BigDecimal numerator;
    private BigDecimal denominator;

    public Ratio(BigDecimal numerator, BigDecimal denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Ratio of(BigDecimal numerator, BigDecimal denominator) {
        return new Ratio(numerator, denominator);
    }

    public BigDecimal decimalValue(int scale, int roundingRule) {
        return numerator.divide(denominator, scale, roundingRule);
    }

    /**
     * 乘一个数，分子乘
     *
     * @param multiplier 乘数
     * @return 结果
     */
    public Ratio times(BigDecimal multiplier) {
        return Ratio.of(numerator.multiply(multiplier), denominator);
    }

    /**
     * 乘一个比值数，分子乘分子，分母乘分母
     *
     * @param multiplier 乘数
     * @return 结果
     */
    public Ratio times(Ratio multiplier) {
        return Ratio.of(numerator.multiply(multiplier.numerator), denominator.multiply(multiplier.denominator));
    }

    /**
     * 幂
     *
     * @param power 幂
     * @return 结果
     */
    public Ratio pow(int power) {
        if (power == 0) {
            return Ratio.of(BigDecimal.ONE, BigDecimal.ONE);
        }

        if (power < 0) {
            return Ratio.of(denominator.pow(-power), numerator.pow(-power));
        }

        return Ratio.of(numerator.pow(power), denominator.pow(power));
    }

    /**
     * 倒数
     *
     * @return 结果
     */
    public Ratio reciprocal() {
        return Ratio.of(denominator, numerator);
    }

    @Override
    public String toString() {
        return numerator.toString() + ":" + denominator;
    }
}
