package org.ddd.unit;

import java.util.Collection;

/**
 * @author chenjx
 */
public interface UnitRegister {
    Collection<String> getAllPhysicalQuantity();

    Collection<UnitDefinition> getAllUnitDefinition();

    Collection<ConversionRateDefinition> getAllConversionRateDefinition();
}
