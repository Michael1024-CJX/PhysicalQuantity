package org.ddd.unit;

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

    public AtomicUnitSystem(Measurement measurement, UnitContainer unitContainer) {
        super(measurement);
        this.unitContainer = unitContainer;
    }

    public Unit registerUnit(String symbol, String alias) {
        Unit atomicUnit = new Unit(UnitSymbol.of(symbol), this, alias);
        unitContainer.registerUnit(atomicUnit);
        return atomicUnit;
    }

    public void registerConversionRate(String numeratorUnit, String denominatorUnit, Ratio ratio) {
        unitContainer.registerConversionRate(UnitSymbol.of(numeratorUnit), UnitSymbol.of(denominatorUnit), ratio);
    }

    @Override
    public boolean containsUnit(UnitSymbol symbol) {
        return unitContainer.contains(symbol);
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
