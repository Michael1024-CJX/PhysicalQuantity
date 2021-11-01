package org.ddd.quantity;

import org.ddd.unit.CompoundUnit;
import org.ddd.unit.MeasurementUnit;
import org.ddd.unit.UnitFactory;

/**
 *
 *
 * @author chenjx
 */
public class QuantityFactory {
    private UnitFactory unitFactory;

    public QuantityFactory(UnitFactory unitFactory) {
        this.unitFactory = unitFactory;
    }

    public PhysicalQuantity of(Number value, String symbol){
        MeasurementUnit unit = unitFactory.getUnit(symbol);
        if (unit == null) {
            throw new IllegalArgumentException("无效的单位符号");
        }
        return PhysicalQuantity.of(value, unit);
    }
}
