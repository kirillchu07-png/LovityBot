
import ru.lovity.bot.commands.Command;
import ru.lovity.bot.commands.StartCommand;
import ru.lovity.bot.commands.HelpCommand;
import ru.lovity.bot.commands.ViewCommand;

import ru.lovity.bot.core.CommandDispatcher;

import ru.lovity.bot.model.ConsoleReader;
import ru.lovity.bot.model.ConsoleWriter;
import ru.lovity.bot.model.IncomingMessage;

import ru.lovity.bot.telegram.TelegramReader;
import ru.lovity.bot.telegram.TelegramWriter;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

class Main {

    public static void main(String[] args)
            throws Exception {

        String token = System.getenv("BOT_TOKEN");

        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "Не указан BOT_TOKEN"
            );
        }

        TelegramClient api =
                new OkHttpTelegramClient(token);

        ConsoleReader console =
                new TelegramReader();

        ConsoleWriter print =
                new TelegramWriter(api);

        StartCommand startCommand =
                new StartCommand();

        ViewCommand viewCommand =
                new ViewCommand();

        HelpCommand helpCommand =
                new HelpCommand();

        List<Command> commands = List.of(
                startCommand,
                helpCommand,
                viewCommand
        );

        CommandDispatcher dispatcher =
                new CommandDispatcher(
                        commands,
                        print
                );

        TelegramReader reader =
                (TelegramReader) console;

        TelegramBotsLongPollingApplication application =
                new TelegramBotsLongPollingApplication();

        application.registerBot(
                token,
                reader
        );

        System.out.println("Telegram-бот запущен!");

        while (true) {

            try {

                IncomingMessage message =
                        console.readLine();

                dispatcher.dispatch(message);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}
