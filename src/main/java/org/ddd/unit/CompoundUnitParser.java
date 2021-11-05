package org.ddd.unit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenjx
 */
public class CompoundUnitParser {
    private static final Pattern JOINER = Pattern.compile("([*/·])");

    private UnitFactory unitFactory;

    public CompoundUnitParser(UnitFactory unitFactory) {
        this.unitFactory = unitFactory;
    }

    public Unit parser(String symbol) {
        Matcher matcher = JOINER.matcher(symbol);

        String operator = null;

        Unit preUnit = null;
        int start = 0;
        while (matcher.find()) {
            int end = matcher.start();
            String unitSymbol = symbol.substring(start, end);
            Unit nextUnit = getUnitWithPower(unitSymbol);

            if (preUnit == null) {
                preUnit = nextUnit;
            }else {
                preUnit = combineUnit(preUnit, operator, nextUnit);
            }

            operator = matcher.group();
            start = end + 1;
        }

        if (operator != null) {
            preUnit = combineUnit(preUnit, operator, getUnitWithPower(symbol.substring(start)));
        }
        return preUnit;
    }

    private Unit combineUnit(Unit prevUnit, String operator, Unit nextUnit) {
        switch (operator) {
            case "·":
            case "*":
                return new CompoundUnit(prevUnit, PowerUnit.ofPositiveOne(nextUnit));
            case "/":
                return new CompoundUnit(prevUnit, PowerUnit.ofNegativeOne(nextUnit));
            default:
                throw new IllegalArgumentException("未知连接符");
        }
    }

    private Unit getUnitWithPower(String symbol) {
        Unit unit;

        if (symbol.contains("^")) {
            String[] unitAndPower = symbol.split("\\^");
            unit = safeGetUnit(unitAndPower[0]);
            int power = 1;
            for (int i = 1; i < unitAndPower.length; i++) {
                power *= Integer.parseInt(unitAndPower[i]);
            }
            unit = PowerUnit.of(unit, power);
        } else {
            unit = safeGetUnit(symbol);
        }

        return unit;
    }

    private Unit safeGetUnit(String symbol) {
        Unit unit = unitFactory.getUnit(symbol);
        if (unit == null) {
            throw new IllegalArgumentException("单位[" + symbol + "]不存在");
        }
        return unit;
    }
}
