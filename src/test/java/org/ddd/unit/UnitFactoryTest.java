package org.ddd.unit;

import org.ddd.unit.impl.DefaultUnitFactory;
import org.ddd.unit.impl.YAMLUnitRegister;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author chenjx
 */
public class UnitFactoryTest {
    private UnitFactory factory;

    @Before
    public void before() {
        UnitRegister unitRegister = new YAMLUnitRegister();
        factory = new DefaultUnitFactory(unitRegister);
    }

    @Test
    public void testMConvertToRuler() throws IOException {
        Unit m = factory.getUnit("m");
        Unit ruler = factory.getUnit("尺");
        ConversionRate ratioOfMToRuler = m.convertTo(ruler.getSymbol());

        BigDecimal ratioValue = ratioOfMToRuler.getRatio().decimalValue(0, BigDecimal.ROUND_HALF_UP);
        assertEquals("米与尺的比例不符合预期", 0, new BigDecimal(3).compareTo(ratioValue));
    }

    @Test
    public void testSConvertToH() {
        Unit s = factory.getUnit("s");
        Unit h = factory.getUnit("h");

        ConversionRate ratioOfSToH = s.convertTo(h.getSymbol());
        BigDecimal ratioValue = ratioOfSToH.getRatio().decimalValue(5, BigDecimal.ROUND_HALF_UP);
        assertEquals("秒与时的比例不符合预期", 0, BigDecimal.ONE.divide(new BigDecimal(3600), 5, BigDecimal.ROUND_HALF_UP).compareTo(ratioValue));
    }

    @Test
    public void testHConvertToH() {
        Unit h = factory.getUnit("h");
        ConversionRate ratioOfHToH = h.convertTo(h.getSymbol());
        BigDecimal ratioValue = ratioOfHToH.getRatio().decimalValue(0, BigDecimal.ROUND_HALF_UP);
        assertEquals("时与时的比例不符合预期", 0, BigDecimal.ONE.compareTo(ratioValue));
    }

    @Test
    public void testHConvertToM() {
        Unit h = factory.getUnit("h");
        Unit m = factory.getUnit("尺");

        ConversionRate ratioOfHToM = h.convertTo(m.getSymbol());

        assertNull("不同物理量类型的单位不可互相转换", ratioOfHToM);
    }

    @Test
    public void testIsSameType() {
        Unit m = factory.getUnit("m");
        UnitSymbol symbol = UnitSymbol.of("尺");
        assertTrue(m.isSameTypeAs(symbol));


        Unit h = factory.getUnit("h");
        assertFalse(h.isSameTypeAs(symbol));

        Unit ms = factory.getUnit("m/s");
        assertFalse(ms.isSameTypeAs(m.getSymbol()));
        assertFalse(m.isSameTypeAs(ms.getSymbol()));

        Unit mas = factory.getUnit("m*s");
        assertFalse(mas.isSameTypeAs(m.getSymbol()));
        assertFalse(m.isSameTypeAs(mas.getSymbol()));
    }
}
