package org.ddd.unit;

import java.util.Objects;

/**
 * @author chenjx
 */
public class UnitDefinition {
    private String physicalQuantity;
    private String symbol;
    private String alias;

    public String getPhysicalQuantity() {
        return physicalQuantity;
    }

    public void setPhysicalQuantity(String physicalQuantity) {
        this.physicalQuantity = physicalQuantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitDefinition that = (UnitDefinition) o;
        return Objects.equals(physicalQuantity, that.physicalQuantity) &&
                Objects.equals(symbol, that.symbol) &&
                Objects.equals(alias, that.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(physicalQuantity, symbol, alias);
    }

    @Override
    public String toString() {
        return "UnitDefinition{" +
                "physicalQuantity='" + physicalQuantity + '\'' +
                ", symbol='" + symbol + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
