package org.ddd.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author chenjx
 */
public class NumberUtil {
    public static int compare(Number n1, Number n2) {
        BigDecimal v1 = new BigDecimal(n1.toString());
        BigDecimal v2 = new BigDecimal(n2.toString());
        return v1.compareTo(v2);
    }

    public static Number add(Number n1, Number n2) {
        BigDecimal v1 = new BigDecimal(n1.toString());
        BigDecimal v2 = new BigDecimal(n2.toString());
        return v1.add(v2);
    }

    public static Number subtract(Number n1, Number n2) {
        BigDecimal v1 = new BigDecimal(n1.toString());
        BigDecimal v2 = new BigDecimal(n2.toString());
        return v1.subtract(v2);
    }

    public static Number multiply(Number n1, Number n2) {
        BigDecimal v1 = new BigDecimal(n1.toString());
        BigDecimal v2 = new BigDecimal(n2.toString());
        return v1.multiply(v2);
    }

    public static Number divide(Number n1, Number n2) {
        BigDecimal v1 = new BigDecimal(n1.toString());
        BigDecimal v2 = new BigDecimal(n2.toString());
        return v1.divide(v2, 2, RoundingMode.HALF_UP);
    }

}
