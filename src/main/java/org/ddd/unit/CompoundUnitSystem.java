package org.ddd.unit;

import java.util.List;

/**
 * @author chenjx
 */
public class CompoundUnitSystem extends AbstractUnitSystem implements UnitSystem {
    private List<UnitSystem> systems;

    public CompoundUnitSystem(List<UnitSystem> systems, Measurement type) {
        super(type);
        this.systems = systems;
    }

    @Override
    public boolean containsUnit(UnitSymbol symbol) {
        List<UnitSymbol> atomicUnits = symbol.split();
        for (UnitSymbol atomicUnit : atomicUnits) {
            if (!contains(atomicUnit)) {
                return false;
            }
        }
        return true;
    }

    @Override
    Unit doGet(UnitSymbol symbol) {
        if (!containsUnit(symbol)) {
            return null;
        }
        return new Unit(symbol, this, "");
    }

    @Override
    ConversionRate doGetConversionRate(UnitSymbol from, UnitSymbol to) {
        List<UnitSymbol> fromUnits = from.split();
        List<UnitSymbol> toUnits = to.split();
        if (fromUnits.size() != toUnits.size()) {
            return null;
        }

        Ratio ratio = Ratio.ONE_RATIO;
        for (int i = 0; i < fromUnits.size(); i++) {
            UnitSymbol fromAtomicUnit = fromUnits.get(i);
            UnitSymbol toAtomicUnit = toUnits.get(i);
            UnitSystem unitSystem = getUnitSystem(fromAtomicUnit);
            ConversionRate conversionRate = unitSystem.getConversionRate(fromAtomicUnit, toAtomicUnit);
            if (conversionRate == null) {
                return null;
            }
            ratio = ratio.times(conversionRate.getRatio());
        }
        return new ConversionRate(getUnit(from), getUnit(to), ratio);
    }

    private boolean contains(UnitSymbol atomicSymbol) {
        for (UnitSystem system : systems) {
            if (system.containsUnit(atomicSymbol)) {
                return true;
            }
        }
        return false;
    }

    private UnitSystem getUnitSystem(UnitSymbol atomicSymbol) {
        for (UnitSystem system : systems) {
            if (system.containsUnit(atomicSymbol)) {
                return system;
            }
        }
        throw new SymbolNotFoundException();
    }
}
