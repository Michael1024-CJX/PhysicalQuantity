package org.ddd.unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenjx
 */
public class DefaultUnitFactory implements UnitFactory {
    private final Map<Measurement, UnitSystem> unitSystemMap = new ConcurrentHashMap<>();
    private final Map<UnitSymbol, Unit> unitMap = new ConcurrentHashMap<>();

//    private CompoundUnitParser compoundUnitParser = new CompoundUnitParser(this);

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
        if (unitSymbol.isAtomic()) {
            return unitMap.get(unitSymbol);
        }
        if (unitSymbol.hasPower()) {
            PowerUnitSystem powerUnitSystem = getPowerUnitSystem(unitSymbol);
            return powerUnitSystem.getUnit(unitSymbol);
        }

        List<UnitSymbol> split = unitSymbol.split();
        List<UnitSystem> systems = new ArrayList<>();
        for (UnitSymbol symbol : split) {
            if (symbol.isAtomic()) {
                Unit unit = unitMap.get(unitSymbol);
                systems.add(unit.unitSystem());
            }else {
                PowerUnitSystem powerUnitSystem = getPowerUnitSystem(symbol);
                systems.add(powerUnitSystem);
            }
        }
        CompoundUnitSystem compoundUnitSystem = new CompoundUnitSystem(systems, getMeasurement(unitSymbol));
        return compoundUnitSystem.getUnit(unitSymbol);
    }

    private PowerUnitSystem getPowerUnitSystem(UnitSymbol symbol) {
        Unit unit = unitMap.get(symbol.atomicSymbol());
        return new PowerUnitSystem(unit.unitSystem(), symbol.power(), getMeasurement(symbol));
    }

    @Override
    public Measurement getMeasurement(UnitSymbol unitSymbol) {
        return null;
    }


    private void registerPhysicalQuantity(UnitRegister register) {
        Collection<String> allPhysicalQuantity = register.getAllMeasurements();

        allPhysicalQuantity.forEach(type -> {
            Measurement measurement = Measurement.of(type);
            AtomicUnitSystem atomicUnitSystem = new AtomicUnitSystem(measurement, new LinkedMapContainer());
            unitSystemMap.putIfAbsent(measurement, atomicUnitSystem);
        });
    }

    private void registerPhysicalUnit(UnitRegister register) {
        Collection<UnitDefinition> allUnitDefinition = register.getAllUnitDefinition();

        allUnitDefinition.forEach(unitDefinition -> {
            Measurement measurement = Measurement.of(unitDefinition.getMeasurement());
            UnitSystem unitSystem = unitSystemMap.get(measurement);
            if (unitSystem instanceof AtomicUnitSystem) {
                Unit unit = ((AtomicUnitSystem) unitSystem).registerUnit(
                        unitDefinition.getSymbol(), unitDefinition.getAlias());
                unitMap.put(unit.symbol(), unit);
            }
        });
    }

    private void registerConversionRate(UnitRegister register) {
        Collection<ConversionRateDefinition> rates = register.getAllConversionRateDefinition();

        rates.forEach(conversionRate -> {
            Measurement measurement = Measurement.of(conversionRate.getMeasurement());
            UnitSystem unitSystem = unitSystemMap.get(measurement);
            if (unitSystem instanceof AtomicUnitSystem) {
                ((AtomicUnitSystem) unitSystem).registerConversionRate(conversionRate.getNumeratorUnit(),
                        conversionRate.getDenominatorUnit(),
                        conversionRate.getRatio());
            }
        });
    }
}
