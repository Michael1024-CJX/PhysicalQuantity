package org.ddd.unit;

import org.ddd.util.NumberUtil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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

//    private UnitFactory unitFactory;

    static PhysicalQuantity of(Number amount, Unit unit) {
        return new PhysicalQuantity(amount, unit);
    }

    private PhysicalQuantity(Number amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
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

    public PhysicalQuantity adaptedTo(UnitSymbol symbol) {
        UnitSymbol rate = unit.adaptedTo(symbol);
        return convertTo(rate);
    }

    private PhysicalQuantity applyConversionRate(ConversionRate rate) {
        Ratio ratio = rate.getRatio();
        Ratio times = ratio.times(new BigDecimal(getAmount().toString()));

        return PhysicalQuantity.of(times.decimalValue(NumberUtil.DEFAULT), rate.denominatorUnit());
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
        PhysicalQuantity sameUnitQuantity = multiplicand.adaptedTo(this.unit.symbol());

        Number multiply = NumberUtil.multiply(this.amount, sameUnitQuantity.amount);

        UnitSystem unitSystem = newUnitSystem(multiplicand.unit);
        UnitSymbol timesUnit = unit.symbol().times(sameUnitQuantity.unit.symbol());

        return PhysicalQuantity.of(multiply, unitSystem.getUnit(timesUnit));
    }

    public PhysicalQuantity multiply(Number multiplicand) {
        Number multiply = NumberUtil.multiply(this.amount, multiplicand);
        return PhysicalQuantity.of(multiply, this.unit);
    }

    public PhysicalQuantity divide(PhysicalQuantity divisor) {
        PhysicalQuantity sameUnitQuantity = divisor.adaptedTo(this.unit.symbol());

        Number divide = NumberUtil.divide(this.amount, sameUnitQuantity.amount);

        UnitSymbol divideUnit = unit.symbol().divide(sameUnitQuantity.unit.symbol());
        UnitSystem unitSystem = newNegativePowerUnitSystem(divisor.unit);

        return PhysicalQuantity.of(divide, unitSystem.getUnit(divideUnit));
    }

    public PhysicalQuantity divide(Number divisor) {
        Number divide = NumberUtil.divide(this.amount, divisor);
        return PhysicalQuantity.of(divide, this.unit);
    }


    private UnitSystem newUnitSystem(Unit another) {
        if (another.symbol().base().equals(unit.symbol().base())) {
            int index = another.symbol().index() + unit.symbol().index();
            return new PowerUnitSystem(unit.unitSystem(), index);
        }
        List<UnitSystem> systems = Arrays.asList(unit.unitSystem(), another.unitSystem());
        return new CompoundUnitSystem(systems);
    }

    private UnitSystem newNegativePowerUnitSystem(Unit another) {
        if (another.symbol().base().equals(unit.symbol().base())) {
            int index = another.symbol().index() + unit.symbol().index();
            return new PowerUnitSystem(unit.unitSystem(), -index);
        }
        List<UnitSystem> systems = Arrays.asList(unit.unitSystem(), new PowerUnitSystem(another.unitSystem(), -1));
        return new CompoundUnitSystem(systems);
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
