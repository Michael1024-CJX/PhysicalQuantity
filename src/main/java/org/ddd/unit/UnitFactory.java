package org.ddd.unit;

/**
 * @author chenjx
 */
public interface UnitFactory {
    void refresh(UnitRegister register);

    default Unit getUnit(String unitSymbol) {
        return getUnit(UnitSymbol.of(unitSymbol));
    }

    Unit getUnit(UnitSymbol unitSymbol);

    Measurement getMeasurement(UnitSymbol unitSymbol);
}
