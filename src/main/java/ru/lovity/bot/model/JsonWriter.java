package ru.lovity.bot.model;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class JsonWriter {
    public void save(Map<String, String> answers, String path)
            throws IOException {

        File profils = new File(path);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(answers);

        try (RandomAccessFile file = new RandomAccessFile(profils, "rw")) {

            if (file.length() == 0) {
                file.write(("[\n" + json + "\n]")
                        .getBytes(StandardCharsets.UTF_8));
                return;
            }

            long pos = file.length() - 1;

            while (pos >= 0) {
                file.seek(pos);
                int c = file.read();

                if (!Character.isWhitespace((char) c)) {
                    break;
                }

                pos--;
            }

            if (pos < 0) {
                throw new IOException("JSON файл пустой или некорректный");
            }

            file.seek(pos);

            if (file.read() != ']') {
                throw new IOException("JSON должен содержать массив");
            }

            // Проверяем, есть ли уже профили
            long previous = pos - 1;

            while (previous >= 0) {
                file.seek(previous);
                int c = file.read();

                if (!Character.isWhitespace((char) c)) {
                    break;
                }

                previous--;
            }

            file.seek(pos);

            String addition = previous >= 0 && previous != 0
                    ? ",\n" + json + "\n]"
                    : "\n" + json + "\n]";

            file.write(addition.getBytes(StandardCharsets.UTF_8));
            file.setLength(file.getFilePointer());
        }
    }

}