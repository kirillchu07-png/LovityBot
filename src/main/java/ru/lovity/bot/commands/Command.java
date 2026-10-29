package ru.lovity.bot.commands;
import ru.lovity.bot.model.ConsoleWriter;

public interface Command {
    String getName();

    void execute(String userId, ConsoleWriter print)
            throws Exception;
    default boolean hasSession(String userId){
        return false;
    }
    default void handleAnswer(String userId,String text,ConsoleWriter print)throws Exception{

    }

}
