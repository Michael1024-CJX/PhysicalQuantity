package org.ddd.unit;

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
public class LinkedMapContainer implements UnitContainer {
    /**
     * key:unit symbol  value:UnitNode
     */
    private Map<UnitSymbol, UnitNode> unitMap = new HashMap<>();

    public LinkedMapContainer() {
    }

    @Override
    public boolean contains(Unit unit) {
        return unitMap.containsKey(unit.symbol());
    }

    @Override
    public boolean contains(UnitSymbol unitSymbol) {
        return unitMap.containsKey(unitSymbol);
    }

    @Override
    public Unit getUnit(UnitSymbol symbol) {
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
    public Ratio calculateRatio(UnitSymbol numeratorUnit, UnitSymbol denominatorUnit) {
        if (!contains(numeratorUnit) || !contains(denominatorUnit)) {
            return null;
        }

        UnitNode fromNode = unitMap.get(numeratorUnit);
        UnitNode toNode = unitMap.get(denominatorUnit);
        return fromNode.calculateRatioToTarget(toNode);
    }

    @Override
    public void addUnit(Unit unit) {
        unitMap.putIfAbsent(unit.symbol(), new UnitNode(unit));
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
    public void addConversionRate(UnitSymbol numeratorUnit, UnitSymbol denominatorUnit, Ratio ratio) {
        if (!contains(numeratorUnit) || !contains(denominatorUnit)) {
            throw new IllegalArgumentException("存在无效的单位");
        }

        UnitNode fromNode = unitMap.get(numeratorUnit);
        UnitNode toNode = unitMap.get(denominatorUnit);
        fromNode.setRatioToTarget(toNode, ratio);
    }

    @Override
    public String toString() {
        return "LinkedMapContainer{" +
                "unitMap=" + unitMap +
                '}';
    }
}
