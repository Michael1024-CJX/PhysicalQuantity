package org.ddd.unit;

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
        Unit unit = unitFactory.getUnit(symbol);
        if (unit == null) {
            throw new SymbolNotFoundException("无效的单位符号");
        }
        return PhysicalQuantity.of(value, unit);
    }
}
