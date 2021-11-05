package org.ddd.unit;

/**
 * 单位制:一系列量度单位的总称。
 * 如：长度，时间，质量，面积等单位制。
 *
 * @author chenjx
 */
public interface UnitSystem {
    /**
     * 单位制的类型，如：长度，时间，质量，面积等。
     *
     * @return 类型
     */
    Measurement type();

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
}
