package org.ddd.unit;

import java.util.Objects;

/**
 * 原子单位，如 米, 厘米, 千克, 克等。
 * 原子单位知道自己的量度{@link Measurement},能够进行单位转换。
 *
 * @author chenjx
 */
public class AtomicUnit implements Unit {
    /**
     * 单位符号
     */
    private String symbol;
    /**
     * 单位的别名
     */
    private String alias;
    /**
     * 单位的量度
     */
    private Measurement type;

    public AtomicUnit(String unitSymbol, String alias) {
        this.symbol = unitSymbol;
        this.alias = alias;
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
    public void setMeasurement(Measurement measurement) {
        this.type = measurement;
    }

    @Override
    public boolean isSameTypeFor(Unit unit) {
        if (unit.type() != null) {
            return Objects.equals(type, unit.type());
        }

        return type.containsUnit(unit);
    }

    @Override
    public ConversionRate convertTo(Unit target) {
        Objects.requireNonNull(target, "目标单位为空");

        if (!isSameTypeFor(target)) {
            return null;
        }

        return type.getConversionRate(this, target);
    }

    @Override
    public String toString() {
        return symbol();
    }
}
