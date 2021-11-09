package org.ddd.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author chenjx
 */
public class NumberUtil {
    /**
     * 四舍五入，保留两位小数
     */
    public static final MathContext DEFAULT = MathContext.DECIMAL32;

    public static int compare(Number n1, Number n2) {
        BigDecimal v1 = new BigDecimal(n1.toString());
        BigDecimal v2 = new BigDecimal(n2.toString());
        return v1.compareTo(v2);
    }

    public static BigDecimal add(Number n1, Number n2) {
        BigDecimal v1 = new BigDecimal(n1.toString());
        BigDecimal v2 = new BigDecimal(n2.toString());
        return v1.add(v2);
    }

    public static BigDecimal subtract(Number n1, Number n2) {
        BigDecimal v1 = new BigDecimal(n1.toString());
        BigDecimal v2 = new BigDecimal(n2.toString());
        return v1.subtract(v2);
    }

    public static BigDecimal multiply(Number n1, Number n2) {
        return multiply(n1, n2, DEFAULT);
    }

    public static BigDecimal multiply(Number n1, Number n2, MathContext mc) {
        BigDecimal v1 = new BigDecimal(n1.toString());
        BigDecimal v2 = new BigDecimal(n2.toString());
        return v1.multiply(v2, mc);
    }

    public static BigDecimal divide(Number n1, Number n2) {
        return divide(n1, n2, DEFAULT);
    }

    public static BigDecimal divide(Number n1, Number n2, MathContext mc) {
        BigDecimal v1 = new BigDecimal(n1.toString());
        BigDecimal v2 = new BigDecimal(n2.toString());
        return v1.divide(v2, mc);
    }

}
