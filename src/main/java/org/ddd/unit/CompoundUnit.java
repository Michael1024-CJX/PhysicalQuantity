package org.ddd.unit;

/**
 * 复合单位用于组合两个单位的乘积，单位需要考虑幂。
 * 当 Quantity 参与的计算时，需要考虑 Quantity 的单位。
 * 复合单位不知道自己的量度，单位转换需要考虑依赖
 *
 * @author chenjx
 * @see AtomicUnit
 * @see UnitWithPower
 * @see Measurement
 */
public class CompoundUnit implements Unit {
    private String alias;
    private String symbol;
    private Unit firstUnit;
    private Unit secondUnit;

    public CompoundUnit(Unit firstUnit, Unit secondUnit) {
        this.firstUnit = firstUnit;
        this.secondUnit = secondUnit;
        this.alias = firstUnit.alias() + secondUnit.alias();
        if (firstUnit.symbol().equals(secondUnit.symbol())) {
            this.symbol = firstUnit.symbol() + "^2";
        } else {
            this.symbol = firstUnit.symbol() + "·" + secondUnit.symbol();
        }
    }

    // TODO 复合单位的命名需要完善
    @Override
    public String alias() {
        return alias;
    }
    // TODO 复合单位的符号需要完善
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

    @Override
    public void setMeasurement(Measurement measurement) {

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
    public boolean isSameTypeFor(Unit target) {
        if (target instanceof CompoundUnit) {
            CompoundUnit targetCompoundUnit = (CompoundUnit) target;
            return this.firstUnit.isSameTypeFor(targetCompoundUnit.firstUnit)
                    && secondUnit.isSameTypeFor(targetCompoundUnit.secondUnit);
        } else {
            return target.isSameTypeFor(this);
        }
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
    public ConversionRate convertTo(Unit target) {
        if (target instanceof CompoundUnit) {
            CompoundUnit targetCompoundUnit = (CompoundUnit) target;
            ConversionRate firstConversionRate = this.firstUnit.convertTo(targetCompoundUnit.firstUnit);
            ConversionRate secondConversionRate = this.secondUnit.convertTo(targetCompoundUnit.secondUnit);
            // 两个子单位之间的转换率相乘的结果就是复合单位的转换率
            Ratio ratioOfOpportunity = firstConversionRate.getRatio().times(secondConversionRate.getRatio());
            return new ConversionRate(this, target, ratioOfOpportunity);
        } else {
            ConversionRate conversionRate = target.convertTo(this);
            if (conversionRate != null) {
                return conversionRate.reverse();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return symbol();
    }
}
