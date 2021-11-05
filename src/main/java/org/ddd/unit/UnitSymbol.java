package org.ddd.unit;

/**
 * 单位符号，用于写作
 *
 * @author chenjx
 */
public final class UnitSymbol {
    private String symbol;

    public UnitSymbol(String symbol) {
        if (symbol == null || symbol.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.symbol = symbol;
    }

    /**
     * 该单位符号是否是原子单位
     * @return
     */
    public boolean isAtomic() {
        return false;
    }

    /**
     * 将复合单位拆分成原子单位
     * @return
     */
    public UnitSymbol[] split() {
        return new UnitSymbol[0];
    }

    /**
     * 获取单位的幂
     * @return
     */
    public int power() {
        return 1;
    }

    public UnitSymbol removePower() {
        return null;
    }
}
