package org.ddd.unit.impl;

import org.ddd.unit.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author chenjx
 */
public class DefaultUnitFactory implements UnitFactory {
    private final Map<Measurement, UnitSystem> unitSystemMap = new ConcurrentHashMap<>();
    private final Map<UnitSymbol, Unit> unitMap = new ConcurrentHashMap<>();

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

    @Override
    public Unit getUnit(UnitSymbol unitSymbol) {
        if (unitSymbol.isBasic()) {
            Unit baseUnit = unitMap.get(unitSymbol);
            return PowerUnit.ofPositive(baseUnit);
        }

        List<UnitSymbol> singleSymbol = unitSymbol.basicSymbols();
        List<Unit> units = singleSymbol.stream()
                .map(this::getSingleSymbolUnit)
                .collect(Collectors.toList());
        return CompoundUnit.create(units);
    }

    private Unit getSingleSymbolUnit(UnitSymbol unitSymbol) {
        int index = unitSymbol.index();
        if (index == 1) {
            Unit baseUnit = unitMap.get(unitSymbol);
            return PowerUnit.ofPositive(baseUnit);
        } else {
            Unit baseUnit = unitMap.get(unitSymbol.base());
            return PowerUnit.ofNegative(baseUnit);
        }
    }

    private void registerPhysicalQuantity(UnitRegister register) {
        Collection<String> allPhysicalQuantity = register.getAllMeasurements();

        allPhysicalQuantity.forEach(type -> {
            Measurement measurement = Measurement.of(type);
            UnitSystem atomicUnitSystem = new BasicUnitSystem(new LinkedMapContainer());
            unitSystemMap.putIfAbsent(measurement, atomicUnitSystem);
        });
    }

    private void registerPhysicalUnit(UnitRegister register) {
        Collection<UnitDefinition> allUnitDefinition = register.getAllUnitDefinition();

        allUnitDefinition.forEach(unitDefinition -> {
            Measurement measurement = Measurement.of(unitDefinition.getMeasurement());
            UnitSystem unitSystem = unitSystemMap.get(measurement);
            if (unitSystem instanceof BasicUnitSystem) {
                Unit unit = ((BasicUnitSystem) unitSystem).registerUnit(
                        unitDefinition.getSymbol());
                unitMap.put(unit.getSymbol(), unit);
            }
        });
    }

    private void registerConversionRate(UnitRegister register) {
        Collection<ConversionRateDefinition> rates = register.getAllConversionRateDefinition();

        rates.forEach(conversionRate -> {
            Measurement measurement = Measurement.of(conversionRate.getMeasurement());
            UnitSystem unitSystem = unitSystemMap.get(measurement);
            if (unitSystem instanceof BasicUnitSystem) {
                ((BasicUnitSystem) unitSystem).registerConversionRate(conversionRate.getNumeratorUnit(),
                        conversionRate.getDenominatorUnit(),
                        conversionRate.getRatio());
            }
        });
    }
}
