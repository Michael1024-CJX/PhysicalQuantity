package org.ddd.unit;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UnitSymbolTest {

    @Test
    public void isAtomic() {
        UnitSymbol m = UnitSymbol.of("m");
        assertTrue(m.isAtomic());

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        assertFalse(mp2.isAtomic());

        UnitSymbol mds = UnitSymbol.of("m/s");
        assertFalse(mds.isAtomic());

        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        assertFalse(mds.isAtomic());
    }

    @Test
    public void hasPower() {
        UnitSymbol m = UnitSymbol.of("m");
        assertFalse(m.hasPower());

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        assertTrue(mp2.hasPower());

        UnitSymbol mds = UnitSymbol.of("m/s");
        assertFalse(mds.hasPower());

        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        assertFalse(mds2.hasPower());
    }

    @Test
    public void split() {
        UnitSymbol m = UnitSymbol.of("m");
        List<UnitSymbol> split = m.split();
        assertEquals(1, split.size());
        assertEquals(m, split.get(0));

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        List<UnitSymbol> mp2Split = mp2.split();
        assertEquals(1, mp2Split.size());
        assertEquals(mp2, mp2Split.get(0));

        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        List<UnitSymbol> mds2Split = mds2.split();
        assertEquals(2, mds2Split.size());
        assertEquals(m, mds2Split.get(0));
        assertEquals(UnitSymbol.of("s^-2"), mds2Split.get(1));
    }

    @Test
    public void atomicSymbol() {

    }

    @Test
    public void power() {
        UnitSymbol m = UnitSymbol.of("m");
        assertEquals(1, m.power());

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        assertEquals(2, mp2.power());

        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        assertEquals(1, mds2.power());
    }

    @Test
    public void times() {
    }

    @Test
    public void divide() {
    }
}