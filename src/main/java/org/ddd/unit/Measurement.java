package org.ddd.unit;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * 量度，如长度，质量，速度等。
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
    private MeasurementUnitContainer measurementUnitContainer;

    public Measurement(String type) {
        this.type = type;
    }

    public void setMeasurementUnitContainer(MeasurementUnitContainer measurementUnitContainer) {
        this.measurementUnitContainer = measurementUnitContainer;
    }

    public MeasurementUnit getUnit(String symbol) {
        if (measurementUnitContainer == null) {
            return null;
        }
        return measurementUnitContainer.getUnitBySymbol(symbol);
    }

    public boolean containsUnit(MeasurementUnit measurementUnit) {
        if (measurementUnitContainer == null) {
            return false;
        }
        return measurementUnitContainer.contains(measurementUnit);
    }

    public ConversionRate getConversionRate(MeasurementUnit from, MeasurementUnit to) {
        if (measurementUnitContainer == null) {
            return null;
        }
        Ratio ratio = measurementUnitContainer.calculateRatio(from, to);
        return new ConversionRate(from, to, ratio);
    }

    public Collection<String> allUnitSymbol() {
        if (measurementUnitContainer == null) {
            return Collections.emptyList();
        }
        return measurementUnitContainer.allUnitSymbol();
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
}
