package org.ddd.unit;

import java.util.Objects;

/**
 * 复合单位用于组合两个单位的乘积，单位需要考虑幂。
 * 当 Quantity 参与的计算时，需要考虑 Quantity 的单位。
 * 复合单位不知道自己属于什么物理量。
 *
 * @author chenjx
 * @see AtomicUnit
 * @see UnitWithPower
 * @see org.ddd.quantity.PhysicalQuantity
 */
public class CompoundUnit implements MeasurementUnit {
    private String alias;
    private String symbol;
    private UnitWithPower firstUnit;
    private UnitWithPower secondUnit;

    public CompoundUnit(UnitWithPower firstUnit, UnitWithPower secondUnit) {
        this.firstUnit = firstUnit;
        this.secondUnit = secondUnit;
        this.alias = firstUnit.alias() + secondUnit.alias();
        if (firstUnit.symbol().equals(secondUnit.symbol())) {
            this.symbol = firstUnit.symbol() + "^2";
        }else {
            this.symbol = firstUnit.symbol() + "·" + secondUnit.symbol();
        }
    }

    // TODO 复合单位的命名需要完善
    @Override
    public String alias() {
        return alias;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    /**
     * 复合单位不知道其物理量
     *
     * @return null
     */
    @Override
    public Measurement type() {
        return null;
    }

    /**
     * 复合单位不知道其物理量，
     * 所以当与原子单位比较时，通过原子单位来比较
     * 若同于复合单位比较，则比较两个子单位
     *
     * @param target 待比较的物理单位
     * @return 比较的结果
     */
    @Override
    public boolean isSameTypeFor(MeasurementUnit target) {
        if (target instanceof AtomicUnit) {
            return target.isSameTypeFor(this);
        }

        if (target instanceof CompoundUnit) {
            CompoundUnit targetCompoundUnit = (CompoundUnit) target;
            return this.firstUnit.isSameTypeFor(targetCompoundUnit.firstUnit)
                    && secondUnit.isSameTypeFor(targetCompoundUnit.secondUnit);
        }

        return false;
    }

    /**
     * 复合单位不知道其物理量
     * 当目标单位是原子单位时，通过原子单位获取与该单位的转换率，若存在则将转换率置位倒数
     * 当目标单位是复合单位时，通过两个子单位的转换率计算出复合单位的转换率。
     *
     * @param target 目标单位
     * @return 与目标单位的转换率
     */
    @Override
    public ConversionRate convertTo(MeasurementUnit target) {
        if (target instanceof AtomicUnit) {
            ConversionRate conversionRate = target.convertTo(this);
            if (conversionRate != null) {
                return conversionRate.reverse();
            }
        }

        if (target instanceof CompoundUnit) {
            CompoundUnit targetCompoundUnit = (CompoundUnit) target;
            ConversionRate firstConversionRate = this.firstUnit.convertTo(targetCompoundUnit.firstUnit);
            ConversionRate secondConversionRate = this.secondUnit.convertTo(targetCompoundUnit.secondUnit);
            // 两个子单位之间的转换率相乘的结果就是复合单位的转换率
            Ratio ratioOfOpportunity = firstConversionRate.getRatio().times(secondConversionRate.getRatio());
            return new ConversionRate(this, target, ratioOfOpportunity);
        }

        return null;
    }

    @Override
    public ConversionRate convertTo(String targetSymbol) {
        return null;
    }

    @Override
    public String toString() {
        return symbol();
    }
}
