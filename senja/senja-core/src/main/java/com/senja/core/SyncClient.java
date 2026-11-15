package com.senja.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/** HTTP client untuk REST API senja-api. */
public final class SyncClient {

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5)).build();
    private static final ObjectMapper M = new ObjectMapper();

    private SyncClient() {}

    public static boolean health(String server) {
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(server + "/api/health"))
                    .timeout(Duration.ofSeconds(5)).GET().build();
            return HTTP.send(req, HttpResponse.BodyHandlers.ofString()).statusCode() == 200;
        } catch (Exception e) { return false; }
    }

    public static Map<String, Object> register(String server, String nama, String tipe) throws Exception {
        return post(server + "/api/devices/register", Map.of("nama", nama, "tipe", tipe), null, null);
    }

    public static Map<String, Object> sync(String server, String deviceId, String apiKey,
                                           long lastPull, Map<String, Object> changes) throws Exception {
        return post(server + "/api/sync",
                Map.of("last_pull", lastPull, "changes", changes), deviceId, apiKey);
    }

    private static Map<String, Object> post(String url, Map<String, Object> body,
                                            String deviceId, String apiKey) throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(M.writeValueAsString(body)));
        if (deviceId != null) b.header("X-Device-Id", deviceId);
        if (apiKey != null) b.header("X-Api-Key", apiKey);
        HttpResponse<String> resp = HTTP.send(b.build(), HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() / 100 != 2)
            throw new RuntimeException("HTTP " + resp.statusCode() + " dari server: " + resp.body());
        return M.readValue(resp.body(), new TypeReference<Map<String, Object>>() {});
    }
}