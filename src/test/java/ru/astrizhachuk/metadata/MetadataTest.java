package ru.astrizhachuk.metadata;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MetadataTest {

    private static final String PATH_TO_RESPONSE_FILE = "./src/test/resources/response.json";

    @Test
    void createFromFile() {
        // given
        File responseFile = new File(PATH_TO_RESPONSE_FILE);

        // when
        Metadata response = Metadata.create(responseFile);

        // then
        Map<String, String> tables = response.getTables();
        assertThat(tables).hasSize(4671);
        assertThat(tables.get("РегистрНакопления.ПТР_РасчетБригадыПодъема"))
                .isEqualTo("_AccumRg14410");
    }
}