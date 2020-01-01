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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Data
@AllArgsConstructor
@JsonDeserialize(using = Metadata.MetadataDeserializer.class)
@Slf4j
public class Metadata {

    private Map<String, String> tables;

    private Metadata() {
        this(new HashMap<>());
    }

    public static Metadata create() {
        return new Metadata();
    }

    public static Metadata create(File file) {
        Metadata metadata = null;
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                metadata = mapper.readValue(file, Metadata.class);
            } catch (IOException e) {
                LOGGER.error("Can't deserialize configuration file", e);
            }
        }
        if (metadata == null) {
            metadata = create();
        }
        return metadata;
    }

    static class MetadataDeserializer extends JsonDeserializer<Metadata> {
        @Override
        public Metadata deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return new Metadata(getTables(node));
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
