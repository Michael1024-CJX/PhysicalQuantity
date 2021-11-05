package org.ddd.unit;

import org.ddd.util.NumberUtil;

import java.math.BigDecimal;

/**
 * 物理量，由数值和单位组成。同时，物理量具有一些行为：
 * 1. 转换。物理量应该能够进行单位转换，如 1m = 100cm, 1kg = 1000g。
 * 转换在 “运算” 和 “比较” 中还具有重要的用途。
 * 数量转换依赖于单位转换 {@link Unit}
 * 2. 算术运算。能够将两个物理量进行加减乘除的运算，运算不仅要考虑值，还需要考虑单位。
 * 如：100cm + 1m = 200cm， 100cm * 1m = 10000cm^2。
 * 3. 比较。两个同量度 {@link org.ddd.unit.Measurement} 的数量应该具有比较的功能，数量的单位不同的情况下，需要转换成相同的单位。
 * 如：180cm > 175cm, 6kg > 5000g等。
 *
 * @author chenjx
 */
public class PhysicalQuantity implements Comparable<PhysicalQuantity> {
    /**
     * 值
     */
    private Number amount;
    /**
     * 单位
     */
    private Unit unit;

    private UnitFactory unitFactory;

    static PhysicalQuantity of(Number amount, Unit unit, UnitFactory unitFactory) {
        return new PhysicalQuantity(amount, unit, unitFactory);
    }

    private PhysicalQuantity(Number amount, Unit unit, UnitFactory unitFactory) {
        this.amount = amount;
        this.unit = unit;
    }

    public PhysicalQuantity convertTo(Unit targetUnit) {
        ConversionRate rate = unit.convertTo(targetUnit);
        if (rate == null) {
            throw new IllegalArgumentException("无效的转换单位");
        }
        Ratio ratio = rate.getRatio();
        Ratio times = ratio.times(new BigDecimal(getAmount().toString()));

        return PhysicalQuantity.of(times.decimalValue(2, BigDecimal.ROUND_HALF_UP), rate.denominatorUnit(), unitFactory);
    }

    public PhysicalQuantity add(PhysicalQuantity augend) {
        PhysicalQuantity sameUnitQuantity = augend.convertTo(this.unit);
        Number add = NumberUtil.add(this.amount, sameUnitQuantity.amount);
        return PhysicalQuantity.of(add, unit, unitFactory);
    }

    public PhysicalQuantity subtract(PhysicalQuantity subtrahend) {
        PhysicalQuantity sameUnitQuantity = subtrahend.convertTo(this.unit);
        Number subtract = NumberUtil.subtract(this.amount, sameUnitQuantity.amount);
        return PhysicalQuantity.of(subtract, unit, unitFactory);
    }

    public PhysicalQuantity multiply(PhysicalQuantity multiplicand) {
        if (isSameTypeFor(multiplicand.unit)) {
            multiplicand = multiplicand.convertTo(this.unit);
        }
        Number multiply = NumberUtil.multiply(this.amount, multiplicand.amount);

        UnitSymbol timesUnit = unit.symbol().times(multiplicand.unit.symbol());

        return PhysicalQuantity.of(multiply, unitFactory.getUnit(timesUnit), unitFactory);
    }

    public PhysicalQuantity multiply(Number multiplicand) {
        Number multiply = NumberUtil.multiply(this.amount, multiplicand);
        return PhysicalQuantity.of(multiply, this.unit, unitFactory);
    }

    public PhysicalQuantity divide(PhysicalQuantity divisor) {
        if (isSameTypeFor(divisor.unit)) {
            divisor = divisor.convertTo(this.unit);
        }
        Number divide = NumberUtil.divide(this.amount, divisor.amount);

        UnitSymbol divideUnit = unit.symbol().divide(divisor.unit.symbol());

        return PhysicalQuantity.of(divide, unitFactory.getUnit(divideUnit), unitFactory);
    }

    public PhysicalQuantity divide(Number divisor) {
        Number divide = NumberUtil.divide(this.amount, divisor);
        return PhysicalQuantity.of(divide, this.unit, unitFactory);
    }

    @Override
    public int compareTo(PhysicalQuantity o) {
        if (this.unit.equals(o.unit)) {
            return NumberUtil.compare(this.amount, o.amount);
        }
        PhysicalQuantity sameUnit = o.convertTo(this.unit);
        if (sameUnit == null) {
            throw new IllegalArgumentException("单位类型不同，不能比较大小");
        }
        return NumberUtil.compare(this.amount, sameUnit.amount);
    }

    public Number getAmount() {
        return amount;
    }

    public Unit getUnit() {
        return unit;
    }

    private boolean isSameTypeFor(Unit unit) {
        return this.unit.isSameSystemFor(unit);
    }

    @Override
    public String toString() {
        return amount.toString() + unit.toString();
    }
}
