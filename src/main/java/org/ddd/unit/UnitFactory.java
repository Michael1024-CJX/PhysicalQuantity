package org.ddd.unit;

/**
 * @author chenjx
 */
public interface UnitFactory {
    void refresh(UnitRegister register);

    Unit getUnit(String unitSymbol);

    Measurement getPhysicalQuantity(String type);
}
