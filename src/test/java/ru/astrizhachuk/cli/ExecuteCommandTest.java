package ru.astrizhachuk.cli;

import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.astrizhachuk.Main.createOptions;

class ExecuteCommandTest {

    public static final String PATH_TO_SHORT_RESPONSE_JSON = "./src/test/resources/short-response.json";
    public static final String PATH_TO_FULL_RESPONSE_JSON = "./src/test/resources/full-response.json";
    public static final String PATH_TO_FULL_CONFIGURATION_FILE = "./src/test/resources/configuration/configuration.json";
    private static MockWebServer server;

    @Test
    void testExecuteFromFileWithoutConfig() throws ParseException {
        // given
        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-f", PATH_TO_SHORT_RESPONSE_JSON});

        // when
        Command command = new ExecuteCommand(cmd);
        int result = command.execute();

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void testExecuteFromFileWithConfig() throws ParseException {
        // given
        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,
                new String[]{"-f", PATH_TO_FULL_RESPONSE_JSON, "-c", PATH_TO_FULL_CONFIGURATION_FILE});

        // when
        Command command = new ExecuteCommand(cmd);
        int result = command.execute();

        // then
        assertThat(result).isEqualTo(0);
    }

    @SneakyThrows
    @Test
    void testExecuteResponseWithoutConfig() throws ParseException {
        // given
        String context = FileUtils.readFileToString(new File(PATH_TO_SHORT_RESPONSE_JSON), StandardCharsets.UTF_8);
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
        Command command = new ExecuteCommand(cmd);
        int result = command.execute();
        server.shutdown();

        // then
        assertThat(result).isEqualTo(0);
    }

    @SneakyThrows
    @Test
    void testExecuteResponseWithConfig() throws ParseException {
        // given
        String context = FileUtils.readFileToString(new File(PATH_TO_SHORT_RESPONSE_JSON), StandardCharsets.UTF_8);
        server = new MockWebServer();
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(context);
        server.enqueue(mockResponse);
        server.start();

        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        String url = String.format("http://%s:%d/metadata", server.getHostName(), server.getPort());
        CommandLine cmd = parser.parse(options, new String[]{"-s", url, "-b", "base", "-c", PATH_TO_FULL_CONFIGURATION_FILE});

        // when
        Command command = new ExecuteCommand(cmd);
        int result = command.execute();
        server.shutdown();

        // then
        assertThat(result).isEqualTo(0);
    }
}