package org.ddd.unit;

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

    @Override
    public String toString() {
        return  numeratorUnit + ":" + denominatorUnit + "=" + ratio;
    }
}
