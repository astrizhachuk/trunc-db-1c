package ru.astrizhachuk.metadata;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Data
@AllArgsConstructor
@JsonDeserialize(using = MetadataParser.MetadataDeserializer.class)
@Slf4j
public class MetadataParser {

    private Map<String, String> tables;

    private MetadataParser() {
        this(new HashMap<>());
    }

    public static MetadataParser create() {
        return new MetadataParser();
    }

    public static MetadataParser create(Object o) {

        MetadataParser metadataParser = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (o instanceof File) {
                metadataParser = mapper.readValue((File) o, MetadataParser.class);
            } else if (o instanceof InputStream) {
                metadataParser = mapper.readValue((InputStream) o, MetadataParser.class);
            } else {
                new IOException();
            }

        } catch (IOException e) {
            LOGGER.error("Can't deserialize metadata file", e);
        }

        if (metadataParser == null) {
            metadataParser = create();
        }
        return metadataParser;
    }

    static class MetadataDeserializer extends JsonDeserializer<MetadataParser> {
        @Override
        public MetadataParser deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return new MetadataParser(getTables(node));
        }

        private Map<String, String> getTables(JsonNode node) {
            if (!node.isArray()) {
                return Collections.emptyMap();
            }
            Map<String, String> tablesMap = new HashMap<>();
            Iterator<JsonNode> tableNodes = node.elements();
            tableNodes.forEachRemaining((JsonNode entry) ->
                    tablesMap.put(entry.get("tableName").asText(), entry.get("storageTableName").asText())
            );
            return tablesMap;
        }
    }
}
