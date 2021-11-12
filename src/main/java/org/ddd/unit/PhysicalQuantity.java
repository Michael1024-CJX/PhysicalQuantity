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

    static PhysicalQuantity of(Number amount, Unit unit) {
        return new PhysicalQuantity(amount, unit);
    }

    private PhysicalQuantity(Number amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public PhysicalQuantity add(PhysicalQuantity augend) {
        PhysicalQuantity sameUnitQuantity = augend;
        if (!this.unit.symbol().equals(augend.unit.symbol())) {
            sameUnitQuantity = augend.convertTo(this.unit.symbol());
        }
        Number add = NumberUtil.add(this.amount, sameUnitQuantity.amount);
        return PhysicalQuantity.of(add, unit);
    }

    public PhysicalQuantity subtract(PhysicalQuantity subtrahend) {
        PhysicalQuantity sameUnitQuantity = subtrahend;
        if (!this.unit.symbol().equals(subtrahend.unit.symbol())) {
            sameUnitQuantity = subtrahend.convertTo(this.unit.symbol());
        }
        Number subtract = NumberUtil.subtract(this.amount, sameUnitQuantity.amount);
        return PhysicalQuantity.of(subtract, unit);
    }

    public PhysicalQuantity multiply(PhysicalQuantity multiplicand) {
        multiplicand = multiplicand.adaptedTo(this.unit.symbol());

        Number multiply = NumberUtil.multiply(this.amount, multiplicand.amount);
        Unit newUnit = this.unit.multiply(multiplicand.unit);

        return PhysicalQuantity.of(multiply, newUnit);
    }

    public PhysicalQuantity multiply(Number multiplicand) {
        Number multiply = NumberUtil.multiply(this.amount, multiplicand);
        return PhysicalQuantity.of(multiply, this.unit);
    }

    public PhysicalQuantity divide(PhysicalQuantity divisor) {
        divisor = divisor.adaptedTo(this.unit.symbol());

        Number divide = NumberUtil.divide(this.amount, divisor.amount);

        Unit newUnit = this.unit.divide(divisor.unit);
        return PhysicalQuantity.of(divide, newUnit);
    }

    public PhysicalQuantity divide(Number divisor) {
        Number divide = NumberUtil.divide(this.amount, divisor);
        return PhysicalQuantity.of(divide, this.unit);
    }

    public PhysicalQuantity convertTo(String symbol) {
        return convertTo(UnitSymbol.of(symbol));
    }

    public PhysicalQuantity convertTo(UnitSymbol symbol) {
        ConversionRate rate = unit.convertTo(symbol);
        if (rate == null) {
            throw new SymbolNotFoundException("无效的转换单位");
        }
        return applyConversionRate(rate);
    }

    private PhysicalQuantity applyConversionRate(ConversionRate rate) {
        Ratio ratio = rate.getRatio();
        Ratio times = ratio.times(new BigDecimal(getAmount().toString()));

        return PhysicalQuantity.of(times.decimalValue(NumberUtil.DEFAULT), rate.denominatorUnit());
    }

    public PhysicalQuantity adaptedTo(UnitSymbol symbol) {
        UnitSymbol rate = unit.adaptedTo(symbol);
        return convertTo(rate);
    }

    @Override
    public int compareTo(PhysicalQuantity o) {
        if (this.unit.equals(o.unit)) {
            return NumberUtil.compare(this.amount, o.amount);
        }
        PhysicalQuantity sameUnit = o.convertTo(this.unit.symbol());
        return NumberUtil.compare(this.amount, sameUnit.amount);
    }

    public Number getAmount() {
        return amount;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return amount.toString() + unit.toString();
    }
}
