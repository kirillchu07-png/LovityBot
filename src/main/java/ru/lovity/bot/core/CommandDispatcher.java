package ru.lovity.bot.core;
import ru.lovity.bot.commands.Command;
import ru.lovity.bot.model.ConsoleWriter;
import ru.lovity.bot.model.IncomingMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CommandDispatcher {
    private final Map<String, Command> commands =
            new HashMap<>();

    private final Map<String, Command> activeCommands =
            new HashMap<>();

    private final ConsoleWriter print;

    public CommandDispatcher(
            List<Command> commandList,
            ConsoleWriter print) {

        this.print = print;

        for (Command command : commandList) {
            commands.put(command.getName(), command);
        }
    }

    public void dispatch(IncomingMessage message)
            throws Exception {

        String userId = message.userId();
        String text = message.text();

        if (commands.containsKey(text)) {

            Command command = commands.get(text);

            activeCommands.remove(userId);
            command.execute(userId, print);

            if (command.hasSession(userId)) {
                activeCommands.put(userId, command);
            }

        } else if (text.startsWith("/")) {

            print.output(userId, "Команда не найдена");

        } else if (activeCommands.containsKey(userId)) {

            Command command = activeCommands.get(userId);

            command.handleAnswer(userId, text, print);

            if (!command.hasSession(userId)) {
                activeCommands.remove(userId);
            }

        } else {

            print.output(userId,
                    "Отправь /start для заполнения анкеты");
        }
    }

}