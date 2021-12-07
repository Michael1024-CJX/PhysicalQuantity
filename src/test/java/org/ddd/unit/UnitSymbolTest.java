package org.ddd.unit;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UnitSymbolTest {
    @Test
    public void testCreateSymbol() {
        UnitSymbol m = UnitSymbol.of("m");
        assertEquals("m", m.symbol());

        UnitSymbol ms = UnitSymbol.of("m*s");
        assertEquals("m*s", ms.symbol());
    }

    @Test
    public void testEquals() {
        UnitSymbol m1 = UnitSymbol.of("m^1");
        UnitSymbol m2= UnitSymbol.of("m");
        assertEquals(m1, m2);

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        UnitSymbol m= UnitSymbol.of("m");
        assertNotEquals(mp2, m);

        UnitSymbol ms1 = UnitSymbol.of("m/s");
        UnitSymbol ms2 = UnitSymbol.of("m*s^-1");
        assertEquals(ms1, ms2);
    }

    @Test
    public void isBasic() {
        UnitSymbol m = UnitSymbol.of("m");
        assertTrue(m.isBasic());

        UnitSymbol mp2 = UnitSymbol.of("m^2");
        assertFalse(mp2.isBasic());

        UnitSymbol mds = UnitSymbol.of("m/s");
        assertFalse(mds.isBasic());

        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        assertFalse(mds2.isBasic());
    }



    @Test
    public void basicSymbols() {
        UnitSymbol m = UnitSymbol.of("m");


        List<UnitSymbol> split = m.basicSymbols();
        assertEquals(1, split.size());
        assertEquals(m, split.get(0));


        UnitSymbol mp2 = UnitSymbol.of("m^2");
        List<UnitSymbol> mp2Split = mp2.basicSymbols();
        assertEquals(2, mp2Split.size());
        assertEquals(m, mp2Split.get(0));
        assertEquals(m, mp2Split.get(1));


        UnitSymbol mds2 = UnitSymbol.of("m/s^2");
        UnitSymbol ns = UnitSymbol.of("s^-1");
        List<UnitSymbol> mds2Split = mds2.basicSymbols();
        assertEquals(3, mds2Split.size());
        assertEquals(m, mds2Split.get(0));
        assertEquals(ns, mds2Split.get(1));
        assertEquals(ns, mds2Split.get(2));


        UnitSymbol mds2Ands = UnitSymbol.of("m/s^2*g");
        UnitSymbol ng = UnitSymbol.of("g^-1");
        List<UnitSymbol> basicSymbols = mds2Ands.basicSymbols();
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
    public void index() {
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
    public void power() {
        UnitSymbol m = UnitSymbol.of("m");
        UnitSymbol m2 = m.power(2);
        assertEquals(UnitSymbol.of("m^2"), m2);


        UnitSymbol nm = UnitSymbol.of("m^-1");
        UnitSymbol nm2 = nm.power(2);
        assertEquals(UnitSymbol.of("m^-2"), nm2);

        UnitSymbol ms = UnitSymbol.of("m/s");
        UnitSymbol ms2 = ms.power(2);
        assertEquals(UnitSymbol.of("m^2*s^-2"), ms2);

        UnitSymbol nms = UnitSymbol.of("m/s");
        UnitSymbol nms2 = nms.power(-2);
        assertEquals(UnitSymbol.of("m^-2*s^2"), nms2);
    }
}