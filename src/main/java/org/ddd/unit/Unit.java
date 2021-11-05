package org.ddd.unit;

/**
 * 量度单位，根据量度的类型{@link Measurement}划分。
 * 与同量度的单位之间可以互相转换。
 * 单位具有唯一的符号。
 *
 * @author chenjx
 */
public interface Unit {
    /**
     * 单位的别名
     *
     * @return 单位的别名
     */
    String alias();

    /**
     * 单位的符号，具有唯一性
     *
     * @return 单位的符号
     */
    UnitSymbol symbol();

    /**
     * 度量，如长度，质量，时间等
     *
     * @return 度量
     */
    Measurement type();

    /**
     * 设置度量
     * @param measurement 度量
     */
    void setMeasurement(Measurement measurement);

    /**
     * 判断是否与该物理单位是否同类型
     *
     * @param target 待比较的物理单位
     * @return 是否是同类型的单位
     */
    boolean isSameTypeFor(Unit target);

    /**
     * 获取与目标单位的的比率，需要两个单位属于同物理量
     *
     * @param target 目标单位
     * @return 自身与目标单位的比率
     */
    ConversionRate convertTo(Unit target);

    default boolean equals(Unit o) {
        return this.symbol().equals(o.symbol());
    }
}
