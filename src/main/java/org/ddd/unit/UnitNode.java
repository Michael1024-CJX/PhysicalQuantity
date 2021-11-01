package org.ddd.unit;

/**
 * 双向链表的节点，保存当前节点单位与下一个节点单位的比率
 * 同时记录当前节点在链表中的索引
 *
 * @author chenjx
 */
public class UnitNode {
    private MeasurementUnit measurementUnit;
    /**
     * The ratio of the current unit to the unit of the next node
     */
    private Ratio ratioToNext = Ratio.ONE_RATIO;

    /**
     * 单位在当前链表中的位置，头结点为0, -1表示游离态
     */
    private int index = 0;

    /**
     * false表示游离状态，表示不存在转换关系，无法转换
     * true表示被管理，存在转换关系
     */
    private boolean managed;

    /**
     * next node
     */
    private UnitNode next;

    /**
     * prev node
     */
    private UnitNode prev;


    public UnitNode(MeasurementUnit measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public MeasurementUnit unit() {
        return measurementUnit;
    }

    public Ratio ratioToNext() {
        return ratioToNext;
    }

    public UnitNode next() {
        return next;
    }

    public UnitNode prev() {
        return prev;
    }

    public boolean hasNext() {
        return next != null;
    }

    public boolean hasPrev() {
        return prev != null;
    }

    /**
     * 标准的保存或更新转换率，
     * 两个单位都在同一条链上的时候，此时的分子是链表中较前的节点。
     * 若两个单位在不同的链上，则将两条链头尾相连，计算连接位置的转换率即可
     */
    public void setRatioToTarget(UnitNode targetNode, Ratio ratioToTarget) {
        // 如果两个单位都是游离态，直接设置转换率
        if (!this.isManaged() && !targetNode.isManaged()) {
            setNext(targetNode, ratioToTarget);
            return;
        }

        // 同一条链的情况下，由处于链表较前端的节点开始计算
        if (this.index() > targetNode.index()) {
            targetNode.setRatioToTarget(this, ratioToTarget.reciprocal());
            return;
        }

        // 非同链的情况下，将指针指向该链的头部节点，并转换比率
        while (targetNode.hasPrev()) {
            // 先移动指针再计算比率
            targetNode = targetNode.prev();
            ratioToTarget = ratioToTarget.times(targetNode.ratioToNext().reciprocal());
        }

        // 循环找到当前链的尾节点，并计算转换率
        UnitNode fromNode = this;
        while (fromNode.hasNext()) {
            // 先计算比率再移动指针
            ratioToTarget = ratioToTarget.times(fromNode.ratioToNext().reciprocal());
            fromNode = fromNode.next();
        }

        fromNode.setNext(targetNode, ratioToTarget);
    }

    /**
     * 设置下一个节点和与下一个节点的比值，存在关联的节点
     *
     * @param next        下一个节点
     * @param ratioToNext 当前节点与下一个节点的比率
     */
    private void setNext(UnitNode next, Ratio ratioToNext) {
        next.setIndex(index + 1);
        next.setPrev(this);
        next.beManaged();
        this.beManaged();
        this.next = next;
        this.ratioToNext = ratioToNext;
    }

    private void setPrev(UnitNode prev) {
        this.prev = prev;
    }

    private void setIndex(int index) {
        this.index = index;
        if (hasNext()) {
            next.setIndex(index + 1);
        }
    }


    Ratio calculateRatioToTarget(UnitNode target) {
        if (this.index > target.index) {
            Ratio ratio = target.calculateRatioToTarget(this);
            return ratio.reciprocal();
        }
        UnitNode fromNode = this;
        Ratio ratio = Ratio.ONE_RATIO;

        for (int cur = this.index(); cur < target.index(); cur++) {
            if (fromNode == null) {
                return null;
            }
            ratio = ratio.times(fromNode.ratioToNext());
            fromNode = fromNode.next();
        }

        return ratio;
    }

    public int index() {
        return index;
    }

    public boolean isManaged() {
        return managed;
    }

    public void beManaged() {
        this.managed = true;
    }

    @Override
    public String toString() {
        return "UnitNode{" +
                "unit=" + unit().symbol() +
                ", ratioToNext=" + ratioToNext +
                ", index=" + index +
                ", prev=" + (prev == null ? null :prev.unit().symbol()) +
                ", next=" + (next == null ? null :next.unit().symbol()) +
                '}';
    }
}
