package ru.lovity.bot.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;

public class ProfileFinder {

    private final String pathProfils;

    public ProfileFinder(String pathProfils) {
        this.pathProfils = pathProfils;
    }

    public JsonObject getProfileById(String userId)
            throws Exception {

        try (FileReader reader = new FileReader(pathProfils)) {

            JsonArray profils = JsonParser.parseReader(reader)
                    .getAsJsonArray();

            for (int i = 0; i < profils.size(); i++) {

                JsonObject profil = profils.get(i)
                        .getAsJsonObject();

                String id = profil.get("id").getAsString();

                if (id.equals(userId)) {
                    return profil;
                }
            }
        }

        return null;
    }
    public String profilForUser(String id) throws Exception {

        JsonObject profil = getProfileById(id);

        if (profil == null) {
            return "Профиль не найден";
        }

        return profil.get("name").getAsString() + ", "
                + profil.get("age").getAsString() + ", "
                + profil.get("information").getAsString();
    }
}