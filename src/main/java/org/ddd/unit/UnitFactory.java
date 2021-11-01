package org.ddd.unit;

/**
 * @author chenjx
 */
public interface UnitFactory {
    MeasurementUnit getUnit(String unitSymbol);

    Measurement getPhysicalQuantity(String type);
}
