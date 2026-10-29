package ru.lovity.bot.model;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

public class UserSession {
    private int questionIndex = 0;
    private int questionNumber = 0;

    private final Map<String, String> answers =
            new LinkedHashMap<>();

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void nextQuestion() {
        questionIndex++;
    }

    public int nextQuestionNumber() {
        return ++questionNumber;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

}
