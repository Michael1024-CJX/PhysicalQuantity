package org.ddd.unit;

import java.util.List;

/**
 * @author chenjx
 */
public class PowerUnitSystem implements UnitSystem {
    private UnitSystem originUnitSystem;

    public PowerUnitSystem(UnitSystem originUnitSystem) {
        this.originUnitSystem = originUnitSystem;
    }

    @Override
    public Measurement type() {
        return originUnitSystem.type();
    }

    @Override
    public List<Unit> allUnits() {
        return originUnitSystem.allUnits();
    }

    @Override
    public Unit getUnit(UnitSymbol symbol) {
        Unit unit = originUnitSystem.getUnit(symbol);
        if (unit != null) {
            return unit;
        }
        return originUnitSystem.getUnit(symbol.removePower());
    }

    @Override
    public ConversionRate getConversionRate(Unit from, Unit to) {
        return null;
    }
}
