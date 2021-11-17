package org.ddd.unit;

import java.util.ArrayList;
import java.util.List;

/**
 * 原子单位制
 *
 * @author chenjx
 */
public class BasicUnitSystem implements UnitSystem {
    /**
     * 属于该物理量的单位容器
     */
    private UnitContainer unitContainer;

    public BasicUnitSystem(UnitContainer unitContainer) {
        this.unitContainer = unitContainer;
    }

    public Unit registerUnit(String symbol) {
        Unit atomicUnit = new BasicUnit(UnitSymbol.of(symbol), this);
        unitContainer.addUnit(atomicUnit);
        return atomicUnit;
    }

    public void registerConversionRate(String numeratorUnit, String denominatorUnit, Ratio ratio) {
        unitContainer.addConversionRate(UnitSymbol.of(numeratorUnit), UnitSymbol.of(denominatorUnit), ratio);
    }

    @Override
    public boolean containsUnit(UnitSymbol symbol) {
        return unitContainer.contains(symbol);
    }

    @Override
    public Unit getUnit(UnitSymbol symbol) {
        return unitContainer.getUnit(symbol);
    }

    @Override
    public ConversionRate getConversionRate(UnitSymbol from, UnitSymbol to) {
        Ratio ratio = unitContainer.calculateRatio(from, to);
        if (ratio == null) {
            return null;
        }
        return new ConversionRate(getUnit(from), getUnit(to), ratio);
    }
}
