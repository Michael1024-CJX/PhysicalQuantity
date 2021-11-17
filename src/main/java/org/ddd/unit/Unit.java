package org.ddd.unit;

import java.util.Iterator;

/**
 * @author chenjx
 */
public interface Unit {
    /**
     * 获取单位符号
     */
    UnitSymbol getSymbol();

    /**
     * 判断两个单位是否是同类型单位
     */
    boolean isSameTypeAs(UnitSymbol symbol);

    /**
     * 转换为目标单位，两个单位需要是同类型单位，
     * 返回两个单位的转换率，分子是原单位，分母是目标单位，Ratio是原单位与目标单位的比率
     */
    ConversionRate convertTo(UnitSymbol target);

    /**
     * 将单位中与目标单位具有同类型的基本单位做转换，转换成能与目标单位运算的单位，并返回转换率
     * 若不需要转换，Ratio为 1。
     * 如: 当km与m/s运算时，将m/s转换为 km/s
     */
    ConversionRate adaptTo(UnitSymbol target);

    /**
     * 返回单位指数相反的单位
     * 如： m -> m^-1, m/s -> m^-1*s
     */
    Unit opposite();

    /**
     * 用于创建组合模式的迭代器
     */
    default Iterator<Unit> iterator() {
        return null;
    }

    /**
     * 通过 UnitSymbol 比较两个单位是否相同
     */
    default boolean isEquals(Unit another) {
        return another.getSymbol().equals(getSymbol());
    }
}
