package ru.lovity.bot.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AnketaParser {
    private Scanner inputFile;

    public AnketaParser(String path) throws FileNotFoundException {
        inputFile = new Scanner(new File(path));
    }

    public boolean hasNextLine() {
        return inputFile.hasNextLine();
    }

    public String nextLine() {
        return inputFile.nextLine();
    }

    public void close() {
        inputFile.close();
    }

}
