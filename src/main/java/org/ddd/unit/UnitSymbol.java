package org.ddd.unit;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 单位符号，用于写作
 *
 * @author chenjx
 */
public final class UnitSymbol {
    private static final String MULTIPLY_JOINER = "*·";
    private static final String DIVIDE_JOINER = "/";
    private static final String POWER_JOINER = "^";
    private static final Pattern COMBINATION_JOINER = Pattern.compile("([" + MULTIPLY_JOINER + DIVIDE_JOINER + "])");

    private final String symbol;

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
     * 判断符号是否只有一种，如 m, kg, h等
     * 组合单位的符号将返回false，如 m/s。
     */
    public boolean isSingleSymbol() {
        Matcher matcher = COMBINATION_JOINER.matcher(symbol);
        return !matcher.find();
    }

    /**
     * 将复合单位拆分成原子单位，m/s拆分成 m 和 s^-1, m/s^2 拆分成 m 和 s^-2
     *
     * @return
     */
    public List<UnitSymbol> splitIntoSingleSymbol() {
        Matcher matcher = COMBINATION_JOINER.matcher(symbol);
        List<UnitSymbol> unitSymbols = new ArrayList<>();
        int nextPower = 1;
        int start = 0;
        while (matcher.find()) {
            String group = matcher.group();
            int end = matcher.start();
            String prevUnit = symbol.substring(start, end);
            UnitSymbol unitSymbol = new UnitSymbol(prevUnit);

            unitSymbol = unitSymbol.power(nextPower);

            unitSymbols.add(unitSymbol);
            start = end + 1;
            // 除号后面的都为负幂
            if (group.equals(DIVIDE_JOINER)) {
                nextPower = -1;
            }
        }
        if (start != 0) {
            String nextUnit = symbol.substring(start);
            UnitSymbol nextUnitSymbol = UnitSymbol.of(nextUnit).power(nextPower);
            unitSymbols.add(nextUnitSymbol);
        } else {
            unitSymbols.add(this);
        }
        return unitSymbols;
    }

    /**
     * 对于原子单位带幂，如 kg, cm^2 ,m^3等，返回底数 kg, cm, m
     * 对于复合单位，直接返回本身，如 m/s^2 返回 m/s^2
     *
     * @return 幂单位的底数
     */
    public UnitSymbol base() {
        if (isSingleSymbol()) {
            int index = symbol.indexOf(POWER_JOINER);
            if (index != -1) {
                return UnitSymbol.of(symbol.substring(0, index));
            }
        }
        return this;
    }

    /**
     * @return 幂单位的指数
     */
    public int index() {
        if (isSingleSymbol()) {
            int index = symbol.indexOf(POWER_JOINER);
            if (index != -1) {
                return Integer.parseInt(symbol.substring(index + 1));
            }
        }
        return 1;
    }

    public String symbol() {
        return symbol;
    }

    public UnitSymbol times(UnitSymbol other) {
        Map<UnitSymbol, Integer> symbolPowerMap = singleSymbolPowerMap();

        List<UnitSymbol> otherSymbols = other.splitIntoSingleSymbol();
        // 将底数相同的单位，指数相加
        for (UnitSymbol otherSymbol : otherSymbols) {
            symbolPowerMap.merge(otherSymbol.base(), otherSymbol.index(), Integer::sum);
        }

        return buildSymbol(symbolPowerMap);
    }

    public UnitSymbol divide(UnitSymbol other) {
        Map<UnitSymbol, Integer> symbolPowerMap = singleSymbolPowerMap();

        List<UnitSymbol> otherSymbols = other.splitIntoSingleSymbol();
        // 将底数相同的单位，指数相减
        for (UnitSymbol otherSymbol : otherSymbols) {
            symbolPowerMap.merge(otherSymbol.base(), -otherSymbol.index(), Integer::sum);
        }

        return buildSymbol(symbolPowerMap);
    }

    private Map<UnitSymbol, Integer> singleSymbolPowerMap() {
        List<UnitSymbol> originSymbols = this.splitIntoSingleSymbol();
        Map<UnitSymbol, Integer> symbolPowerMap = new LinkedHashMap<>();

        for (UnitSymbol originSymbol : originSymbols) {
            symbolPowerMap.merge(originSymbol.base(), originSymbol.index(), Integer::sum);
        }
        return symbolPowerMap;
    }

    private UnitSymbol buildSymbol(Map<UnitSymbol, Integer> symbolPowerMap) {
        return symbolPowerMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != 0)
//                .sorted((e1,e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(entry -> entry.getValue() == 1 ? entry.getKey() : entry.getKey().power(entry.getValue()))
                .reduce(UnitSymbol::appendWith)
                .orElse(null);
    }

    public UnitSymbol power(int power) {
        int finalPower = this.index() * power;
        if (finalPower == 1) {
            return this.base();
        } else {
            return UnitSymbol.of(this.base().symbol + POWER_JOINER + finalPower);
        }
    }

    private UnitSymbol appendWith(UnitSymbol symbol) {
        return UnitSymbol.of(this.symbol + "*" + symbol.symbol);
    }

    public UnitSymbol format() {
        Map<UnitSymbol, Integer> unitSymbolIntegerMap = singleSymbolPowerMap();
        return buildSymbol(unitSymbolIntegerMap);
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
        return Objects.equals(this.format().symbol, that.format().symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
