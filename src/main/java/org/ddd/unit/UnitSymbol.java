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
     * 将复合单位拆分成原子单位，m/s拆分成 m 和 s^-1, m^2拆分成 m m.
     * @return
     */
    public UnitSymbol[] split() {
        return new UnitSymbol[0];
    }

    public UnitSymbol atomicSymbol() {
        return null;
    }

    public int power() {
        return 1;
    }

    public String symbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol();
    }
}
