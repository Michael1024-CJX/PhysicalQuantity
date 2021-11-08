package org.ddd.unit;

import java.util.Collection;
import java.util.List;

/**
 * 量度单位的容器
 * 用于存储同量度的单位
 * 以及计算同量度单位之间的转换率
 *
 * @author chenjx
 */
public interface UnitContainer {
    /**
     * 判断该容器内是否已保存该物理单位
     *
     * @param unit 待判断的物理单位
     * @return 是否已存在
     */
    boolean contains(Unit unit);

    /**
     * 判断该容器内是否已保存该物理单位
     *
     * @param unitSymbol 待判断的物理单位
     * @return 是否已存在
     */
    boolean contains(UnitSymbol unitSymbol);

    /**
     * 通过唯一的物理量单位的符号查询单位
     *
     * @param symbol 物理单位的符号
     * @return 物理单位
     */
    Unit getUnit(UnitSymbol symbol);

    /**
     * 计算两个单位之间的转换率，
     * 如果两个单位不属于同物理量，或容器不知道这两个单位之间的转换率时，返回null
     *
     * @param numeratorUnit   分子单位
     * @param denominatorUnit 分母单位
     * @return 比值
     */
    Ratio calculateRatio(UnitSymbol numeratorUnit, UnitSymbol denominatorUnit);

    /**
     * 注册一个新单位，如果符号已存在，则不会替换
     *
     * @param unit 注册的单位
     */
    void addUnit(Unit unit);

    /**
     * 注册两个单位之间的转换率
     *
     * @param numeratorUnit 分子单位
     * @param denominatorUnit 分母单位
     * @param ratio 比率
     */
    void addConversionRate(UnitSymbol numeratorUnit, UnitSymbol denominatorUnit, Ratio ratio);
}
