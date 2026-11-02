
package ru.lovity.bot.model;

import ru.lovity.bot.service.ProfileFinder;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class LikeManager {


    private final Map<String, Queue<String>> requests =
            new HashMap<>();


    private final Map<String, String> activeRequests =
            new HashMap<>();

    private final ProfileFinder profileFinder;

    public LikeManager(ProfileFinder profileFinder) {
        this.profileFinder = profileFinder;
    }

    public boolean hasSession(String userId) {
        return activeRequests.containsKey(userId);
    }

    public void sendLikes(
            String senderId,
            String[] likedIds,
            ConsoleWriter print) throws Exception {

        for (String likedId : likedIds) {

            if (likedId == null || likedId.equals(senderId)) {
                continue;
            }

            Queue<String> queue = requests.computeIfAbsent(
                    likedId,
                    key -> new ArrayDeque<>()
            );

            if (!queue.contains(senderId)
                    && !senderId.equals(activeRequests.get(likedId))) {

                queue.add(senderId);
            }

            if (!activeRequests.containsKey(likedId)) {
                showNextRequest(likedId, print);
            }
        }
    }

    private void showNextRequest(
            String userId,
            ConsoleWriter print) throws Exception {

        Queue<String> queue = requests.get(userId);

        if (queue == null || queue.isEmpty()) {
            activeRequests.remove(userId);
            requests.remove(userId);
            return;
        }

        String senderId = queue.poll();

        activeRequests.put(userId, senderId);

        String profile = profileFinder.profilForUser(senderId);

        print.output(userId,
                "Тебе поставили лайк!\n\n"
                        + "Анкета пользователя:\n"
                        + profile
                        + "\n\nНравится ли он тебе?\n"
                        + "like — да\n"
                        + "skip — нет");
    }

    public void handleAnswer(
            String userId,
            String answer,
            ConsoleWriter print) throws Exception {

        String senderId = activeRequests.get(userId);

        if (senderId == null) {
            return;
        }

        answer = answer.trim().toLowerCase();

        if (!answer.equals("like") && !answer.equals("skip")) {
            print.output(userId,
                    "Напиши like или skip.");
            return;
        }

        // Если пользователь ответил like
        if (answer.equals("like")) {

            print.output(userId,
                    "У вас взаимная симпатия!\n"
                            + "Telegram ID: " + senderId);

            print.output(senderId,
                    "У вас взаимная симпатия!\n"
                            + "Telegram ID: " + userId);
        }


        activeRequests.remove(userId);

        showNextRequest(userId, print);
    }
}
