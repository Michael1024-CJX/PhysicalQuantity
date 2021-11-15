package org.ddd.unit;

import java.util.*;

/**
 * @author chenjx
 */
public class BasicUnit implements Unit {
    /**
     * 单位的符号，具有唯一性，即写作
     */
    private UnitSymbol symbol;
    /**
     * 单位制，如长度，质量，时间等
     */
    private UnitSystem system;

    public BasicUnit(UnitSymbol symbol, UnitSystem system) {
        this.symbol = symbol;
        this.system = system;
    }

    @Override
    public UnitSymbol getSymbol() {
        return symbol;
    }

    @Override
    public boolean isSameTypeAs(UnitSymbol symbol) {
        return system.containsUnit(symbol);
    }

    @Override
    public ConversionRate convertTo(UnitSymbol target) {
        if (isSameTypeAs(symbol)) {
            return system.getConversionRate(this.symbol, target);
        }
        return null;
    }

    @Override
    public ConversionRate adaptTo(UnitSymbol target) {
        List<UnitSymbol> singleSymbol = target.getBasicSymbols();
        for (UnitSymbol unitSymbol : singleSymbol) {
            UnitSymbol base = unitSymbol.base();
            if (system.containsUnit(base)) {
                return system.getConversionRate(this.symbol, base);
            }
        }
        return new ConversionRate(this, this, Ratio.ONE_RATIO);
    }

    @Override
    public Unit opposite() {
        return PowerUnit.ofNegative(this);
    }

    @Override
    public String toString() {
        return symbol.symbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicUnit basicUnit = (BasicUnit) o;
        return Objects.equals(symbol, basicUnit.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
