package org.ddd.unit;

import java.util.Objects;

/**
 * 量度，如长度，质量，速度等。
 * 量度是单位的信息专家，拥有单位的知识。
 * 只有同量度的单位之间能够转换。
 *
 * @author chenjx
 */
public class Measurement {
    static final Measurement UNKNOWN = new Measurement("UNKNOWN");
    /**
     * 量度的类型
     */
    private String type;

    private Measurement(String type) {
        this.type = type;
    }

    public static Measurement of(String type) {
        return new Measurement(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement measurement = (Measurement) o;
        return Objects.equals(type, measurement.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return type;
    }

    public String type() {
        return type;
    }
}
