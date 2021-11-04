package org.ddd.unit;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenjx
 */
public class DefaultUnitFactory implements UnitFactory {
    private final Map<String, Measurement> measurements = new ConcurrentHashMap<>();
    private final Map<String, Unit> unitMap = new ConcurrentHashMap<>();

    private CompoundUnitParser compoundUnitParser = new CompoundUnitParser(this);

    public DefaultUnitFactory(UnitRegister register) {
        if (register == null) {
            throw new IllegalArgumentException("register is null");
        }
        refresh(register);
    }

    public void refresh(UnitRegister register) {
        registerPhysicalQuantity(register);
        registerPhysicalUnit(register);
        registerConversionRate(register);
    }

    private void registerPhysicalQuantity(UnitRegister register) {
        Collection<String> allPhysicalQuantity = register.getAllMeasurements();

        allPhysicalQuantity.forEach(type -> {
            Measurement measurement = new Measurement(type, new LinkedMapContainer());
            measurements.putIfAbsent(type, measurement);
        });
    }

    private void registerPhysicalUnit(UnitRegister register) {
        Collection<UnitDefinition> allUnitDefinition = register.getAllUnitDefinition();

        allUnitDefinition.forEach(unitDefinition -> {
            Measurement measurement = measurements.get(unitDefinition.getMeasurement());
            if (measurement != null) {
                Unit unit = measurement.registerUnit(
                        unitDefinition.getSymbol(), unitDefinition.getAlias());
                unitMap.put(unitDefinition.getSymbol(), unit);
            }
        });
    }

    private void registerConversionRate(UnitRegister register) {
        Collection<ConversionRateDefinition> rates = register.getAllConversionRateDefinition();

        rates.forEach(conversionRate -> {
            Measurement measurement = measurements.get(conversionRate.getMeasurement());
            if (measurement != null) {
                measurement.registerConversionRate(conversionRate.getNumeratorUnit(),
                        conversionRate.getDenominatorUnit(),
                        conversionRate.getRatio());
            }
        });
    }

    @Override
    public Unit getUnit(String unitSymbol) {
        Unit unit = unitMap.get(unitSymbol);
        if (unit != null) {
            return unit;
        }
        // TODO 处理复合单位

        return compoundUnitParser.parser(unitSymbol);
    }

    @Override
    public Measurement getPhysicalQuantity(String type) {
        return measurements.get(type);
    }

    @Override
    public String toString() {
        return "DefaultUnitFactory{" +
                "measurements=" + measurements +
                ",\r\n unitMap=" + unitMap +
                '}';
    }
}
