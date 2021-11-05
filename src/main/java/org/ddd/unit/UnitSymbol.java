package org.ddd.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 单位符号，用于写作
 *
 * @author chenjx
 */
public final class UnitSymbol {
    private static final Pattern JOINER = Pattern.compile("([*/·])");

    private String symbol;

    private UnitSymbol(String symbol) {
        if (symbol == null || symbol.length() < 1) {
            throw new IllegalArgumentException("符号不能为空");
        }
        this.symbol = symbol;
    }

    public static UnitSymbol of(String symbol) {
        return new UnitSymbol(symbol);
    }

    /**
     * 该单位符号是否是原子单位
     *
     */
    public boolean isAtomic() {
        return isSingleSymbol() && !symbol.contains("^");
    }

    /**
     * 该单位符号是否是原子单位带幂
     *
     */
    public boolean hasPower() {
        return isSingleSymbol() && symbol.contains("^");
    }

    /**
     * 将复合单位拆分成原子单位，m/s拆分成 m 和 s^-1, m/s^2 拆分成 m 和 s^-2
     *
     * @return
     */
    public List<UnitSymbol> split() {
        Matcher matcher = JOINER.matcher(symbol);
        List<UnitSymbol> unitSymbols = new ArrayList<>();
        int nextPower = 1;
        int start = 0;
        while (matcher.find()) {
            String group = matcher.group();
            int end = matcher.start();
            String prevUnit = symbol.substring(start, end);
            UnitSymbol unitSymbol = new UnitSymbol(prevUnit);

            int power = unitSymbol.power() * nextPower;
            unitSymbol = unitSymbol.power(power);

            unitSymbols.add(unitSymbol);
            start = end + 1;
            if (group.equals("/")) {
                nextPower = -1;
            }else {
                nextPower = 1;
            }
        }
        String nextUnit = symbol.substring(start);
        UnitSymbol nextUnitSymbol = UnitSymbol.of(nextUnit).power(nextPower);
        unitSymbols.add(nextUnitSymbol);
        return unitSymbols;
    }

    /**
     * 去幂的单符号单位
     * @return
     */
    public UnitSymbol atomicSymbol() {
        if (isSingleSymbol()) {
            int index = symbol.lastIndexOf("^");
            if (index != -1) {
                return UnitSymbol.of(symbol.substring(0, index));
            }
        }
        return this;
    }

    public int power() {
        if (hasPower()) {
            int index = symbol.lastIndexOf("^");
            return Integer.parseInt(symbol.substring(index + 1));
        }else {
            return 1;
        }
    }

    public String symbol() {
        return symbol;
    }

    public UnitSymbol times(UnitSymbol other) {
        if (this.atomicSymbol().equals(other.atomicSymbol())) {
            int finalPower = this.power() + other.power();
            if (finalPower == 0) {
                return null;
            }
            return UnitSymbol.of(symbol + "^" + finalPower);
        } else {
            return UnitSymbol.of(symbol + "*" + other.symbol);
        }
    }

    public UnitSymbol divide(UnitSymbol other) {
        if (this.atomicSymbol().equals(other.atomicSymbol())) {
            int finalPower = this.power() - other.power();
            if (finalPower == 0) {
                return null;
            }
            return UnitSymbol.of(symbol + "^" + finalPower);
        } else {
            return UnitSymbol.of(symbol + "/" + other.symbol);
        }
    }

    public UnitSymbol power(int power) {
        int finalPower = this.power() * power;
        if (finalPower == 1) {
            return this.atomicSymbol();
        }else {
            return UnitSymbol.of(this.atomicSymbol().symbol + "^" + finalPower);
        }
    }

    private boolean isSingleSymbol() {
        Matcher matcher = JOINER.matcher(symbol);
        return !matcher.find();
    }

    @Override
    public String toString() {
        return symbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitSymbol that = (UnitSymbol) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
