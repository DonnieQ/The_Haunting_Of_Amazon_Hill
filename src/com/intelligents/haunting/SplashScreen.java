package com.intelligents.haunting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class SplashScreen implements java.io.Serializable{
    public static Scanner scanner = new Scanner(System.in);
    printFiles p = new printFiles();


    public void splash(){
        System.out.println("What game would you like to play?\n " +
                "Chapter 1. The Haunting on Amazon Hill\n " +
                "Chapter 2. Chasing Ghosts\n " +
                "Chapter 3. Hangman's Gallows\n " +
                "Please enter a number for Chapter:");
        String gameType = getUserInput();
        if (gameType.matches("1")) {
            Game g = new Game();
           // String banner = Files.readString(Path.of("The_Haunting_Of_Amazon_Hill/resources", "introText"));
            p.print("introText");
            //String intro = Files.readString(Path.of("The_Haunting_Of_Amazon_Hill/resources", "settingTheScene"));
            p.print("settingTheScene");
           // System.out.println(banner);
            //System.out.println(intro);
            g.start();

        } else {
            System.out.println("invalid selection , please choose 1.");
            splash();
        }
    }

    public void pressEnterToContinue() {
        p.print("splashScreen");
        System.out.println("Press Enter key to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }


    public static String getUserInput() {
        return scanner.nextLine();
    }
}
