package org.ddd.unit;

import org.ddd.util.NumberUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

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
        if (!this.unit.getSymbol().equals(augend.unit.getSymbol())) {
            sameUnitQuantity = augend.convertTo(this.unit.getSymbol());
        }
        Number add = NumberUtil.add(this.amount, sameUnitQuantity.amount);
        return PhysicalQuantity.of(add, unit);
    }

    public PhysicalQuantity subtract(PhysicalQuantity subtrahend) {
        PhysicalQuantity sameUnitQuantity = subtrahend;
        if (!this.unit.getSymbol().equals(subtrahend.unit.getSymbol())) {
            sameUnitQuantity = subtrahend.convertTo(this.unit.getSymbol());
        }
        Number subtract = NumberUtil.subtract(this.amount, sameUnitQuantity.amount);
        return PhysicalQuantity.of(subtract, unit);
    }

    public PhysicalQuantity multiply(PhysicalQuantity multiplicand) {
        return multiply(multiplicand, NumberUtil.DEFAULT);
    }

    public PhysicalQuantity multiply(PhysicalQuantity multiplicand, MathContext mc) {
        multiplicand = multiplicand.adaptedTo(this.unit.getSymbol(), mc);

        Number multiply = NumberUtil.multiply(this.amount, multiplicand.amount);
        Unit newUnit = CompoundUnit.create(Arrays.asList(this.unit, multiplicand.unit));
        return PhysicalQuantity.of(multiply, newUnit);
    }

    public PhysicalQuantity multiply(Number multiplicand) {
        return multiply(multiplicand, NumberUtil.DEFAULT);
    }

    public PhysicalQuantity multiply(Number multiplicand, MathContext mc) {
        Number multiply = NumberUtil.multiply(this.amount, multiplicand, mc);
        return PhysicalQuantity.of(multiply, this.unit);
    }

    public PhysicalQuantity divide(PhysicalQuantity divisor) {
        return divide(divisor, NumberUtil.DEFAULT);
    }

    public PhysicalQuantity divide(PhysicalQuantity divisor, MathContext mc) {
        divisor = divisor.adaptedTo(this.unit.getSymbol(), mc);

        Number divide = NumberUtil.divide(this.amount, divisor.amount, mc);
        Unit newUnit = CompoundUnit.create(Arrays.asList(this.unit, divisor.unit.opposite()));
        return PhysicalQuantity.of(divide, newUnit);
    }

    public PhysicalQuantity divide(Number divisor) {
        return divide(divisor, NumberUtil.DEFAULT);
    }

    public PhysicalQuantity divide(Number divisor, MathContext mc) {
        Number divide = NumberUtil.divide(this.amount, divisor, mc);
        return PhysicalQuantity.of(divide, this.unit);
    }

    public PhysicalQuantity convertTo(UnitSymbol symbol) {
        return convertTo(symbol, NumberUtil.DEFAULT);
    }

    public PhysicalQuantity convertTo(UnitSymbol symbol, MathContext mc) {
        ConversionRate rate = unit.convertTo(symbol);
        if (rate == null) {
            throw new SymbolNotFoundException("无效的转换单位");
        }
        return applyConversionRate(rate, mc);
    }

    private PhysicalQuantity adaptedTo(UnitSymbol symbol, MathContext mc) {
        ConversionRate rate = unit.adaptTo(symbol);
        return applyConversionRate(rate, mc);
    }

    private PhysicalQuantity applyConversionRate(ConversionRate rate, MathContext mc) {
        Ratio ratio = rate.getRatio();
        Ratio times = ratio.times(new BigDecimal(getAmount().toString()));

        return PhysicalQuantity.of(times.decimalValue(mc), rate.denominatorUnit());
    }

    @Override
    public int compareTo(PhysicalQuantity o) {
        if (this.unit.isEquals(o.unit)) {
            return NumberUtil.compare(this.amount, o.amount);
        }
        PhysicalQuantity sameUnit = o.convertTo(this.unit.getSymbol());
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
        return amount.toString() + (unit == null ? "" : unit.toString());
    }
}
