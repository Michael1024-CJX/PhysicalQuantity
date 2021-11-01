package org.ddd.unit.impl;

import org.ddd.unit.*;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenjx
 */
public class YAMLUnitRegister implements UnitRegister {
    private File importUnitDir;

    private List<String> physicalQuantities = new ArrayList<>();
    private List<UnitDefinition> unitDefinitions = new ArrayList<>();
    private List<ConversionRateDefinition> conversionRateDefinitions = new ArrayList<>();

    public YAMLUnitRegister(File importUnitDir) {
        this.importUnitDir = importUnitDir;
        refresh();
    }

    public void refresh() {
        File[] files = importUnitDir.listFiles();
        if (files == null) {
            return;
        }
        try {
            for (File file : files) {
                Yaml yaml = new Yaml();
                BufferedReader bufferedReader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
                UnitTypeConfig unitTypeConfig = yaml.loadAs(bufferedReader, UnitTypeConfig.class);

                List<SingleUnitType> units = unitTypeConfig.getUnits();
                units.forEach((type) -> {
                    physicalQuantities.add(type.getType());
                    YamlConverter yamlConverter = new YamlConverter(type);
                    unitDefinitions.addAll(yamlConverter.getUnitDefinitions());
                    conversionRateDefinitions.addAll(yamlConverter.getConversionRateDefinitions());
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<String> getAllPhysicalQuantity() {
        return physicalQuantities;
    }

    @Override
    public Collection<UnitDefinition> getAllUnitDefinition() {
        return unitDefinitions;
    }

    @Override
    public Collection<ConversionRateDefinition> getAllConversionRateDefinition() {
        return conversionRateDefinitions;
    }

    @Override
    public String toString() {
        return "YAMLUnitRegister{" +
                "\r\n physicalQuantities=" + physicalQuantities +
                ",\r\n unitDefinitions=" + unitDefinitions +
                ",\r\n conversionRateDefinitions=" + conversionRateDefinitions +
                '}';
    }
}
