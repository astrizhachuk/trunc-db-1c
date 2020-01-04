package ru.astrizhachuk.configuration;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationTest {

    private static final String PATH_TO_DEFAULT_CONFIGURATION_FILE = "./src/main/resources/configuration.json";
    private static final String PATH_TO_FULL_CONFIGURATION_FILE = "./src/test/resources/configuration/configuration.json";

    @Test
    void createDefaultProperties() {
        // given
        File file = new File(PATH_TO_DEFAULT_CONFIGURATION_FILE);

        // when
        Configuration configuration = Configuration.create(file);

        // then
        List<String> prototypes = configuration.getPrototypes();
        assertThat(prototypes).hasSize(2);
        List<String> metaDataObjects = configuration.getMetaDataObjects();
        assertThat(metaDataObjects).hasSize(0);
    }

    @Test
    void createAllProperties() {
        // given
        File file = new File(PATH_TO_FULL_CONFIGURATION_FILE);

        // when
        Configuration configuration = Configuration.create(file);

        // then
        List<String> prototypes = configuration.getPrototypes();
        assertThat(prototypes).hasSize(2);
        List<String> metaDataObjects = configuration.getMetaDataObjects();
        assertThat(metaDataObjects).hasSize(4);
    }
}