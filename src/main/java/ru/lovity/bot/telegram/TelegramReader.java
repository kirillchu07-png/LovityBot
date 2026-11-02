
package ru.lovity.bot.telegram;

import ru.lovity.bot.model.ConsoleReader;
import ru.lovity.bot.model.IncomingMessage;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class
TelegramReader implements ConsoleReader,
        LongPollingSingleThreadUpdateConsumer {

    private final BlockingQueue<IncomingMessage> messages =
            new LinkedBlockingQueue<>();

    @Override
    public void consume(Update update) {

        if (!update.hasMessage()) {
            return;
        }

        var message = update.getMessage();

        if (!message.hasText()) {
            return;
        }

        if (!message.getChat().isUserChat()) {
            return;
        }

        String userId = "telegram:" +
                message.getChatId();

        String text = message.getText();

        messages.add(
                new IncomingMessage(userId, text)
        );
    }

    @Override
    public IncomingMessage readLine()
            throws InterruptedException {

        return messages.take();
    }
}
