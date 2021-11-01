package org.ddd.unit.impl;

import org.ddd.unit.ConversionRateDefinition;
import org.ddd.unit.Ratio;
import org.ddd.unit.UnitDefinition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author chenjx
 */
public class YamlConverter {
    private static final String STANDARD_FORMAT = "^.+\\(.*?\\)(:.+\\(.*?\\)=(([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])):(([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])))?$";

    private static final Pattern PATTERN = Pattern.compile(STANDARD_FORMAT);

    private Set<UnitDefinition> unitDefinitions = new LinkedHashSet<>();
    private Set<ConversionRateDefinition> conversionRateDefinitions = new LinkedHashSet<>();

    public YamlConverter(SingleUnitType singleUnitType) {
        parse(singleUnitType);
    }

    private void parse(SingleUnitType singleUnitType) {
        String type = singleUnitType.getType();
        String[] ratios = singleUnitType.getRatios();

        for (String ratio : ratios) {
            if (!PATTERN.matcher(ratio).matches()) {
                throw new IllegalArgumentException("ratio 格式异常：" + ratio);
            }
            String[] strArr = ratio.split("[:=]");
            ConversionRateDefinition definition = new ConversionRateDefinition();
            definition.setPhysicalQuantity(type);
            if (strArr.length == 1) {
                UnitDefinition unitDefinition = convertUnit(type, strArr[0]);
                unitDefinitions.add(unitDefinition);
                continue;
            }

            UnitDefinition unitDefinition1 = convertUnit(type, strArr[0]);
            UnitDefinition unitDefinition2 = convertUnit(type, strArr[1]);
            definition.setNumeratorUnit(unitDefinition1.getSymbol());
            definition.setDenominatorUnit(unitDefinition2.getSymbol());
            definition.setRatio(convertRatio(strArr[2],strArr[3]));
            unitDefinitions.add(unitDefinition1);
            unitDefinitions.add(unitDefinition2);
            conversionRateDefinitions.add(definition);
        }
    }

    private UnitDefinition convertUnit(String type, String text) {
        UnitDefinition unitDefinition = new UnitDefinition();
        String[] split = text.split("[()]");
        if (split.length == 1) {
            unitDefinition.setAlias("");
        }else {
            unitDefinition.setAlias(split[1]);
        }
        unitDefinition.setPhysicalQuantity(type);
        unitDefinition.setSymbol(split[0]);
        return unitDefinition;
    }

    private Ratio convertRatio(String f1, String f2) {
        return Ratio.of(new BigDecimal(f1), new BigDecimal(f2));
    }

    public Set<UnitDefinition> getUnitDefinitions() {
        return unitDefinitions;
    }

    public Set<ConversionRateDefinition> getConversionRateDefinitions() {
        return conversionRateDefinitions;
    }
}
