package org.ddd.unit;

import java.util.Collection;
import java.util.Objects;

/**
 * 量度，如长度，质量，速度等。
 * 量度是单位的信息专家，拥有单位的知识。
 * 只有同量度的单位之间能够转换。
 *
 * @author chenjx
 */
public class Measurement {
    /**
     * 量度的类型
     */
    private String type;
    /**
     * 属于该物理量的单位容器
     */
    private UnitContainer unitContainer;

    public Measurement(String type, UnitContainer unitContainer) {
        this.type = type;
        this.unitContainer = unitContainer;
    }

    public Unit registerUnit(String symbol, String alias) {
        AtomicUnit atomicUnit = new AtomicUnit(symbol, alias);
        atomicUnit.setMeasurement(this);
        unitContainer.registerUnit(atomicUnit);
        return atomicUnit;
    }

    public void registerConversionRate(String numeratorUnit, String denominatorUnit, Ratio ratio) {

        unitContainer.registerConversionRate(numeratorUnit, denominatorUnit, ratio);
    }

    public Unit getUnit(String symbol) {

        return unitContainer.getUnitBySymbol(symbol);
    }

    public boolean containsUnit(Unit unit) {
        return unitContainer.contains(unit);
    }

    public ConversionRate getConversionRate(Unit from, Unit to) {
        Ratio ratio = unitContainer.calculateRatio(from, to);
        return new ConversionRate(from, to, ratio);
    }

    public Collection<String> allUnitSymbol() {
//        return unitContainer.allUnitSymbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement measurement = (Measurement) o;
        return Objects.equals(type, measurement.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return type;
    }

    public String type() {
        return type;
    }
}
