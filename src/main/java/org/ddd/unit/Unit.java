package org.ddd.unit;

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
//    /**
//     * 单位别名，用于读作
//     */
//    private String alias;

    public Unit(UnitSymbol symbol, UnitSystem system) {
        this.symbol = symbol;
        this.system = system;
//        this.alias = alias;
    }

    public UnitSymbol symbol() {
        return symbol;
    }

//    public String alias() {
//        return alias;
//    }

    public UnitSystem unitSystem() {
        return system;
    }

    /**
     * 判断是否与该物理单位是否同类型
     *
     * @param target 待比较的物理单位
     * @return 是否是同类型的单位
     */
//    public boolean isSameSystemFor(Unit target) {
//        return unitSystem().containsUnit(target.symbol());
//    }

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
