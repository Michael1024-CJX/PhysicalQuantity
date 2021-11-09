package org.ddd.unit;

import org.ddd.unit.impl.DefaultUnitFactory;
import org.ddd.unit.impl.YAMLUnitRegister;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author chenjx
 */
public class UnitFactoryTest {
    private UnitFactory factory;

    @Before
    public void before() {
        UnitRegister unitRegister = new YAMLUnitRegister(new File("src/test/resources/unit/"));
        factory = new DefaultUnitFactory(unitRegister);
    }

    @Test
    public void testMConvertToRuler() throws IOException {
        Unit m = factory.getUnit("m");
        Unit ruler = factory.getUnit("尺");
        ConversionRate ratioOfMToRuler = m.convertTo(ruler);

        BigDecimal ratioValue = ratioOfMToRuler.getRatio().decimalValue(0, BigDecimal.ROUND_HALF_UP);
        Assert.assertEquals("米与尺的比例不符合预期", 0, new BigDecimal(3).compareTo(ratioValue));
    }

    @Test
    public void testSConvertToH() {
        Unit s = factory.getUnit("s");
        Unit h = factory.getUnit("h");

        ConversionRate ratioOfSToH = s.convertTo(h);
        BigDecimal ratioValue = ratioOfSToH.getRatio().decimalValue(5, BigDecimal.ROUND_HALF_UP);
        Assert.assertEquals("秒与时的比例不符合预期", 0, BigDecimal.ONE.divide(new BigDecimal(3600), 5, BigDecimal.ROUND_HALF_UP).compareTo(ratioValue));
    }

    @Test
    public void testHConvertToH() {
        Unit h = factory.getUnit("h");
        ConversionRate ratioOfHToH = h.convertTo(h);
        BigDecimal ratioValue = ratioOfHToH.getRatio().decimalValue(0, BigDecimal.ROUND_HALF_UP);
        Assert.assertEquals("时与时的比例不符合预期", 0, BigDecimal.ONE.compareTo(ratioValue));
    }

    @Test
    public void testHConvertToM() {
        Unit h = factory.getUnit("h");
        Unit m = factory.getUnit("m");

        ConversionRate ratioOfHToM = h.convertTo(m);

        Assert.assertNull("不同物理量类型的单位不可互相转换", ratioOfHToM);
    }

    @Test
    public void testIsSameType() {
//        Unit m = factory.getUnit("m");
//        Unit ruler = factory.getUnit("尺");
//        Assert.assertTrue(m.isSameSystemFor(ruler));
//
//
//        Unit h = factory.getUnit("h");
//        Assert.assertFalse(m.isSameSystemFor(h));
    }
}
