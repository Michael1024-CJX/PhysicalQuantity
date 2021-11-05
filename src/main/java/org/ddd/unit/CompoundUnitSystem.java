package org.ddd.unit;

import java.util.List;

/**
 * @author chenjx
 */
public class CompoundUnitSystem implements UnitSystem {
    @Override
    public Measurement type() {
        return null;
    }

    @Override
    public List<Unit> allUnits() {
        return null;
    }

    @Override
    public Unit getUnit(UnitSymbol symbol) {
        return null;
    }

    @Override
    public ConversionRate getConversionRate(Unit from, Unit to) {
        return null;
    }
}
