package org.ddd.unit;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjx
 */
public class CompoundUnit implements Unit {
    private List<Unit> units;
    private UnitSymbol symbol;

    public CompoundUnit(List<Unit> units) {
        this.units = units;
        this.symbol = createSymbol();
    }

    @Override
    public UnitSymbol getSymbol() {
        return symbol;
    }

    @Override
    public boolean isSameTypeAs(UnitSymbol symbol) {
        List<UnitSymbol> singleSymbol = symbol.getBasicSymbols();
        for (UnitSymbol unitSymbol : singleSymbol) {
            if (!containsType(unitSymbol)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsType(UnitSymbol symbol) {
        for (Unit unit : units) {
            if (unit.isSameTypeAs(symbol)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConversionRate convertTo(UnitSymbol target) {
        if (!isSameTypeAs(target)) {
            return null;
        }
        List<UnitSymbol> singleSymbol = target.getBasicSymbols();
        List<Unit> targetUnits = new ArrayList<>(units.size());
        Ratio ratio = Ratio.ONE_RATIO;

        for (UnitSymbol unitSymbol : singleSymbol) {
            for (Unit unit : units) {
                if (unit.isSameTypeAs(unitSymbol)) {
                    ConversionRate rate = unit.convertTo(unitSymbol);
                    if (rate == null) {
                        return null;
                    }
                    ratio = ratio.times(rate.getRatio());
                    targetUnits.add(rate.denominatorUnit());
                    break;
                }
            }
        }
        return new ConversionRate(this, new CompoundUnit(targetUnits), ratio);
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
        return new ConversionRate(this, new CompoundUnit(targetUnits), ratio);
    }

    @Override
    public Unit opposite() {
        List<Unit> oppositeUnits = units.stream().map(Unit::opposite).collect(Collectors.toList());

        return new CompoundUnit(oppositeUnits);
    }

    private UnitSymbol createSymbol() {
        return units.stream()
                .map(Unit::getSymbol)
                .reduce(UnitSymbol::times)
                .orElseThrow(IllegalArgumentException::new);
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
