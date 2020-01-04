package ru.astrizhachuk.metadata;

import lombok.SneakyThrows;
import okhttp3.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MetadataParserTest {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String PATH_TO_FULL_RESPONSE_JSON = "./src/test/resources/full-response.json";
    private static final String PATH_TO_SHORT_RESPONSE_JSON = "./src/test/resources/short-response.json";

    @SneakyThrows
    private InputStream getResponseStream() {

        String context = FileUtils.readFileToString(new File(PATH_TO_SHORT_RESPONSE_JSON), StandardCharsets.UTF_8);

        MockWebServer server = new MockWebServer();
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(context);
        server.enqueue(mockResponse);
        server.start();

        HttpUrl baseUrl = server.url("/metadata");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create("{}", JSON);
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .build();

        return client.newCall(request).execute().body().byteStream();
    }

    @SneakyThrows
    @Test
    void createFromResponse() {
        // given
        InputStream responseStream = getResponseStream();

        // when
        MetadataParser response = MetadataParser.create(responseStream);

        // then
        Map<String, String> tables = response.getTables();
        assertThat(tables).hasSize(2);
        assertThat(tables.get("Справочник.АдресныеСокращения"))
                .isEqualTo("_Reference56");
    }

    @Test
    void createFromFile() {
        // given
        File responseFile = new File(PATH_TO_FULL_RESPONSE_JSON);

        // when
        MetadataParser response = MetadataParser.create(responseFile);

        // then
        Map<String, String> tables = response.getTables();
        assertThat(tables).hasSize(4671);
        assertThat(tables.get("РегистрНакопления.ПТР_РасчетБригадыПодъема"))
                .isEqualTo("_AccumRg14410");
    }
}