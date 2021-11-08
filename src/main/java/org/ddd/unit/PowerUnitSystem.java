package org.ddd.unit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenjx
 */
public class PowerUnitSystem extends AbstractUnitSystem implements UnitSystem {
    private UnitSystem originUnitSystem;
    private int power;

    public PowerUnitSystem(UnitSystem originUnitSystem, int power) {
        this.originUnitSystem = originUnitSystem;
        this.power = power;
    }

    @Override
    public boolean containsUnit(UnitSymbol symbol) {
        return originUnitSystem.containsUnit(symbol.base());
    }

    @Override
    public UnitSymbol adapt(UnitSymbol from, UnitSymbol target) {
        return originUnitSystem.adapt(from, target);
    }

    @Override
    Unit doGet(UnitSymbol symbol) {
        if (symbol.index() != power) {
            return null;
        }
        Unit unit = originUnitSystem.getUnit(symbol.base());
        if (unit == null) {
            return null;
        }
        return new Unit(symbol, this);
    }

    @Override
    ConversionRate doGetConversionRate(UnitSymbol from, UnitSymbol to) {
        if (from.index() != power || to.index() != power) {
            return null;
        }
        ConversionRate atomicRate = originUnitSystem.getConversionRate(from.base(), to.base());
        if (atomicRate == null) {
            return null;
        }
        Ratio atomicRatio = atomicRate.getRatio();
        return new ConversionRate(getUnit(from), getUnit(to), atomicRatio.pow(power));
    }
}
