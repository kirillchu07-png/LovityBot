
package ru.lovity.bot.telegram;

import ru.lovity.bot.model.ConsoleWriter;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramWriter implements ConsoleWriter {

    private final TelegramClient api;

    public TelegramWriter(TelegramClient api) {
        this.api = api;
    }

    @Override
    public void output(String userId, String text)
            throws Exception {

        String chatId = userId.replaceFirst(
                "^telegram:", ""
        );

        SendMessage message = new SendMessage(
                chatId,
                text
        );

        api.execute(message);
    }
}
