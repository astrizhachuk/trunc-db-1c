package ru.astrizhachuk.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Slf4j
public class Configuration {
    private List<String> prototypes;
    private List<String> metaDataObjects;

    private Configuration() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    private static Configuration create() {
        return new Configuration();
    }

    public static Configuration create(File file) {
        Configuration configuration = null;
        try {
            if (file.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                configuration = mapper.readValue(file, Configuration.class);
            } else {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Configuration file not found.", e);
        } catch (IOException e) {
            LOGGER.error("Can't deserialize configuration file.", e);
        }

        if (configuration == null) {
            configuration = create();
        }

        return configuration;
    }
}