package org.ddd.quantity;

import org.ddd.unit.DefaultUnitFactory;
import org.ddd.unit.PhysicalQuantity;
import org.ddd.unit.QuantityFactory;
import org.ddd.unit.UnitFactory;
import org.ddd.unit.impl.YAMLUnitRegister;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;

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
        Assert.assertEquals("180cm", String.valueOf(height));
    }

    @Test
    public void testConvertToAtomicUnit() {
        PhysicalQuantity height = quantityFactory.of(180, "cm");
        PhysicalQuantity heightByM = height.convertTo(unitFactory.getUnit("m"));
        Assert.assertEquals("1.80m", String.valueOf(heightByM));
    }

    @Test
    public void testConvertToCompoundUnit() {
        PhysicalQuantity ms = quantityFactory.of(10, "m/s");
        PhysicalQuantity kmh = ms.convertTo(unitFactory.getUnit("km/h"));
        Assert.assertEquals(0, new BigDecimal(36).compareTo(new BigDecimal(kmh.getAmount().toString())));
    }

    @Test
    public void testAdd() {
        PhysicalQuantity expected = quantityFactory.of(185, "cm");

        PhysicalQuantity height = quantityFactory.of(1.8, "m");
        PhysicalQuantity hairHeight = quantityFactory.of(5, "cm");
        PhysicalQuantity sum = height.add(hairHeight);

        Assert.assertEquals(0, sum.compareTo(expected));
    }

    @Test
    public void testMultiplyNumber() {
        PhysicalQuantity expected = quantityFactory.of(200, "cm");

        PhysicalQuantity height = quantityFactory.of(1, "m");

        PhysicalQuantity multiply = height.multiply(2);

        Assert.assertEquals(0, multiply.compareTo(expected));
    }

    @Test
    public void testMultiplyQuantity() {
        PhysicalQuantity height1 = quantityFactory.of(1, "m");
        PhysicalQuantity height2 = quantityFactory.of(2, "m");

        PhysicalQuantity multiply = height1.multiply(height2);

        System.out.println(multiply);

        PhysicalQuantity expected = quantityFactory.of(2, "m^2");
        Assert.assertEquals(0, multiply.compareTo(expected));
    }

    @Test
    public void testDivideNumber() {
        PhysicalQuantity expected = quantityFactory.of(50, "cm");

        PhysicalQuantity height = quantityFactory.of(1, "m");

        PhysicalQuantity multiply = height.divide(2);

        Assert.assertEquals(0, multiply.compareTo(expected));
    }

    @Test
    public void testDivideQuantity() {
        PhysicalQuantity expected = quantityFactory.of(50, "cm");

        PhysicalQuantity height = quantityFactory.of(1, "m");

        PhysicalQuantity multiply = height.divide(2);

        Assert.assertEquals(0, multiply.compareTo(expected));
    }
}
