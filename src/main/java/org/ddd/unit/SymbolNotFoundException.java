package org.ddd.unit;

/**
 * @author chenjx
 */
public class SymbolNotFoundException extends RuntimeException {
    public SymbolNotFoundException() {
        super("未查找到单位");
    }

    public SymbolNotFoundException(String message) {
        super(message);
    }
}
