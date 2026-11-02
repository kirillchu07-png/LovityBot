package ru.lovity.bot.telegram;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class TelegramApiClient {
    private final String token;

    private final HttpClient client =
            HttpClient.newHttpClient();

    public TelegramApiClient(String token) {
        this.token = token;
    }

    public JsonObject request(String method, String body)
            throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://api.telegram.org/bot"
                                + token + "/" + method))
                .header(
                        "Content-Type",
                        "application/x-www-form-urlencoded"
                )
                .timeout(Duration.ofSeconds(40))
                .POST(
                        HttpRequest.BodyPublishers.ofString(body)
                )
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        JsonObject json = JsonParser.parseString(
                response.body()
        ).getAsJsonObject();

        if (!json.has("ok") ||
                !json.get("ok").getAsBoolean()) {

            throw new RuntimeException(
                    "Ошибка Telegram API: " + response.body()
            );
        }

        return json;
    }

    public static String encode(String text) {

        return URLEncoder.encode(
                text,
                StandardCharsets.UTF_8
        );
    }
}