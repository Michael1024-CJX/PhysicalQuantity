package org.ddd.unit;

/**
 * @author chenjx
 */
public class PowerUnitSystem extends AbstractUnitSystem implements UnitSystem {
    private UnitSystem originUnitSystem;
    private int power;

    public PowerUnitSystem(UnitSystem originUnitSystem, int power, Measurement measurement) {
        super(measurement);
        assert power != 0;
        this.originUnitSystem = originUnitSystem;
        this.power = power;
    }

    @Override
    public boolean containsUnit(UnitSymbol symbol) {
        return originUnitSystem.containsUnit(symbol.base());
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
        return new Unit(symbol, this, newAlias(unit));
    }

    private String newAlias(Unit unit) {
        if (power > 0) {
            return power == 1? unit.alias() : power + "次方" + unit.alias();
        }else {
            return "每" + power + "次方" + unit.alias();
        }
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
