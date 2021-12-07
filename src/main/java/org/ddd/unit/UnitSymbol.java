package org.ddd.unit;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 单位符号
 *
 * @author chenjx
 */
public final class UnitSymbol {
    static final UnitSymbol NIX = UnitSymbol.of("");

    private static final String MULTIPLY_JOINER = "*·";
    private static final String DIVIDE_JOINER = "/";
    private static final String POWER_JOINER = "^";
    private static final Pattern CALC_JOINER = Pattern.compile("([" + MULTIPLY_JOINER + DIVIDE_JOINER + "])");
    private static final Pattern COMBINATION_JOINER = Pattern.compile("([" + MULTIPLY_JOINER + DIVIDE_JOINER + POWER_JOINER + "])");

    private final String symbol;

    private UnitSymbol(String symbol) {
        this.symbol = symbol;
    }

    public static UnitSymbol of(String symbol) {
        return new UnitSymbol(symbol);
    }

    /**
     * 判断符号是否是基本单位，基本单位如：m, cm, g, kg等
     * 组合单位将返回false，如 m/s, m^2, m*kg/s^2
     */
    public boolean isBasic() {
        Matcher matcher = COMBINATION_JOINER.matcher(symbol);
        return !matcher.find();
    }

    /**
     * 获取单位的底数，如 kg, cm^2 ,m^3将返回底数 kg, cm, m
     * 对于复合单位，直接返回本身，如 m/s^2 返回 m/s^2
     */
    public UnitSymbol base() {
        if (isSingle()) {
            int index = symbol.indexOf(POWER_JOINER);
            if (index != -1) {
                return UnitSymbol.of(symbol.substring(0, index));
            }
        }
        return this;
    }

    /**
     * 获取单位的指数
     */
    public int index() {
        if (isSingle()) {
            int index = symbol.indexOf(POWER_JOINER);
            if (index != -1) {
                return Integer.parseInt(symbol.substring(index + 1));
            }
        }
        return 1;
    }

    /**
     * 判断符号是否只有一种，如 m, kg, h等
     * 组合单位的符号将返回false，如 m/s, m^2。
     */
    private boolean isSingle() {
        Matcher matcher = CALC_JOINER.matcher(symbol);
        return !matcher.find();
    }

    /**
     * 获取组成单位的基本单位列表。
     * 注意以 “/” 为分割线，“/”后面的单位都转为负幂
     * 如：
     *   m     -> [m]
     *   m/s   -> [m, s^-1]
     *   m/s^2 -> [m, s^-1, s^-1]
     */
    public List<UnitSymbol> basicSymbols() {
        List<UnitSymbol> singleSymbol = splitIntoSingleSymbol();
        List<UnitSymbol> basicSymbols = new ArrayList<>();
        for (UnitSymbol unitSymbol : singleSymbol) {
            basicSymbols.addAll(unitSymbol.splitPowerSymbol());
        }
        return basicSymbols;
    }

    /**
     * 将复合单位拆分成单符号单位，m/s拆分成 m 和 s^-1, m/s^2 拆分成 m 和 s^-2
     */
    private List<UnitSymbol> splitIntoSingleSymbol() {
        List<UnitSymbol> unitSymbols = new ArrayList<>();
        Matcher matcher = CALC_JOINER.matcher(symbol);
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
     * 将单符号单位分解成幂为 1 或 -1 的基本单位。
     * 如：
     *   m    -> [m]
     *   m^2  -> [m, m]
     *   m^-2 -> [m^-1, m^-1]
     *   m/s  -> [m/s]
     */
    private List<UnitSymbol> splitPowerSymbol() {
        int absIndex = Math.abs(index());
        if (absIndex == 1) {
            return Collections.singletonList(this);
        }

        int subIndex = indexSymbol();
        UnitSymbol base = base();
        ArrayList<UnitSymbol> unitSymbols = new ArrayList<>(absIndex);
        for (int i = 0; i < absIndex; i++) {
            unitSymbols.add(base.power(subIndex));
        }
        return unitSymbols;
    }

    /**
     * 正幂返回1, 负幂返回-1, 0返回0
     */
    private int indexSymbol() {
        int index = index();
        return (index >> 31) - (-index >> 31);
    }

    /**
     * 将单位符号的幂与power相乘
     */
    UnitSymbol power(int power) {
        if (!isSingle()) {
            List<UnitSymbol> singleSymbol = splitIntoSingleSymbol();
            return singleSymbol.stream().map(s -> s.power(power)).reduce(UnitSymbol::appendWith).orElse(NIX);
        }
        int finalPower = this.index() * power;
        if (finalPower == 1) {
            return this.base();
        } else {
            return UnitSymbol.of(this.base().symbol + POWER_JOINER + finalPower);
        }
    }

    /**
     * 单位符号乘以单位符号，对于同底的单位相乘，指数相加
     */
    UnitSymbol times(UnitSymbol other) {
        Map<UnitSymbol, Integer> symbolPowerMap = singleSymbolPowerMap();

        List<UnitSymbol> otherSymbols = other.splitIntoSingleSymbol();
        // 将底数相同的单位，指数相加
        for (UnitSymbol otherSymbol : otherSymbols) {
            symbolPowerMap.merge(otherSymbol.base(), otherSymbol.index(), Integer::sum);
        }

        return buildSymbol(symbolPowerMap);
    }

    /**
     * 将单位拆分成基本符号与其指数的映射
     */
    private Map<UnitSymbol, Integer> singleSymbolPowerMap() {
        List<UnitSymbol> originSymbols = this.splitIntoSingleSymbol();
        Map<UnitSymbol, Integer> symbolPowerMap = new HashMap<>();

        for (UnitSymbol originSymbol : originSymbols) {
            symbolPowerMap.merge(originSymbol.base(), originSymbol.index(), Integer::sum);
        }
        return symbolPowerMap;
    }

    /**
     * 将基本符号与其指数的映射组合成符号
     */
    private UnitSymbol buildSymbol(Map<UnitSymbol, Integer> symbolPowerMap) {
        return symbolPowerMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != 0)
                .sorted(Comparator.comparing(e -> e.getKey().symbol))
                .map(entry -> entry.getValue() == 1 ? entry.getKey() : entry.getKey().power(entry.getValue()))
                .reduce(UnitSymbol::appendWith)
                .orElse(NIX);
    }

    private UnitSymbol appendWith(UnitSymbol symbol) {
        if (symbol == NIX || this == NIX) {
            if (symbol == NIX) {
                return this;
            }else {
                return symbol;
            }
        }
        return UnitSymbol.of(this.symbol + "*" + symbol.symbol);
    }

    /**
     * 将单位符号规范格式,并按单位字符排序
     * 如：
     *  m    -> m
     *  m*m  -> m^2
     *  m·kg -> m*kg
     *  m/s  -> m*s^-1
     */
    public UnitSymbol format() {
        Map<UnitSymbol, Integer> unitSymbolIntegerMap = singleSymbolPowerMap();
        return buildSymbol(unitSymbolIntegerMap);
    }

    public String symbol() {
        return symbol;
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
