package ru.astrizhachuk.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.astrizhachuk.metadata.MetadataParser;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.astrizhachuk.Main.createOptions;

class HttpClientTest {

    private static final String PATH_TO_SHORT_RESPONSE_FILE = "./src/test/resources/short-response.json";
    private static MockWebServer server = getMockServer();

    @SneakyThrows
    private static MockWebServer getMockServer() {
        String context = FileUtils.readFileToString(new File(PATH_TO_SHORT_RESPONSE_FILE), StandardCharsets.UTF_8);

        MockWebServer server = new MockWebServer();
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(context);
        server.enqueue(mockResponse);
        //server.enqueue(new MockResponse().setResponseCode(404));

        return server;
    }

    @SneakyThrows
    @BeforeEach
    void setUp() {
        server.start();
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        server.shutdown();
    }

    @SneakyThrows
    @Test
    void createFromResponse() {
        // given
        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        String format = String.format("http://%s:%d/metadata", server.getHostName(), server.getPort());
        CommandLine cmd = parser.parse(options, new String[]{"-s", format});

        // when
        HttpClient httpClient = HttpClient.create(cmd);
        InputStream inputStream = httpClient.getResponseByteStream();

        // then
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        MetadataParser metadataParser = mapper.readValue(inputStream, MetadataParser.class);

        Map<String, String> tables = metadataParser.getTables();
        assertThat(tables).hasSize(2);
        assertThat(tables.get("Справочник.АдресныеСокращения"))
                .isEqualTo("_Reference56");
    }

}