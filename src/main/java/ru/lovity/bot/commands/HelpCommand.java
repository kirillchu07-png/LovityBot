package ru.lovity.bot.commands;
import ru.lovity.bot.model.ConsoleWriter;
public class HelpCommand implements Command {
    @Override
    public String getName() {
        return "/help";
    }

    @Override
    public void execute(String userId, ConsoleWriter print)
            throws Exception {

        print.output(userId,
                "Доступные команды:\n" +
                        "/start - заполнение вашего профиля\n" +
                        "/help - список команд\n" +
                        "/view - просмотр анкет");
    }

}
