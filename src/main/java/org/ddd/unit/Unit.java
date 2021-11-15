package org.ddd.unit;

import java.util.Iterator;

/**
 * @author chenjx
 */
public interface Unit {
    UnitSymbol getSymbol();

    boolean isSameTypeAs(UnitSymbol symbol);

    ConversionRate convertTo(UnitSymbol target);

    ConversionRate adaptTo(UnitSymbol target);

    Unit opposite();

    default Iterator<Unit> iterator() {
        return null;
    }

    default boolean isEquals(Unit another) {
        return another.getSymbol().equals(getSymbol());
    }
}
