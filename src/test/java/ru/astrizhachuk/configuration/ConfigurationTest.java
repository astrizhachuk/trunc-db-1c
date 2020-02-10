package ru.astrizhachuk.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationTest {

    private static final String PATH_TO_DEFAULT_CONFIGURATION_FILE = "./src/main/resources/configuration.json";
    private static final String PATH_TO_FULL_CONFIGURATION_FILE = "./src/test/resources/configuration/configuration.json";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

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

    @Test
    void fileConfigurationNotExists() {
        // given
        File file = new File(PATH_TO_FULL_CONFIGURATION_FILE + ".fake");

        // when
        try {
            Configuration configuration = Configuration.create(file);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(errContent.toString()).containsIgnoringCase("Configuration file not found.");

    }
}