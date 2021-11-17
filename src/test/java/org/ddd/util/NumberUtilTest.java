package org.ddd.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class NumberUtilTest {

    @Test
    public void zeroNumberCompareTest() {
        assertEquals(0, NumberUtil.compare(0, 0));
    }

    @Test
    public void positiveNumberCompareTest() {
        // 测试整数
        assertEquals(0, NumberUtil.compare(100, 100));
        assertEquals(1, NumberUtil.compare(101, 100));
        assertEquals(-1, NumberUtil.compare(100, 101));

        // 测试浮点数
        assertEquals(0, NumberUtil.compare(0.9999d, 0.9999d));
        assertEquals(1, NumberUtil.compare(0.9999d, 0.9998d));
        assertEquals(-1, NumberUtil.compare(0.9998d, 0.9999d));

    }

    @Test
    public void negativeNumberCompareTest() {
        assertEquals(0, NumberUtil.compare(-100, -100));
        assertEquals(-1, NumberUtil.compare(-101, -100));
        assertEquals(1, NumberUtil.compare(-100, -101));

        assertEquals(0, NumberUtil.compare(-0.9999d, -0.9999d));
        assertEquals(1, NumberUtil.compare(-0.9998d, -0.9999d));
        assertEquals(-1, NumberUtil.compare(-0.9999d, -0.9998d));
    }

    @Test
    public void positiveNumberAddTest() {
        BigDecimal add = NumberUtil.add(100, 100);
        BigDecimal exp = BigDecimal.valueOf(200);
        assertEquals(0, exp.compareTo(add));

        BigDecimal floatAct = NumberUtil.add(99.1, 0.9);
        BigDecimal floatExp = BigDecimal.valueOf(100);
        assertEquals(0, floatExp.compareTo(floatAct));
    }

    @Test
    public void negativeNumberAddTest() {
        BigDecimal add = NumberUtil.add(-167, -33);
        BigDecimal exp = BigDecimal.valueOf(-200);
        assertEquals(0, exp.compareTo(add));

        BigDecimal floatAct = NumberUtil.add(-99.9, -0.1);
        BigDecimal floatExp = BigDecimal.valueOf(-100);
        assertEquals(0, floatExp.compareTo(floatAct));
    }

    @Test
    public void positiveAndNegativeNumberAddTest() {
        BigDecimal add = NumberUtil.add(-233, 33);
        BigDecimal exp = BigDecimal.valueOf(-200);
        assertEquals(0, exp.compareTo(add));

        BigDecimal floatAct = NumberUtil.add(99.8, -0.8);
        BigDecimal floatExp = BigDecimal.valueOf(99);
        assertEquals(0, floatExp.compareTo(floatAct));
    }

    @Test
    public void subtractPositiveTest() {
        assertEquals(0, NumberUtil.subtract(100, 100).compareTo(BigDecimal.valueOf(0)));

        assertEquals(0, NumberUtil.subtract(10, 100).compareTo(BigDecimal.valueOf(-90)));

        assertEquals(0, NumberUtil.subtract(3, -0.14).compareTo(BigDecimal.valueOf(3.14)));
    }

    @Test
    public void subtractNegativeTest() {
        assertEquals(0, NumberUtil.subtract(-100, -100).compareTo(BigDecimal.valueOf(0)));

        assertEquals(0, NumberUtil.subtract(-10, -100).compareTo(BigDecimal.valueOf(90)));

        assertEquals(0, NumberUtil.subtract(-3, 0.14).compareTo(BigDecimal.valueOf(-3.14)));
    }

    @Test
    public void multiplyPositiveTest() {
        assertEquals(0, NumberUtil.multiply(20, 10).compareTo(BigDecimal.valueOf(200)));
        assertEquals(0, NumberUtil.multiply(10.1, 2.2).compareTo(BigDecimal.valueOf(22.22)));
    }

    @Test
    public void multiplyNegativeTest() {
        assertEquals(0, NumberUtil.multiply(-20, -10).compareTo(BigDecimal.valueOf(200)));
        assertEquals(0, NumberUtil.multiply(-10.1, -2.2).compareTo(BigDecimal.valueOf(22.22)));
        assertEquals(0, NumberUtil.multiply(-23, 10).compareTo(BigDecimal.valueOf(-230)));
    }

    @Test
    public void dividePositiveTest() {
        assertEquals(0, NumberUtil.divide(20, 10).compareTo(BigDecimal.valueOf(2)));
        assertEquals(0, NumberUtil.divide(10, 20).compareTo(BigDecimal.valueOf(0.5)));
    }

    @Test
    public void divideNegativeTest() {
        assertEquals(0, NumberUtil.divide(-20, -10).compareTo(BigDecimal.valueOf(2)));
        assertEquals(0, NumberUtil.divide(-10, -20).compareTo(BigDecimal.valueOf(0.5)));
        assertEquals(0, NumberUtil.divide(-10, 20).compareTo(BigDecimal.valueOf(-0.5)));
    }
}