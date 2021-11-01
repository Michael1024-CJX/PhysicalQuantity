package org.ddd.unit;

import java.util.Objects;

/**
 * 原子单位，如 米, 厘米, 千克, 克等。
 * 同物理量的单位之间可以互相转换。
 *
 * @author chenjx
 */
public class AtomicUnit implements MeasurementUnit {
    /**
     * 单位符号
     */
    private String symbol;
    /**
     * 单位的别名
     */
    private String alias;
    /**
     * 单位的物理量
     */
    private Measurement type;

    public AtomicUnit(String unitSymbol, String alias, Measurement type) {
        this.symbol = unitSymbol;
        this.alias = alias;
        this.type = type;
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public Measurement type() {
        return this.type;
    }

    @Override
    public boolean isSameTypeFor(MeasurementUnit measurementUnit) {
        if (measurementUnit.type() != null) {
            return Objects.equals(type, measurementUnit.type());
        }

        return type.containsUnit(measurementUnit);
    }

    @Override
    public ConversionRate convertTo(MeasurementUnit target) {
        Objects.requireNonNull(target, "目标单位为空");

        if (!isSameTypeFor(target)) {
            return null;
        }

        return type.getConversionRate(this, target);
    }

    @Override
    public ConversionRate convertTo(String targetSymbol) {
        MeasurementUnit unit = type.getUnit(targetSymbol);
        if (unit != null) {
            return this.convertTo(unit);
        }
        return null;
    }

    @Override
    public String toString() {
        return symbol();
    }
}
