package ru.astrizhachuk.http;

import okhttp3.*;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class HttpClient {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final int TIMEOUT = 10;
    private OkHttpClient client;
    private Request request;

    private HttpClient() {
        client = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private HttpClient(String server, String baseName) {
        this();
        request = getRequest(server, baseName);
    }

    public static HttpClient create(CommandLine cmd) {
        String server = cmd.getOptionValue("s", "localhost");
        String baseName = cmd.getOptionValue("b", "");
        return new HttpClient(server, baseName);
    }

    public InputStream getResponseByteStream() throws IOException {
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().byteStream();
    }

    @NotNull
    private Request getRequest(String server, String baseName) {
        return new Request.Builder()
                .url(server)
                .post(getRequestBody(baseName))
                .build();
    }

    @NotNull
    private RequestBody getRequestBody(String baseName) {
        String json = String.format("{\"base\":\"%s\"}", baseName);
        return RequestBody.create(json, JSON);
    }

}
