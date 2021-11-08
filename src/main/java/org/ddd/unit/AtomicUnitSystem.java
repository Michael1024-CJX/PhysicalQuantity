package org.ddd.unit;

import java.util.ArrayList;
import java.util.List;

/**
 * 原子单位制
 *
 * @author chenjx
 */
public class AtomicUnitSystem extends AbstractUnitSystem implements UnitSystem {
    /**
     * 属于该物理量的单位容器
     */
    private UnitContainer unitContainer;

    public AtomicUnitSystem(UnitContainer unitContainer) {
        this.unitContainer = unitContainer;
    }

    public Unit registerUnit(String symbol) {
        Unit atomicUnit = new Unit(UnitSymbol.of(symbol), this);
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
    public UnitSymbol adapt(UnitSymbol from, UnitSymbol target) {
        List<UnitSymbol> targetSingleSymbol = target.splitIntoSingleSymbol();

        ArrayList<UnitSymbol> result = new ArrayList<>();
        for (UnitSymbol symbol : targetSingleSymbol) {
            if (containsUnit(symbol.base())) {
                result.add(symbol.base());
            }
        }
        return result.stream().reduce(UnitSymbol::appendWith).orElse(from);
    }

    @Override
    Unit doGet(UnitSymbol symbol) {
        return unitContainer.getUnit(symbol);
    }

    @Override
    ConversionRate doGetConversionRate(UnitSymbol from, UnitSymbol to) {
        Ratio ratio = unitContainer.calculateRatio(from, to);
        if (ratio == null) {
            return null;
        }
        return new ConversionRate(getUnit(from), getUnit(to), ratio);
    }
}
