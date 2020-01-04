package ru.astrizhachuk.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.astrizhachuk.metadata.Metadata;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.astrizhachuk.Main.createOptions;

class HttpClientTest {

    private static final String PATH_TO_SHORT_RESPONSE_FILE = "./src/test/resources/short-response.json";
    private static MockWebServer server;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        server.shutdown();
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    @SneakyThrows
    void getResponseOK() {
        // given
        String context = FileUtils.readFileToString(new File(PATH_TO_SHORT_RESPONSE_FILE), StandardCharsets.UTF_8);
        server = new MockWebServer();
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(context);
        server.enqueue(mockResponse);
        server.start();

        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        String url = String.format("http://%s:%d/metadata", server.getHostName(), server.getPort());
        CommandLine cmd = parser.parse(options, new String[]{"-s", url, "-b", "base"});

        // when
        HttpClient httpClient = HttpClient.create(cmd);
        InputStream inputStream = httpClient.getResponseByteStream();

        // then
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        Metadata metadata = mapper.readValue(inputStream, Metadata.class);

        Map<String, String> tables = metadata.getTables();
        assertThat(tables).hasSize(2);
        assertThat(tables.get("Справочник.АдресныеСокращения"))
                .isEqualTo("_Reference56");
    }

    @Test
    void getResponseBad() throws IOException {

        // given
        server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(500));
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        String url = String.format("http://%s:%d/metadata", server.getHostName(), server.getPort());
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, new String[]{"-s", url});
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // when
        HttpClient httpClient = HttpClient.create(cmd);

        try {
            InputStream inputStream = httpClient.getResponseByteStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // then
        assertThat(errContent.toString()).containsIgnoringCase("Unexpected code");

    }
}