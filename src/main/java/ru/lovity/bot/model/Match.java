package ru.lovity.bot.model;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.util.Set;
import java.util.HashSet;

public class Match {

    public int ifMatch(JsonObject profile1, JsonObject profile2) {

        JsonElement gender1 = profile1.get("gender");
        JsonElement gender2 = profile2.get("gender");

        if (gender1 != null && gender2 != null
                && !gender1.isJsonNull()
                && !gender2.isJsonNull()
                && gender1.getAsString().trim()
                .equalsIgnoreCase(gender2.getAsString().trim())) {
            return -1;
        }

        Set<String> keys = new HashSet<>();

        for (String key : profile1.keySet()) {
            if (!key.equals("id")
                    && !key.equals("gender")
                    && !key.equals("name")
                    && !key.equals("username")
                    && !key.equals("information")
                    && !key.equals("age")) {
                keys.add(key);
            }
        }

        for (String key : profile2.keySet()) {
            if (!key.equals("id")
                    && !key.equals("gender")
                    && !key.equals("name")
                    && !key.equals("username")
                    && !key.equals("information")
                    && !key.equals("age")) {
                keys.add(key);
            }
        }

        if (keys.isEmpty()) {
            return 1;
        }

        int matches = 0;

        for (String key : keys) {
            JsonElement answer1 = profile1.get(key);
            JsonElement answer2 = profile2.get(key);

            if (answer1 == null || answer2 == null
                    || answer1.isJsonNull()
                    || answer2.isJsonNull()) {
                continue;
            }

            String value1 = answer1.getAsString().trim();
            String value2 = answer2.getAsString().trim();

            if (value1.equalsIgnoreCase(value2)) {
                matches++;
            }
        }

        int result = Math.round(
                (float) matches / keys.size() * 100
        );

        return Math.max(1, result);
    }
}