package org.ddd.unit;

import java.util.Objects;

/**
 * @author chenjx
 */
public class ConversionRateDefinition {
    private String physicalQuantity;
    private String numeratorUnit;
    private String denominatorUnit;
    private Ratio ratio;

    public String getPhysicalQuantity() {
        return physicalQuantity;
    }

    public void setPhysicalQuantity(String physicalQuantity) {
        this.physicalQuantity = physicalQuantity;
    }

    public String getNumeratorUnit() {
        return numeratorUnit;
    }

    public void setNumeratorUnit(String numeratorUnit) {
        this.numeratorUnit = numeratorUnit;
    }

    public String getDenominatorUnit() {
        return denominatorUnit;
    }

    public void setDenominatorUnit(String denominatorUnit) {
        this.denominatorUnit = denominatorUnit;
    }

    public Ratio getRatio() {
        return ratio;
    }

    public void setRatio(Ratio ratio) {
        this.ratio = ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionRateDefinition that = (ConversionRateDefinition) o;
        return Objects.equals(physicalQuantity, that.physicalQuantity) &&
                Objects.equals(numeratorUnit, that.numeratorUnit) &&
                Objects.equals(denominatorUnit, that.denominatorUnit) &&
                Objects.equals(ratio, that.ratio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(physicalQuantity, numeratorUnit, denominatorUnit, ratio);
    }

    @Override
    public String toString() {
        return "ConversionRateDefinition{" +
                "physicalQuantity='" + physicalQuantity + '\'' +
                ", numeratorUnit='" + numeratorUnit + '\'' +
                ", denominatorUnit='" + denominatorUnit + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
