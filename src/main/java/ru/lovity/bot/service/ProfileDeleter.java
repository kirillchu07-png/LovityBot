package ru.lovity.bot.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;

public class ProfileDeleter {
    private final String pathProfils;
    private final Gson gson;

    public ProfileDeleter(String pathProfils) {
        this.pathProfils = pathProfils;
        this.gson = new Gson();
    }

    public boolean deleteProfile(String userId) throws Exception {
        JsonArray profils;

        try (FileReader reader = new FileReader(pathProfils)) {
            profils = JsonParser.parseReader(reader).getAsJsonArray();
        }

        boolean isDeleted = false;

        for (int i = 0; i < profils.size(); i++) {
            JsonObject profil = profils.get(i).getAsJsonObject();

            if (profil.get("id").getAsString().equals(userId)) {
                profils.remove(i);
                isDeleted = true;
                break;
            }
        }

        if (isDeleted) {
            try (FileWriter writer = new FileWriter(pathProfils)) {
                gson.toJson(profils, writer);
            }
        }

        return isDeleted;
    }
}