package org.ddd.unit;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenjx
 */
public class DefaultUnitFactory implements UnitFactory {
    private final Map<String, Measurement> physicalQuantities = new ConcurrentHashMap<>();
    private final Map<String, MeasurementUnitContainer> physicalQuantityContainers = new ConcurrentHashMap<>();
    private final Map<String, MeasurementUnit> unitMap = new ConcurrentHashMap<>();

    public DefaultUnitFactory(UnitRegister register) {
        if (register== null) {
            throw new IllegalArgumentException("register is null");
        }

        init(register);
    }

    private void init(UnitRegister register) {
        registerPhysicalQuantity(register);
        registerPhysicalUnit(register);
        registerConversionRate(register);
    }

    private void registerPhysicalQuantity(UnitRegister register) {
        Collection<String> allPhysicalQuantity = register.getAllPhysicalQuantity();

        allPhysicalQuantity.forEach(type -> {
            Measurement measurement = new Measurement(type);
            LinkedMapContainer container = new LinkedMapContainer(measurement);
            measurement.setMeasurementUnitContainer(container);
            physicalQuantities.putIfAbsent(type, measurement);
            physicalQuantityContainers.putIfAbsent(type, container);
        });
    }

    private void registerPhysicalUnit(UnitRegister register) {
        Collection<UnitDefinition> allUnitDefinition = register.getAllUnitDefinition();

        allUnitDefinition.forEach(unitDefinition -> {
            MeasurementUnitContainer container = physicalQuantityContainers.get(unitDefinition.getPhysicalQuantity());
            if (container != null) {
                MeasurementUnit measurementUnit = container.registerUnit(unitDefinition.getSymbol(), unitDefinition.getAlias());
                unitMap.put(unitDefinition.getSymbol(), measurementUnit);
            }
        });
    }

    private void registerConversionRate(UnitRegister register) {
        Collection<ConversionRateDefinition> rates = register.getAllConversionRateDefinition();

        rates.forEach(conversionRate -> {
            MeasurementUnitContainer container = physicalQuantityContainers.get(conversionRate.getPhysicalQuantity());

            if (container != null) {
                container.registerConversionRate(
                        conversionRate.getNumeratorUnit(),
                        conversionRate.getDenominatorUnit(),
                        conversionRate.getRatio()
                );
            }

        });
    }

    @Override
    public MeasurementUnit getUnit(String unitSymbol) {
        return unitMap.get(unitSymbol);
    }

    @Override
    public Measurement getPhysicalQuantity(String type) {
        return physicalQuantities.get(type);
    }

    @Override
    public String toString() {
        return "DefaultUnitFactory{" +
                "physicalQuantities=" + physicalQuantities +
                ",\r\n physicalQuantityContainers=" + physicalQuantityContainers +
                ",\r\n unitMap=" + unitMap +
                '}';
    }
}
