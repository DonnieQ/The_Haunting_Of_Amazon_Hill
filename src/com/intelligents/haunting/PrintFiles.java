package com.intelligents.haunting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class PrintFiles {


    public PrintFiles() {

    }


    public void print(String path, String fileToRead) {
        if (fileToRead != null) {
            String results = null;
            try {
                results = Files.readString(Path.of(path, fileToRead));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(ConsoleColors.BLACK_BACKGROUND + results + ConsoleColors.RESET);
        } else {
            System.out.println("Sorry that file is not in the Path.");
        }
    }

    void printAlternateColor(String path, String fileToRead) {
        if (fileToRead != null) {
            String results = null;
            try {
                results = Files.readString(Path.of(path, fileToRead));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(ConsoleColors.BLACK_BACKGROUND_BRIGHT + results + ConsoleColors.RESET);
        } else {
            System.out.println("Sorry that file is not in the Path.");
        }
    }


}

