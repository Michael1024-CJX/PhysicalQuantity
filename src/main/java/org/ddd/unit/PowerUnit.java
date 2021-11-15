package org.ddd.unit;

import java.util.Iterator;
import java.util.Objects;

/**
 * @author chenjx
 */
public class PowerUnit implements Unit {
    /**
     * 底数
     */
    private Unit base;
    /**
     * 幂 1 or -1
     */
    private int index;

    private UnitSymbol symbol;

    private PowerUnit(Unit base, int index) {
        this.base = base;
        this.index = index;
        this.symbol = base.getSymbol().power(index);
    }

    public static PowerUnit ofPositive(Unit base) {
        return new PowerUnit(base, 1);
    }

    public static PowerUnit ofNegative(Unit base) {
        return new PowerUnit(base, -1);
    }

    @Override
    public UnitSymbol getSymbol() {
        return symbol;
    }

    @Override
    public boolean isSameTypeAs(UnitSymbol symbol) {
        int index = symbol.index();

        if (this.index != index) {
            return false;
        }

        UnitSymbol baseSymbol = symbol.base();
        return base.isSameTypeAs(baseSymbol);
    }

    @Override
    public ConversionRate convertTo(UnitSymbol target) {
        if (!isSameTypeAs(target)) {
            return null;
        }
        if (index == 1) {
            return base.convertTo(target);
        }

        ConversionRate baseRate = base.convertTo(target.base());
        Unit denominatorUnit = baseRate.denominatorUnit();
        PowerUnit denominatorPowerUnit = new PowerUnit(denominatorUnit, index);
        return new ConversionRate(this, denominatorPowerUnit, baseRate.getRatio().pow(index));
    }

    @Override
    public ConversionRate adaptTo(UnitSymbol target) {
        if (index == 1) {
            return base.adaptTo(target);
        }
        ConversionRate baseRate = base.adaptTo(target);
        Unit denominatorUnit = baseRate.denominatorUnit();
        PowerUnit denominatorPowerUnit = new PowerUnit(denominatorUnit, index);
        return new ConversionRate(this, denominatorPowerUnit, baseRate.getRatio().pow(index));
    }

    @Override
    public Iterator<Unit> iterator() {
        return null;
    }

    @Override
    public Unit opposite() {
        return new PowerUnit(base, -index);
    }

    @Override
    public String toString() {
        return symbol.symbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerUnit powerUnit = (PowerUnit) o;
        return Objects.equals(symbol, powerUnit.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
