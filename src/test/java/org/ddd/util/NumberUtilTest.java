package org.ddd.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class NumberUtilTest {

    @Test
    public void compare() {
        // 测试整数
        assertEquals(0, NumberUtil.compare(100, 100));
        assertEquals(1, NumberUtil.compare(101, 100));
        assertEquals(-1, NumberUtil.compare(99, 100));

        // 测试浮点数
        assertEquals(0, NumberUtil.compare(0.9999d, 0.9999d));
        assertEquals(1, NumberUtil.compare(0.9999d, 0.9998d));
        assertEquals(-1, NumberUtil.compare(0.9998d, 0.9999d));

        // 测试负浮点数
        assertEquals(0, NumberUtil.compare(-0.9999d, -0.9999d));
        assertEquals(1, NumberUtil.compare(-0.9998d, -0.9999d));
        assertEquals(-1, NumberUtil.compare(-0.9999d, -0.9998d));
    }

    @Test
    public void add() {
        assertEquals(0, NumberUtil.add(100, 100).compareTo(BigDecimal.valueOf(200)));
        assertEquals(0, NumberUtil.add(99.1, 0.9).compareTo(BigDecimal.valueOf(100)));
        assertEquals(0, NumberUtil.add(-233, 33).compareTo(BigDecimal.valueOf(-200)));
    }

    @Test
    public void subtract() {
        assertEquals(0, NumberUtil.subtract(100, 100).compareTo(BigDecimal.valueOf(0)));
        assertEquals(0, NumberUtil.subtract(99.1, 0.9).compareTo(BigDecimal.valueOf(98.2)));
        assertEquals(0, NumberUtil.subtract(-233, 33).compareTo(BigDecimal.valueOf(-266)));
    }

    @Test
    public void multiply() {
        assertEquals(0, NumberUtil.multiply(20, 10).compareTo(BigDecimal.valueOf(200)));
        assertEquals(0, NumberUtil.multiply(10.1, 2.2).compareTo(BigDecimal.valueOf(22.22)));
        assertEquals(0, NumberUtil.multiply(-23, 10).compareTo(BigDecimal.valueOf(-230)));
    }

    @Test
    public void divide() {
    }
}