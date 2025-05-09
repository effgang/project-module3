package com.efanov.resourcesParser;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;


@Data
public class YamlParser {
    public static final String YAML_FILE_IS_MISSING = "Yaml file is missing";
    public static final String CONFIG_YAML = "config.yaml";
    private static final Logger log = LoggerFactory.getLogger(YamlParser.class.getName());

    Yaml yaml = new Yaml();

    Map<String, Integer> ymlMap = yaml.load(getFileAsIOStream(CONFIG_YAML));

    private InputStream getFileAsIOStream(final String fileName) {
        InputStream ioStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (ioStream == null) {
            log.error(YAML_FILE_IS_MISSING);
        }
        return ioStream;
    }
}
