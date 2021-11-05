package org.ddd.unit;

/**
 * @author chenjx
 */
public class PowerUnitSystem extends AbstractUnitSystem implements UnitSystem {
    private AtomicUnitSystem originUnitSystem;
    private int power;

    public PowerUnitSystem(AtomicUnitSystem originUnitSystem, int power, Measurement measurement) {
        super(measurement);
        assert power != 0;
        this.originUnitSystem = originUnitSystem;
        this.power = power;
    }

    @Override
    public boolean containsUnit(UnitSymbol symbol) {
        return originUnitSystem.containsUnit(symbol);
    }

    @Override
    Unit doGet(UnitSymbol symbol) {
        if (symbol.power() != power) {
            return null;
        }
        Unit unit = originUnitSystem.getUnit(symbol.atomicSymbol());
        if (unit == null) {
            return null;
        }
        return new Unit(symbol, this, "");
    }

    @Override
    ConversionRate doGetConversionRate(UnitSymbol from, UnitSymbol to) {
        if (from.power() != power || to.power() != power) {
            return null;
        }
        ConversionRate atomicRate = originUnitSystem.getConversionRate(from.atomicSymbol(), to.atomicSymbol());
        if (atomicRate == null) {
            return null;
        }
        Ratio atomicRatio = atomicRate.getRatio();
        return new ConversionRate(getUnit(from), getUnit(to), atomicRatio.pow(power));
    }
}
