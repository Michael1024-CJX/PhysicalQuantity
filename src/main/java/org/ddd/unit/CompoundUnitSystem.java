package org.ddd.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjx
 */
public class CompoundUnitSystem extends AbstractUnitSystem implements UnitSystem {
    private List<UnitSystem> systems;

    public CompoundUnitSystem(List<UnitSystem> systems) {
        this.systems = systems;
    }

    public void addUnitSystem(UnitSystem system) {
        systems.add(system);
    }

    @Override
    public boolean containsUnit(UnitSymbol symbol) {
        List<UnitSymbol> singleSymbols = symbol.splitIntoSingleSymbol();
        for (UnitSymbol singleSymbol : singleSymbols) {
            if (!contains(singleSymbol)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public UnitSymbol adapt(UnitSymbol from, UnitSymbol target) {
        List<UnitSymbol> fromSS = from.splitIntoSingleSymbol();
        List<UnitSymbol> targetSS = target.splitIntoSingleSymbol();
        ArrayList<UnitSymbol> result = new ArrayList<>();
        for (UnitSymbol fromS : fromSS) {
            UnitSystem unitSystem = getUnitSystem(fromS);
//            boolean flag = false;
//            for (UnitSymbol targetS : targetSS) {
//                if (unitSystem.containsUnit(targetS)) {
//                    result.add(targetS);
//                    flag = true;
//                    break;
//                }
//            }
//            if (!flag) {
//                result.add(fromS);
//            }
            UnitSymbol adapt = unitSystem.adapt(fromS, target);
            result.add(adapt);
        }
        return result.stream().reduce(UnitSymbol::appendWith).orElse(null);
    }

    @Override
    Unit doGet(UnitSymbol symbol) {
        List<UnitSymbol> singleSymbols = symbol.splitIntoSingleSymbol();
        ArrayList<UnitSystem> unitSystems = new ArrayList<>();

        List<UnitSystem> collect = singleSymbols
                .stream()
                .map(this::getUnitSystem)
                .collect(Collectors.toList());

        CompoundUnitSystem unitSystem = new CompoundUnitSystem(collect);

        return new Unit(symbol, unitSystem);
    }

    @Override
    ConversionRate doGetConversionRate(UnitSymbol from, UnitSymbol to) {
        List<UnitSymbol> fromUnits = from.splitIntoSingleSymbol();
        List<UnitSymbol> toUnits = to.splitIntoSingleSymbol();

        int size = fromUnits.size();
        Ratio ratio = Ratio.ONE_RATIO;

        for (int i = 0; i < size; i++) {
            UnitSymbol fromAtomicUnit = fromUnits.get(i);
            UnitSymbol toAtomicUnit = toUnits.get(i);
            UnitSystem unitSystem = getUnitSystem(fromAtomicUnit);
            if (!unitSystem.containsUnit(toAtomicUnit)){
                return null;
            }
            ConversionRate rate = unitSystem.getConversionRate(fromAtomicUnit, toAtomicUnit);
            ratio = ratio.times(rate.getRatio());
        }

        return new ConversionRate(getUnit(from), getUnit(to), ratio);
    }

    private boolean contains(UnitSymbol atomicSymbol) {
        for (UnitSystem system : systems) {
            if (system.containsUnit(atomicSymbol)) {
                return true;
            }
        }
        return false;
    }

    private UnitSystem getUnitSystem(UnitSymbol atomicSymbol) {
        for (UnitSystem system : systems) {
            if (system.containsUnit(atomicSymbol)) {
                return system;
            }
        }
        throw new SymbolNotFoundException();
    }
}
