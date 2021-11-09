package org.ddd.unit;

import org.ddd.unit.impl.DefaultUnitFactory;
import org.ddd.unit.impl.YAMLUnitRegister;
import org.ddd.util.NumberUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author chenjx
 */
public class PhysicalQuantityTest {
    private QuantityFactory quantityFactory;
    private UnitFactory unitFactory;
    @Before
    public void init() {
        YAMLUnitRegister register = new YAMLUnitRegister(new File("src/test/resources/unit/"));
        unitFactory = new DefaultUnitFactory(register);
        quantityFactory = new QuantityFactory(unitFactory);
    }

    @Test
    public void testCreateQuantity() {
        PhysicalQuantity height = quantityFactory.of(180, "cm");
        assertEquals(0, NumberUtil.compare(180, height.getAmount()));
        assertEquals(unitFactory.getUnit("cm"), height.getUnit());


        PhysicalQuantity area = quantityFactory.of(20, "m^2");
        assertEquals(0, NumberUtil.compare(20, area.getAmount()));
        assertEquals(unitFactory.getUnit("m^2"), area.getUnit());


        PhysicalQuantity acceleration = quantityFactory.of(20, "m/s^2");
        assertEquals(0, NumberUtil.compare(20, acceleration.getAmount()));
        assertEquals(unitFactory.getUnit("m/s^2"), acceleration.getUnit());

    }

    @Test
    public void testConvertTo() {
        PhysicalQuantity height = quantityFactory.of(180, "cm");
        PhysicalQuantity heightByM = height.convertTo("m");
        assertEquals(0, NumberUtil.compare(1.8, heightByM.getAmount()));
        assertEquals(unitFactory.getUnit("m"), heightByM.getUnit());

        PhysicalQuantity ms = quantityFactory.of(10, "m/s");
        PhysicalQuantity kmh = ms.convertTo("km/h");
        assertEquals(0, NumberUtil.compare(36, kmh.getAmount()));
        assertEquals(unitFactory.getUnit("km/h"), kmh.getUnit());
    }

    @Test
    public void testAdd() {
        PhysicalQuantity expected = quantityFactory.of(185, "cm");

        PhysicalQuantity height = quantityFactory.of(1.8, "m");
        PhysicalQuantity hairHeight = quantityFactory.of(5, "cm");
        PhysicalQuantity sum = height.add(hairHeight);

        assertEquals(0, sum.compareTo(expected));
    }

    @Test
    public void testMultiplyNumber() {
        PhysicalQuantity expected = quantityFactory.of(200, "cm");

        PhysicalQuantity height = quantityFactory.of(1, "m");

        PhysicalQuantity multiply = height.multiply(2);

        assertEquals(0, multiply.compareTo(expected));
    }

    @Test
    public void testMultiplyQuantity() {
        PhysicalQuantity length1 = quantityFactory.of(1, "m");
        PhysicalQuantity length2 = quantityFactory.of(2, "m");
        PhysicalQuantity area = length1.multiply(length2);
        assertEquals(0, NumberUtil.compare(2, area.getAmount()));
        assertEquals(unitFactory.getUnit("m^2"), area.getUnit());

        PhysicalQuantity ms = quantityFactory.of(10, "m/s");
        PhysicalQuantity length3 = ms.multiply(quantityFactory.of(2, "s"));
        assertEquals(0, NumberUtil.compare(20, length3.getAmount()));
        assertEquals(unitFactory.getUnit("m"), length3.getUnit());


        PhysicalQuantity length4 = ms.multiply(quantityFactory.of(2, "min"));
        assertEquals(0, NumberUtil.compare(1200, length4.getAmount()));
        assertEquals(unitFactory.getUnit("m"), length4.getUnit());

        PhysicalQuantity gs = quantityFactory.of(10, "g/s");
        PhysicalQuantity kgm = quantityFactory.of(10, "kg*min");
        PhysicalQuantity kgms = gs.multiply(kgm);
        assertEquals(0, NumberUtil.compare(6000000, kgms.getAmount()));
        assertEquals(unitFactory.getUnit("g^2"), kgms.getUnit());
    }

    @Test
    public void testDivideNumber() {
        PhysicalQuantity height = quantityFactory.of(1, "m");
        PhysicalQuantity divide = height.divide(2);
        assertEquals(0, NumberUtil.compare(0.5, divide.getAmount()));
        assertEquals(unitFactory.getUnit("m"), divide.getUnit());
    }

    @Test
    public void testDivideQuantity() {
        PhysicalQuantity length = quantityFactory.of(1000, "m");
        PhysicalQuantity s = quantityFactory.of(50, "s");
        PhysicalQuantity speed = length.divide(s);
        assertEquals(0, NumberUtil.compare(20, speed.getAmount()));
        assertEquals(unitFactory.getUnit("m/s"), speed.getUnit());

        PhysicalQuantity s2 = quantityFactory.of(1, "min");
        PhysicalQuantity acceleration = speed.divide(s2);
        assertEquals(0,
                NumberUtil.compare(
                        BigDecimal.ONE.divide(BigDecimal.valueOf(3), NumberUtil.DEFAULT),
                        acceleration.getAmount()));
        assertEquals(unitFactory.getUnit("m/s^2"), acceleration.getUnit());
    }

    @Test
    public void testCompareTo() {
        PhysicalQuantity m = quantityFactory.of(1, "m");
        PhysicalQuantity cm = quantityFactory.of(100, "cm");

        assertEquals(0, m.compareTo(cm));
    }
}
