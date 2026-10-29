
package ru.lovity.bot.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import ru.lovity.bot.core.Paths;
import ru.lovity.bot.model.ConsoleWriter;
import ru.lovity.bot.model.Match;
import ru.lovity.bot.model.ViewSession;
import ru.lovity.bot.service.ProfileFinder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ViewCommand implements Command {

    private final String pathProfils =
            Paths.PATH_RESOURCES
                    + "profils.json";

    private final ProfileFinder profileFinder;
    private final Match match;

    private final Map<String, ViewSession> sessions =
            new HashMap<>();

    private final Map<String, String[]> results =
            new HashMap<>();

    public ViewCommand() throws IOException {
        profileFinder = new ProfileFinder(pathProfils);
        match = new Match();
    }

    @Override
    public String getName() {
        return "/view";
    }

    @Override
    public boolean hasSession(String userId) {
        return sessions.containsKey(userId);
    }

    @Override
    public void execute(String userId, ConsoleWriter print)
            throws Exception {

        JsonObject userProfile =
                profileFinder.getProfileById(userId);

        if (userProfile == null) {
            print.output(userId, "Сначала создай профиль!");
            return;
        }

        String[] ids = new String[100];
        int[] scores = new int[100];
        int count = 0;

        // Читаем JSON по одной анкете
        try (JsonReader reader = new JsonReader(
                new InputStreamReader(
                        new FileInputStream(pathProfils),
                        StandardCharsets.UTF_8))) {

            reader.beginArray();

            while (reader.hasNext()) {

                JsonObject profile = JsonParser
                        .parseReader(reader)
                        .getAsJsonObject();

                String otherId =
                        profile.get("id").getAsString();

                // Не показываем собственную анкету
                if (otherId.equals(userId)) {
                    continue;
                }

                int score =
                        match.ifMatch(userProfile, profile);
                if (score == -1) {
                    continue;
                }

                if (count == ids.length) {
                    ids = Arrays.copyOf(ids, ids.length * 2);
                    scores = Arrays.copyOf(
                            scores, scores.length * 2);
                }

                ids[count] = otherId;
                scores[count] = score;
                count++;
            }

            reader.endArray();
        }

        if (count == 0) {
            print.output(userId, "Других анкет пока нет.");
            return;
        }


        quickSort(ids, scores, 0, count - 1);

        ViewSession session =
                new ViewSession(ids, scores, count);

        sessions.put(userId, session);
        results.remove(userId);

        print.output(userId,
                "Просмотр анкет начат!\n"
                        + "like — понравилась анкета\n"
                        + "skip — пропустить\n"
                        + "stop — закончить просмотр");

        showCurrentProfile(userId, print);
    }

    @Override
    public void handleAnswer(String userId, String text,
                             ConsoleWriter print) throws Exception {

        ViewSession session = sessions.get(userId);

        if (session == null) {
            print.output(userId, "Сначала отправь /profiles");
            return;
        }

        String answer = text.trim().toLowerCase();

        if (answer.equals("stop")) {
            finish(userId, session, print);
            return;
        }

        if (answer.equals("like")) {
            String likedId =
                    session.getIds()[session.getIndex()];

            session.addLike(likedId);

        } else if (!answer.equals("skip")) {
            print.output(userId,
                    "Напиши like, skip или stop.");
            return;
        }

        session.nextProfile();

        if (session.getIndex() >= session.getCount()) {
            finish(userId, session, print);
            return;
        }

        showCurrentProfile(userId, print);
    }

    private void showCurrentProfile(String userId,
                                    ConsoleWriter print)
            throws Exception {

        ViewSession session = sessions.get(userId);

        String id =
                session.getIds()[session.getIndex()];

        String profileText =
                profileFinder.profilForUser(id);

        int score =
                session.getScores()[session.getIndex()];

        print.output(userId,
                "Совпадение: " + score + "%\n"
                        + profileText
                        + "\n\nНапиши like, skip или stop.");
    }

    private void finish(String userId, ViewSession session,
                        ConsoleWriter print) throws Exception {

        String[] likedIds = session.getLikes();

        results.put(userId, likedIds);
        sessions.remove(userId);

        print.output(userId,
                "Просмотр завершён!\n"
                        + "Понравившиеся ID: "
                        + Arrays.toString(likedIds));
    }

    public String[] getResults(String userId) {
        return results.get(userId);
    }

    private void quickSort(String[] ids, int[] scores,
                           int low, int high) {

        int i = low;
        int j = high;

        int pivot = scores[low + (high - low) / 2];

        while (i <= j) {

            while (scores[i] > pivot) {
                i++;
            }

            while (scores[j] < pivot) {
                j--;
            }

            if (i <= j) {
                int tempScore = scores[i];
                scores[i] = scores[j];
                scores[j] = tempScore;

                String tempId = ids[i];
                ids[i] = ids[j];
                ids[j] = tempId;

                i++;
                j--;
            }
        }

        if (low < j) {
            quickSort(ids, scores, low, j);
        }

        if (i < high) {
            quickSort(ids, scores, i, high);
        }
    }
}
