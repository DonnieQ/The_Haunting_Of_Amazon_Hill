package com.intelligents.haunting;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

class PrintFiles {

    /**Reads text files and displays it's content**/


    PrintFiles() {

    }




    void print(String path, String fileToRead, ClassLoader cl) throws IOException {
        if (fileToRead != null) {
            InputStream results = null;
            results = cl.getResourceAsStream(path + fileToRead);
//            String results = "";
//                results = Files.readString(Path.of(path, fileToRead));
            System.out.println(ConsoleColors.BLACK_BACKGROUND + results.transferTo(System.out) + ConsoleColors.RESET);
        } else {
            System.out.println("Sorry that file is not in the Path.");
        }
    }

    void printAlternateColor(String path, String fileToRead, ClassLoader cl) {
        if (fileToRead != null) {
            InputStream results = null;
            results = cl.getResourceAsStream(path + fileToRead);

//                results = Files.readString(Path.of(path, fileToRead));
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

