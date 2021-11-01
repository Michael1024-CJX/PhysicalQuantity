package org.ddd.unit;

import java.util.Collection;

/**
 * 量度单位的容器
 * 用于存储同量度的单位
 * 以及计算同量度单位之间的转换率
 *
 * @author chenjx
 */
public interface MeasurementUnitContainer {
    /**
     * 判断该容器内是否已保存该物理单位
     *
     * @param unit 待判断的物理单位
     * @return 是否已存在
     */
    boolean contains(MeasurementUnit unit);

    /**
     * 判断该容器内是否已保存该物理单位
     *
     * @param unitSymbol 待判断的物理单位
     * @return 是否已存在
     */
    boolean contains(String unitSymbol);

    /**
     * 通过唯一的物理量单位的符号查询单位
     *
     * @param symbol 物理单位的符号
     * @return 物理单位
     */
    MeasurementUnit getUnitBySymbol(String symbol);

    /**
     * 计算两个单位之间的转换率，
     * 如果两个单位不属于同物理量，或容器不知道这两个单位之间的转换率时，返回null
     *
     * @param numeratorUnit   分子单位
     * @param denominatorUnit 分母单位
     * @return 比值
     */
    Ratio calculateRatio(MeasurementUnit numeratorUnit, MeasurementUnit denominatorUnit);

    /**
     * 注册一个新单位，如果符号已存在，则不会替换
     *
     * @param symbol 单位符号，唯一的
     * @param alias  单位名称
     * @return 注册成功的单位
     */
    MeasurementUnit registerUnit(String symbol, String alias);

    /**
     * 注册两个单位之间的转换率
     *
     * @param numeratorUnit 分子单位
     * @param denominatorUnit 分母单位
     * @param ratio 比率
     */
    void registerConversionRate(String numeratorUnit, String denominatorUnit, Ratio ratio);

    /**
     * 返回一个测量的所有单位
     * @return
     */
    Collection<String> allUnitSymbol();
}
