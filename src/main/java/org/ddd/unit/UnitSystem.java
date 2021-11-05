package org.ddd.unit;

import java.util.List;

/**
 * 单位制:一系列量度单位的总称。
 * 如：长度，时间，质量，面积等。
 *
 * @author chenjx
 */
public interface UnitSystem {

    Measurement type();

    /**
     * 返回同单位制的所有单位
     *
     * @return 同单位制的所有单位
     */
    List<Unit> allUnits();

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
     * @param to 到
     * @return 转换率
     */
    ConversionRate getConversionRate(Unit from, Unit to);
}
