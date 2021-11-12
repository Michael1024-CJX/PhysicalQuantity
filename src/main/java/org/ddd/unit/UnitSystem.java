package org.ddd.unit;

/**
 * 单位制:一系列量度单位的总称。
 * 如：长度，时间，质量，面积等单位制。
 *
 * @author chenjx
 */
public interface UnitSystem {
    /**
     * 判断该单位制是否存在该物理单位
     *
     * @param symbol 待判断的单位
     * @return 是否存在
     */
    boolean containsUnit(UnitSymbol symbol);

    /**
     * 根据单位符号获取单位
     *
     * @param symbol 单位符号
     * @return 单位
     */
    Unit getUnit(UnitSymbol symbol);

    /**
     * 获取 from 到 to 之间的单位转换率
     *
     * @param from 从
     * @param to   到
     * @return 转换率
     */
    ConversionRate getConversionRate(UnitSymbol from, UnitSymbol to);

    /**
     * 将 from 与 target中原子单位底数类型相同的单位转换成统一的单位。
     * 如，为了适配 min 与 m/s 的运算，需要先将 min -> s， 再与m/s运算
     *
     * @param from 原单位
     * @param target 与之运算的单位
     * @return 适配的单位符号
     */
    UnitSymbol adapt(UnitSymbol from, UnitSymbol target);
}
