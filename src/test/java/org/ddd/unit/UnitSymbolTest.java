package org.ddd.unit;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UnitSymbolTest {

    @Test
    public void isBasic() {
        UnitSymbol m = UnitSymbol.of("m");
        assertTrue(m.isBasic());

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        assertFalse(mp2.isBasic());

        UnitSymbol mds = UnitSymbol.of("m/s");
        assertFalse(mds.isBasic());

        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        assertFalse(mds.isBasic());
    }



    @Test
    public void split() {
        UnitSymbol m = UnitSymbol.of("m");
        UnitSymbol mp2 = UnitSymbol.of("m^2");
        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        UnitSymbol ns = UnitSymbol.of("s^-1");
        UnitSymbol mds2Ands = UnitSymbol.of("m/s^2*g");
        UnitSymbol ng = UnitSymbol.of("g^-1");

        List<UnitSymbol> split = m.getBasicSymbols();
        assertEquals(1, split.size());
        assertEquals(m, split.get(0));


        List<UnitSymbol> mp2Split = mp2.getBasicSymbols();
        assertEquals(2, mp2Split.size());
        assertEquals(m, mp2Split.get(0));
        assertEquals(m, mp2Split.get(1));


        List<UnitSymbol> mds2Split = mds2.getBasicSymbols();
        assertEquals(3, mds2Split.size());
        assertEquals(m, mds2Split.get(0));
        assertEquals(ns, mds2Split.get(1));
        assertEquals(ns, mds2Split.get(2));


        List<UnitSymbol> basicSymbols = mds2Ands.getBasicSymbols();
        assertEquals(4, basicSymbols.size());
        assertEquals(m, mds2Split.get(0));
        assertEquals(ns, basicSymbols.get(1));
        assertEquals(ns, basicSymbols.get(2));
        assertEquals(ng, basicSymbols.get(3));
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
    public void testEquals() {
        UnitSymbol m2 = UnitSymbol.of("m^2");
        UnitSymbol m= UnitSymbol.of("m");
        assertNotEquals(m2, m);

        UnitSymbol ms1 = UnitSymbol.of("m/s");
        UnitSymbol ms2 = UnitSymbol.of("m*s^-1");
        assertEquals(ms1, ms2);
    }
}