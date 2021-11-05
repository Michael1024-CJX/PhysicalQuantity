package org.ddd.unit;

import java.util.List;

/**
 * 原子单位制
 *
 * @author chenjx
 */
public class AtomicUnitSystem implements UnitSystem {
    /**
     * 度量
     */
    private Measurement measurement;
    /**
     * 属于该物理量的单位容器
     */
    private UnitContainer unitContainer;

    public AtomicUnitSystem(Measurement measurement, UnitContainer unitContainer) {
        this.measurement = measurement;
        this.unitContainer = unitContainer;
    }

    public Unit registerUnit(String symbol, String alias) {
        AtomicUnit atomicUnit = new AtomicUnit(symbol, alias);
        atomicUnit.setMeasurement(measurement);
        unitContainer.registerUnit(atomicUnit);
        return atomicUnit;
    }

    public void registerConversionRate(String numeratorUnit, String denominatorUnit, Ratio ratio) {
        unitContainer.registerConversionRate(numeratorUnit, denominatorUnit, ratio);
    }


    @Override
    public Measurement type() {
        return measurement;
    }

    @Override
    public List<Unit> allUnits() {
        return unitContainer.allUnits();
    }

    @Override
    public Unit getUnit(UnitSymbol symbol) {
        return unitContainer.getUnit(symbol);
    }

    @Override
    public ConversionRate getConversionRate(Unit from, Unit to) {
        Ratio ratio = unitContainer.calculateRatio(from, to);
        if (ratio == null) {
            return null;
        }
        return new ConversionRate(from, to, ratio);
    }
}
