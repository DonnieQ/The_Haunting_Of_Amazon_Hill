package com.intelligents.haunting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class PrintFiles {


    PrintFiles() {

    }


    void print(String path, String fileToRead) {
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
            System.out.println(ConsoleColors.RED_BOLD + results + ConsoleColors.RESET);
        } else {
            System.out.println("Sorry that file is not in the Path.");
        }
    }

    void printHelp(String path, String path2, String fileToRead, String fileToRead2) {
        if (fileToRead != null) {
            String results = null;
            String results2 = null;
            try {
                results = Files.readString(Path.of(path, fileToRead));
                results2 = Files.readString(Path.of(path2, fileToRead2));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.printf("%-15s %100s %n", results, results2);
        } else {
            System.out.println("Sorry that file is not in the Path.");
        }
    }


}

