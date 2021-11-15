package org.ddd.unit;

import org.ddd.unit.impl.DefaultUnitFactory;
import org.ddd.unit.impl.YAMLUnitRegister;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;

import static org.ddd.util.NumberUtil.*;
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
        assertEquals(0,  compare(180, height.getAmount()));
        assertTrue(unitFactory.getUnit("cm").isEquals(height.getUnit()));


        PhysicalQuantity area = quantityFactory.of(20, "m^2");
        assertEquals(0,  compare(20, area.getAmount()));
        assertTrue(unitFactory.getUnit("m^2").isEquals(area.getUnit()));

        PhysicalQuantity acceleration = quantityFactory.of(20, "m/s^2");
        assertEquals(0,  compare(20, acceleration.getAmount()));
        assertTrue(unitFactory.getUnit("m/s^2").isEquals(acceleration.getUnit()));
    }

    @Test
    public void testConvertTo() {
        PhysicalQuantity height = quantityFactory.of(180, "cm");
        PhysicalQuantity heightByM = height.convertTo(UnitSymbol.of("m"));
        assertEquals(0,  compare(1.8, heightByM.getAmount()));
        assertTrue(unitFactory.getUnit("m").isEquals(heightByM.getUnit()));

        PhysicalQuantity ms = quantityFactory.of(10, "m/s");
        PhysicalQuantity kmh = ms.convertTo(UnitSymbol.of("km/h"));
        assertEquals(0,  compare(36, kmh.getAmount()));
        assertTrue(unitFactory.getUnit("km/h").isEquals(kmh.getUnit()));
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
        assertEquals(0,  compare(2, area.getAmount()));
        assertTrue(unitFactory.getUnit("m^2").isEquals(area.getUnit()));

        PhysicalQuantity ms = quantityFactory.of(10, "m/s");
        PhysicalQuantity length3 = ms.multiply(quantityFactory.of(2, "s"));
        assertEquals(0,  compare(20, length3.getAmount()));
        assertTrue(unitFactory.getUnit("m").isEquals(length3.getUnit()));


        PhysicalQuantity length4 = ms.multiply(quantityFactory.of(2, "min"));
        assertEquals(0,  compare(1200, length4.getAmount()));
        assertTrue(unitFactory.getUnit("m").isEquals(length4.getUnit()));

        PhysicalQuantity gs = quantityFactory.of(10, "g/s");
        PhysicalQuantity kgm = quantityFactory.of(10, "kg*min");
        PhysicalQuantity kgms = gs.multiply(kgm);
        assertEquals(0,  compare(6000000, kgms.getAmount()));
        assertTrue(unitFactory.getUnit("g^2").isEquals(kgms.getUnit()));
    }

    @Test
    public void testDivideNumber() {
        PhysicalQuantity height = quantityFactory.of(1, "m");
        PhysicalQuantity divide = height.divide(2);
        assertEquals(0,  compare(0.5, divide.getAmount()));
        assertTrue(unitFactory.getUnit("m").isEquals(divide.getUnit()));
    }

    @Test
    public void testMultiplyAndDivide() {
        PhysicalQuantity length = quantityFactory.of(100, "m");
        PhysicalQuantity time = quantityFactory.of(20, "s");
        PhysicalQuantity speed = length.divide(time);

        PhysicalQuantity spend = quantityFactory.of(2, "min");
        PhysicalQuantity result = speed.multiply(spend);

        assertEquals(0,  compare(600, result.getAmount()));
        assertTrue(unitFactory.getUnit("m").isEquals(result.getUnit()));

        PhysicalQuantity physicalQuantity = speed.convertTo(UnitSymbol.of("km/h"));
        assertEquals(0,  compare(18, physicalQuantity.getAmount()));
        assertTrue(unitFactory.getUnit("km/h").isEquals(physicalQuantity.getUnit()));


        PhysicalQuantity s = physicalQuantity.multiply(quantityFactory.of(3600, "s"));
        assertEquals(0,  compare(18, s.getAmount()));
        assertTrue(unitFactory.getUnit("km").isEquals(s.getUnit()));
    }

    @Test
    public void testDivideQuantity() {
        PhysicalQuantity length = quantityFactory.of(1000, "m");
        PhysicalQuantity s = quantityFactory.of(50, "s");
        PhysicalQuantity speed = length.divide(s);
        assertEquals(0, compare(20, speed.getAmount()));
        assertTrue(unitFactory.getUnit("m/s").isEquals(speed.getUnit()));

        PhysicalQuantity s2 = quantityFactory.of(1, "min");
        PhysicalQuantity acceleration = speed.divide(s2);
        assertEquals(0,
                compare(
                        BigDecimal.ONE.divide(BigDecimal.valueOf(3),  DEFAULT),
                        acceleration.getAmount()));
        assertTrue(unitFactory.getUnit("m/s^2").isEquals(acceleration.getUnit()));
    }

    @Test
    public void complexCalculation() {
        PhysicalQuantity m = quantityFactory.of(800, "m");
        PhysicalQuantity ms = quantityFactory.of(40, "s");
        PhysicalQuantity mss = quantityFactory.of(2, "s");

        PhysicalQuantity speed = m.divide(ms);
        PhysicalQuantity acceleration = speed.divide(mss);
        assertEquals(0, compare(10, acceleration.getAmount()));
        assertTrue(unitFactory.getUnit("m/s^2").isEquals(acceleration.getUnit()));

        PhysicalQuantity kmhAcceleration = acceleration.convertTo(UnitSymbol.of("km/h^2"));
        assertEquals(0, compare(129600, kmhAcceleration.getAmount()));
        assertTrue(unitFactory.getUnit("km/h^2").isEquals(kmhAcceleration.getUnit()));
    }
    
    @Test
    public void testCompareTo() {
        PhysicalQuantity m = quantityFactory.of(1, "m");
        PhysicalQuantity cm = quantityFactory.of(100, "cm");

        assertEquals(0, m.compareTo(cm));
    }
}
