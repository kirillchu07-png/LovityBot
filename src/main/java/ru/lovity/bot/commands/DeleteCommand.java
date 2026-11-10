package ru.lovity.bot.commands;
import ru.lovity.bot.core.Paths;
import ru.lovity.bot.model.ConsoleWriter;
import ru.lovity.bot.service.ProfileDeleter;

public class DeleteCommand implements Command {
    private final ProfileDeleter deleter;

    public DeleteCommand() {
        this.deleter = new ProfileDeleter(Paths.PATH_RESOURCES + "profils.json");
    }

    @Override
    public String getName() {
        return "/delete";
    }

    @Override
    public void execute(String userId, ConsoleWriter print) throws Exception {
        boolean isDeleted = deleter.deleteProfile(userId);

        if (isDeleted) {
            print.output(userId, "Твоя анкета успешно удалена!\n" +
                    "Чтобы создать новую, отправь /start");
        } else {
            print.output(userId, "У тебя еще нет анкеты.\n" +
                    "Создай её командой /start");
        }
    }
}
