package org.ddd.unit.impl;

import java.util.List;

/**
 * @author chenjx
 */
public class UnitTypeConfig {
    private List<SingleUnitType> units;

    public List<SingleUnitType> getUnits() {
        return units;
    }

    public void setUnits(List<SingleUnitType> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "UnitTypeConfig{" +
                "units=" + units +
                '}';
    }
}

