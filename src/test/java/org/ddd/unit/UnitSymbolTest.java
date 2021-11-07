package org.ddd.unit;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UnitSymbolTest {

    @Test
    public void isAtomic() {
        UnitSymbol m = UnitSymbol.of("m");
        assertTrue(m.isSingleSymbol());

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        assertTrue(mp2.isSingleSymbol());

        UnitSymbol mds = UnitSymbol.of("m/s");
        assertFalse(mds.isSingleSymbol());

        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        assertFalse(mds.isSingleSymbol());
    }

    @Test
    public void split() {
        UnitSymbol m = UnitSymbol.of("m");
        List<UnitSymbol> split = m.splitIntoSingleSymbol();
        assertEquals(1, split.size());
        assertEquals(m, split.get(0));

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        List<UnitSymbol> mp2Split = mp2.splitIntoSingleSymbol();
        assertEquals(1, mp2Split.size());
        assertEquals(mp2, mp2Split.get(0));

        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        List<UnitSymbol> mds2Split = mds2.splitIntoSingleSymbol();
        assertEquals(2, mds2Split.size());
        assertEquals(m, mds2Split.get(0));
        assertEquals(UnitSymbol.of("s^-2"), mds2Split.get(1));

        UnitSymbol mds2And = UnitSymbol.of("m/s^2*s");
        System.out.println(mds2And.splitIntoSingleSymbol());

    }

    @Test
    public void base() {
        UnitSymbol m = UnitSymbol.of("m");
        assertEquals(UnitSymbol.of("m"), m.base());

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        assertEquals(UnitSymbol.of("m"), mp2.base());

        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        assertEquals(UnitSymbol.of("m/s^2"), mds2.base());
    }

    @Test
    public void power() {
        UnitSymbol m = UnitSymbol.of("m");
        assertEquals(1, m.index());

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        assertEquals(2, mp2.index());

        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        assertEquals(1, mds2.index());
    }

    @Test
    public void times() {
        UnitSymbol m1 = UnitSymbol.of("m");
        UnitSymbol m2= UnitSymbol.of("m");
        assertEquals(UnitSymbol.of("m^2"), m1.times(m2));

        UnitSymbol ms = UnitSymbol.of("m/s");
        UnitSymbol s = UnitSymbol.of("s");
        assertEquals(UnitSymbol.of("m"), ms.times(s));
    }

    @Test
    public void divide() {
        UnitSymbol m2 = UnitSymbol.of("m^2");
        UnitSymbol m= UnitSymbol.of("m");
        assertEquals(UnitSymbol.of("m"), m2.divide(m));


        UnitSymbol s = UnitSymbol.of("s");
        assertEquals(UnitSymbol.of("m*s^-1"), m.divide(s));
    }
}