package org.ddd.unit;

import java.util.Objects;

/**
 * 带有幂数的单位
 *
 * @author chenjx
 * @see AtomicUnit
 */
public class UnitWithPower implements MeasurementUnit {
    private MeasurementUnit originUnit;
    private int power;

    private UnitWithPower(MeasurementUnit originUnit, int power) {
        this.originUnit = originUnit;
        this.power = power;
    }

    public static UnitWithPower ofPositiveOne(MeasurementUnit originUnit) {
        return new UnitWithPower(originUnit, 1);
    }

    public static UnitWithPower ofNegativeOne(MeasurementUnit originUnit) {
        return new UnitWithPower(originUnit, -1);
    }

    public static UnitWithPower of(MeasurementUnit originUnit, int power) {
        return new UnitWithPower(originUnit, power);
    }

    @Override
    public String alias() {
        return originUnit.alias();
    }

    @Override
    public String symbol() {
        if (power != 1) {
            return originUnit.symbol() + "^" + power;
        }
        return originUnit.symbol();
    }

    @Override
    public Measurement type() {
        return originUnit.type();
    }

    @Override
    public boolean isSameTypeFor(MeasurementUnit targetUnit) {
        return this.originUnit.isSameTypeFor(targetUnit);
    }

    @Override
    public ConversionRate convertTo(MeasurementUnit target) {
        ConversionRate conversionRate = originUnit.convertTo(target);

        Ratio ratio = conversionRate.getRatio();
        Ratio powRatio = ratio.pow(power);

        return new ConversionRate(this, new UnitWithPower(target, power), powRatio);
    }

    @Override
    public ConversionRate convertTo(String targetSymbol) {
        ConversionRate conversionRate = originUnit.convertTo(targetSymbol);

        Ratio ratio = conversionRate.getRatio();
        Ratio powRatio = ratio.pow(power);

        return new ConversionRate(this, new UnitWithPower(conversionRate.denominatorUnit(), power), powRatio);
    }

    public MeasurementUnit getOriginUnit() {
        return originUnit;
    }

    public int getPower() {
        return power;
    }
}
