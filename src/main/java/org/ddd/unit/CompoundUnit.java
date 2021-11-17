package org.ddd.unit;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjx
 */
public class CompoundUnit implements Unit {
    private List<Unit> units;
    private UnitSymbol symbol;

    private CompoundUnit(UnitSymbol symbol, List<Unit> units) {
        this.symbol = symbol;
        this.units = units;
    }

    public static Unit create(List<Unit> units) {
        UnitSymbol symbol = createSymbol(units);
        if (symbol == null) {
            return null;
        }
        return new CompoundUnit(symbol, units);
    }

    private static UnitSymbol createSymbol(List<Unit> units) {
        return units.stream()
                .map(Unit::getSymbol)
                .reduce(UnitSymbol::times)
                .orElse(null);
    }

    @Override
    public UnitSymbol getSymbol() {
        return symbol;
    }

    /**
     * 对于复合单位
     * @param symbol
     * @return
     */
    @Override
    public boolean isSameTypeAs(UnitSymbol symbol) {
        List<UnitSymbol> basicSymbols = symbol.basicSymbols();
        Iterator<Unit> unitIterator = this.iterator();

        while (unitIterator.hasNext() && !basicSymbols.isEmpty()) {

            Unit unit = unitIterator.next();
            int index = getSameTypeSymbolIndex(basicSymbols, unit);
            if (index == -1) {
                return false;
            }
            basicSymbols.remove(index);
        }

        return !unitIterator.hasNext() && basicSymbols.isEmpty();
    }

    private int getSameTypeSymbolIndex(List<UnitSymbol> basicSymbols, Unit unit) {
        for (int i = 0; i < basicSymbols.size(); i++) {
            UnitSymbol unitSymbol = basicSymbols.get(i);
            if (unit.isSameTypeAs(unitSymbol)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ConversionRate convertTo(UnitSymbol target) {
        if (!isSameTypeAs(target)) {
            return null;
        }
        Iterator<Unit> unitIterator = this.iterator();
        List<UnitSymbol> basicSymbols = target.basicSymbols();
        Ratio ratio = Ratio.ONE_RATIO;
        List<Unit> targetUnits = new ArrayList<>(units.size());
        while (unitIterator.hasNext()) {
            Unit unit = unitIterator.next();
            int size = basicSymbols.size();

            for (int i = 0; i < size; i++) {
                UnitSymbol unitSymbol = basicSymbols.get(i);
                if (unit.isSameTypeAs(unitSymbol)) {
                    ConversionRate rate = unit.convertTo(unitSymbol);
                    if (rate == null) {
                        return null;
                    }
                    ratio = ratio.times(rate.getRatio());
                    targetUnits.add(rate.denominatorUnit());
                    basicSymbols.remove(i);
                    break;
                }
            }
        }
        return new ConversionRate(this, CompoundUnit.create(targetUnits), ratio);
    }

    @Override
    public ConversionRate adaptTo(UnitSymbol target) {
        List<Unit> targetUnits = new ArrayList<>(units.size());
        Ratio ratio = Ratio.ONE_RATIO;
        for (Unit unit : units) {
            ConversionRate rate = unit.adaptTo(target);
            if (rate != null) {
                ratio = ratio.times(rate.getRatio());
                targetUnits.add(rate.denominatorUnit());
            }
        }
        return new ConversionRate(this, CompoundUnit.create(targetUnits), ratio);
    }

    @Override
    public Unit opposite() {
        List<Unit> oppositeUnits = units.stream().map(Unit::opposite).collect(Collectors.toList());

        return CompoundUnit.create(oppositeUnits);
    }

    @Override
    public Iterator<Unit> iterator() {
        return new CompoundUnitIterator(units.iterator());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompoundUnit unit = (CompoundUnit) o;
        return Objects.equals(symbol, unit.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return symbol.symbol();
    }
}
