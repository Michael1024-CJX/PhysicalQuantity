package org.ddd.unit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于存储同类型的节点，同类型的节点之间可以存在转换率
 * 通过链表连接表示存在转换率
 * 没有指针指向的节点为游离节点，不可与其他单位转换
 * <p>
 *
 * @author chenjx
 * @see UnitNode 链表的节点
 */
public class LinkedMapContainer implements MeasurementUnitContainer {
    /**
     * key:unit symbol  value:UnitNode
     */
    private Map<String, UnitNode> unitMap = new HashMap<>();
    private Measurement measurement;


    public LinkedMapContainer(Measurement measurement) {
        this.measurement = measurement;
        this.measurement.setMeasurementUnitContainer(this);
    }

    @Override
    public boolean contains(MeasurementUnit unit) {
        return unitMap.containsKey(unit.symbol());
    }

    @Override
    public boolean contains(String unitSymbol) {
        return unitMap.containsKey(unitSymbol);
    }

    @Override
    public MeasurementUnit getUnitBySymbol(String symbol) {
        if (!unitMap.containsKey(symbol)) {
            return null;
        }
        UnitNode unitNode = unitMap.get(symbol);
        return unitNode.unit();
    }

    /**
     * 计算两个单位之间的转换率
     * 每个节点都保存与其下一个节点的比率，
     * 如果from 和 to 之间跨越多个节点，则通过比值相乘获取最终的比率
     * 默认情况下，from在链表中的位置比to 靠前，
     * 如果不是这样，则先交换 from 与 to，待算出比率后，将其置位倒数就是最终的比率
     *
     * @param numeratorUnit   分子单位
     * @param denominatorUnit 分母单位
     * @return 转换率
     */
    public Ratio calculateRatio(MeasurementUnit numeratorUnit, MeasurementUnit denominatorUnit) {
        if (!contains(numeratorUnit) || !contains(denominatorUnit)) {
            return null;
        }

        UnitNode fromNode = unitMap.get(numeratorUnit.symbol());
        UnitNode toNode = unitMap.get(denominatorUnit.symbol());
        return fromNode.calculateRatioToTarget(toNode);
    }

    @Override
    public MeasurementUnit registerUnit(String symbol, String alias) {
        AtomicUnit atomicUnit = new AtomicUnit(symbol, alias, this.measurement);
        unitMap.putIfAbsent(symbol, new UnitNode(atomicUnit));
        return getUnitBySymbol(symbol);
    }

    /**
     * 保存或更新转换率。
     * 对于两个单位节点都处于游离状态时，可以直接指定两者的转换率。
     * 对于至少有一方是被管理的状态的节点，计算两个节点之间的路径的转换率
     *
     * @param numeratorUnit   分子单位
     * @param denominatorUnit 分母单位
     * @param ratio           比率
     */
    @Override
    public void registerConversionRate(String numeratorUnit, String denominatorUnit, Ratio ratio) {
        if (!contains(numeratorUnit) || !contains(denominatorUnit)) {
            throw new IllegalArgumentException("存在无效的单位");
        }

        UnitNode fromNode = unitMap.get(numeratorUnit);
        UnitNode toNode = unitMap.get(denominatorUnit);
        fromNode.setRatioToTarget(toNode, ratio);
    }

    @Override
    public Collection<String> allUnitSymbol() {
        return unitMap.keySet();
    }

    @Override
    public String toString() {
        return "LinkedMapContainer{" +
                "unitMap=" + unitMap +
                '}';
    }
}
