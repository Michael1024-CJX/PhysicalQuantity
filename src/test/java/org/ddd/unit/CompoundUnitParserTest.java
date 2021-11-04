package org.ddd.unit;

import org.ddd.unit.impl.YAMLUnitRegister;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class CompoundUnitParserTest {
    private CompoundUnitParser parser;
    @Before
    public void init() {
        UnitRegister unitRegister = new YAMLUnitRegister(new File("src/test/resources/unit/"));
        UnitFactory unitFactory = new DefaultUnitFactory(unitRegister);

        parser = new CompoundUnitParser(unitFactory);
    }

    @Test
    public void testParser() {
        Unit parser = this.parser.parser("m*s/kg^2");
        assertEquals("m·s·kg^-2", String.valueOf(parser));
    }
}