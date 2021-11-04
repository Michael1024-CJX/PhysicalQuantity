package org.ddd.unit;

/**
 * 带有幂数的单位
 *
 * @author chenjx
 * @see AtomicUnit
 */
public class UnitWithPower implements Unit {
    private Unit originUnit;
    private int power;
    private String symbol;

    private UnitWithPower(Unit originUnit, int power) {
        if (originUnit instanceof UnitWithPower) {
            power *= ((UnitWithPower) originUnit).power;
            originUnit = ((UnitWithPower) originUnit).originUnit;
        }
        this.originUnit = originUnit;
        this.power = power;

        this.symbol = power == 1 ? this.originUnit.symbol() : this.originUnit.symbol() + "^" + power;
    }

    public static UnitWithPower ofPositiveOne(Unit originUnit) {
        return UnitWithPower.of(originUnit, 1);
    }

    public static UnitWithPower ofNegativeOne(Unit originUnit) {
        return UnitWithPower.of(originUnit, -1);
    }

    public static UnitWithPower of(Unit originUnit, int power) {
        return new UnitWithPower(originUnit, power);
    }

    @Override
    public String alias() {
        return originUnit.alias();
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public Measurement type() {
        return originUnit.type();
    }

    @Override
    public void setMeasurement(Measurement measurement) {

    }

    @Override
    public boolean isSameTypeFor(Unit targetUnit) {
        return this.originUnit.isSameTypeFor(targetUnit);
    }

    @Override
    public ConversionRate convertTo(Unit target) {
        ConversionRate conversionRate = originUnit.convertTo(target);

        Ratio ratio = conversionRate.getRatio();
        Ratio powRatio = ratio.pow(power);

        return new ConversionRate(this, new UnitWithPower(target, power), powRatio);
    }

    public Unit getOriginUnit() {
        return originUnit;
    }

    public int getPower() {
        return power;
    }

    @Override
    public String toString() {
        return originUnit.symbol();
    }
}
