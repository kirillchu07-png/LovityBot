package ru.lovity.bot.telegram;
import ru.lovity.bot.model.ConsoleWriter;
public class TelegramWriter implements ConsoleWriter {
    private final TelegramApiClient api;

    public TelegramWriter(TelegramApiClient api) {
        this.api = api;
    }

    @Override
    public void output(String userId, String text)
            throws Exception {

        String chatId = userId.replaceFirst(
                "^telegram:", ""
        );

        String body = "chat_id=" + chatId
                + "&text=" + TelegramApiClient.encode(text);

        api.request("sendMessage", body);
    }
}