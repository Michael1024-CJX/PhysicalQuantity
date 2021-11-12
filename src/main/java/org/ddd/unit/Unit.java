package org.ddd.unit;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author chenjx
 */
public class Unit {
    /**
     * 单位的符号，具有唯一性，即写作
     */
    private UnitSymbol symbol;
    /**
     * 单位制，如长度，质量，时间等
     */
    private UnitSystem system;

    public Unit(UnitSymbol symbol, UnitSystem system) {
        this.symbol = symbol;
        this.system = system;
    }

    public UnitSymbol symbol() {
        return symbol;
    }

    public UnitSystem unitSystem() {
        return system;
    }

    /**
     * 获取与目标单位的的比率，需要两个单位属于同物理量
     *
     * @param target 目标单位
     * @return 自身与目标单位的比率
     */
    public ConversionRate convertTo(Unit target) {
        return system.getConversionRate(this.symbol(), target.symbol());
    }


    /**
     * 获取与目标单位的的比率，需要两个单位属于同物理量
     *
     * @param target 目标单位
     * @return 自身与目标单位的比率
     */
    public ConversionRate convertTo(UnitSymbol target) {
        return system.getConversionRate(this.symbol(), target);
    }

    public UnitSymbol adaptedTo(UnitSymbol target) {
        return system.adapt(this.symbol(), target);
    }

    public Unit multiply(Unit another) {
        UnitSymbol timesUnit = this.symbol.times(another.symbol());

        UnitSystem newSystem;
        if (another.symbol().baseEquals(this.symbol)) {
            int index = another.symbol().index() + symbol().index();
            if (index == 0) {
                return null;
            }else if (index == 1) {
                newSystem = this.unitSystem();
            }else {
                newSystem = new PowerUnitSystem(unitSystem(), index);
            }
        }else {
            List<UnitSystem> systems = Arrays.asList(unitSystem(), another.unitSystem());
            newSystem = new CompoundUnitSystem(systems);
        }

        return newSystem.getUnit(timesUnit);
    }

    public Unit divide(Unit another) {
        UnitSymbol timesUnit = this.symbol.divide(another.symbol());

        UnitSystem newSystem;
        if (another.symbol().baseEquals(this.symbol)) {
            int index = symbol().index() - another.symbol().index();
            if (index == 0) {
                return null;
            }else if (index == 1) {
                newSystem = this.unitSystem();
            }else {
                newSystem = new PowerUnitSystem(unitSystem(), index);
            }
        }else {
            List<UnitSystem> systems = Arrays.asList(unitSystem(), new PowerUnitSystem(another.unitSystem(), -1));
            newSystem = new CompoundUnitSystem(systems);
        }

        return newSystem.getUnit(timesUnit);
    }

    @Override
    public String toString() {
        return symbol.symbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return Objects.equals(symbol, unit.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
