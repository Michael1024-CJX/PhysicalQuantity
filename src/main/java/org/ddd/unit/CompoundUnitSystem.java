package org.ddd.unit;

/**
 * @author chenjx
 */
public class CompoundUnitSystem extends AbstractUnitSystem implements UnitSystem {
    private UnitSystem[] systems;

    public CompoundUnitSystem(UnitSystem[] systems, Measurement type) {
        super(type);
        this.systems = systems;
    }

    @Override
    public boolean containsUnit(UnitSymbol symbol) {
        UnitSymbol[] atomicUnits = symbol.split();
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
        UnitSymbol[] fromUnits = from.split();
        UnitSymbol[] toUnits = to.split();
        Ratio ratio = Ratio.ONE_RATIO;

        for (int i = 0; i < fromUnits.length; i++) {
            UnitSymbol fromAtomicUnit = fromUnits[i];
            UnitSymbol toAtomicUnit = toUnits[i];
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
