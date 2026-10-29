package ru.lovity.bot.telegram;
import ru.lovity.bot.model.ConsoleReader;
import ru.lovity.bot.model.IncomingMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayDeque; import java.util.Queue;
public class TelegramReader implements ConsoleReader {
    private final TelegramApiClient api;

    private final Queue<IncomingMessage> messages =
            new ArrayDeque<>();

    private long offset = 0;

    public TelegramReader(TelegramApiClient api) {
        this.api = api;
    }

    @Override
    public IncomingMessage readLine() throws Exception {

        while (messages.isEmpty()) {

            JsonObject response = api.request(
                    "getUpdates",
                    "offset=" + offset + "&timeout=25"
            );

            JsonArray updates =
                    response.getAsJsonArray("result");

            for (JsonElement element : updates) {

                JsonObject update =
                        element.getAsJsonObject();

                offset = update.get("update_id")
                        .getAsLong() + 1;

                if (!update.has("message")) {
                    continue;
                }

                JsonObject message =
                        update.getAsJsonObject("message");

                if (!message.has("text")) {
                    continue;
                }

                JsonObject chat =
                        message.getAsJsonObject("chat");

                // Только личные сообщения
                if (!chat.get("type")
                        .getAsString().equals("private")) {
                    continue;
                }

                String userId = "telegram:" +
                        chat.get("id").getAsString();

                String text = message.get("text")
                        .getAsString();

                messages.add(
                        new IncomingMessage(userId, text)
                );
            }
        }

        return messages.poll();
    }
}