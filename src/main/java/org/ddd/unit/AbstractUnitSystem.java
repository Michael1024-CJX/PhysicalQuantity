package org.ddd.unit;

/**
 * @author chenjx
 */
public abstract class AbstractUnitSystem implements UnitSystem {
//    private Measurement type;
//
//    AbstractUnitSystem(Measurement type) {
//        this.type = type;
//    }
//
//    @Override
//    public Measurement type() {
//        return type;
//    }

    @Override
    public Unit getUnit(UnitSymbol symbol) {
        if (containsUnit(symbol)) {
            return doGet(symbol);
        }
        return null;
    }

    abstract Unit doGet(UnitSymbol symbol);

    @Override
    public ConversionRate getConversionRate(UnitSymbol from, UnitSymbol to) {
        if (containsUnit(from) && containsUnit(to)) {
            return doGetConversionRate(from, to);
        }
        return null;
    }

    abstract ConversionRate doGetConversionRate(UnitSymbol from, UnitSymbol to);
}
