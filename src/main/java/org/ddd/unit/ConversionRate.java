package org.ddd.unit;

import java.math.BigDecimal;

/**
 * 转换率
 *
 * @author chenjx
 */
public class ConversionRate {
    private Unit numeratorUnit;
    private Unit denominatorUnit;
    private Ratio ratio;

    public ConversionRate(Unit numeratorUnit, Unit denominatorUnit, Ratio ratio) {
        this.numeratorUnit = numeratorUnit;
        this.denominatorUnit = denominatorUnit;
        this.ratio = ratio;
    }

    public Unit numeratorUnit() {
        return numeratorUnit;
    }

    public Unit denominatorUnit() {
        return denominatorUnit;
    }

    public Ratio getRatio() {
        return ratio;
    }

    public ConversionRate reverse() {
        return new ConversionRate(denominatorUnit, numeratorUnit, ratio.reciprocal());
    }
//
//    public PhysicalQuantity apply(PhysicalQuantity quantity) {
//        Ratio times = ratio.times(new BigDecimal(quantity.getAmount().toString()));
//        return PhysicalQuantity.of(times.decimalValue(2, BigDecimal.ROUND_HALF_UP), denominatorUnit);
//    }

    @Override
    public String toString() {
        return  numeratorUnit + ":" + denominatorUnit + "=" + ratio;
    }
}
