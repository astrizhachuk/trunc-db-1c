package ru.astrizhachuk.metadata;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.astrizhachuk.configuration.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@AllArgsConstructor
@JsonDeserialize(using = Metadata.MetadataDeserializer.class)
@Slf4j
public class Metadata {

    @Getter
    private Map<String, String> tables;

    private Metadata() {
        this(new HashMap<>());
    }

    private static Metadata create() {
        return new Metadata();
    }

    public static Metadata create(Object o) {

        Metadata metadata = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            //TODO пересмотреть create(Object o)
            if (o instanceof File) {
                metadata = mapper.readValue((File) o, Metadata.class);
            } else if (o instanceof InputStream) {
                metadata = mapper.readValue((InputStream) o, Metadata.class);
            } else {
                throw new IOException();
            }

        } catch (IOException e) {
            LOGGER.error("Can't deserialize metadata file", e);
        }

        if (metadata == null) {
            metadata = create();
        }
        return metadata;
    }

    public Collection<String> collectByConfig(Configuration config) {
        // TODO опитимизировать
        HashMap<String, String> result = new HashMap<>();

        List<String> prototypes = config.getPrototypes().stream()
                .map(s -> format("^%s.*", s))
                .collect(Collectors.toList());

        addByRule(result, prototypes);

        List<String> metaDataObjects = config.getMetaDataObjects();
        addByRule(result, metaDataObjects);

        addByRule(result, Collections.singletonList(".*\\..*\\.Изменения$"));

        return result.values();
    }

    private void addByRule(HashMap<String, String> map, List<String> rules) {

        Set<Map.Entry<String, String>> entries = getTables().entrySet();
        for (Map.Entry<String, String> entry : entries) {
            for (String s : rules) {
                if (entry.getKey().matches(s)) {
                    map.put(entry.getKey(), entry.getValue());
                    break;
                }
            }
        }
    }

    static class MetadataDeserializer extends JsonDeserializer<Metadata> {
        @Override
        public Metadata deserialize(JsonParser p, DeserializationContext context) throws IOException {
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
