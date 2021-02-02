package com.intelligents.haunting;

import java.util.Scanner;

public class SplashScreen implements java.io.Serializable {
    public static Scanner scanner = new Scanner(System.in);
    PrintFiles p = new PrintFiles();
    SaveGame save = new SaveGame();

    public void splash() {


        System.out.println(ConsoleColors.YELLOW_BRIGHT + "What game would you like to play?\n " +
                "Chapter 1. The Haunting of Amazon Hill\n " +
                "Chapter 2. Chasing Ghosts\n " +
                "Chapter 3. Hangman's Gallows\n " +
                "Press 4. to load saved game\n" +
                "Please enter a number for Chapter: \n\n" +
                ConsoleColors.RESET);
        String gameType = getUserInput();
        if (gameType.matches("1")) {
            Game g = new Game();

            p.print("The_Haunting_Of_Amazon_Hill/resources", "introText");

            p.printAlternateColor("The_Haunting_Of_Amazon_Hill/resources", "settingTheScene");

            g.start(false);

        } else if (gameType.matches("4")) {
            try {
                Game g = new Game();
                save.setGame(g);
                save.loadGame();
                g.start(true);
            } catch (NullPointerException e) {
                splash();
            }

        } else {
            System.out.println("invalid selection , please choose 1.");
            splash();
        }
    }

    public void pressEnterToContinue() {
        p.print("The_Haunting_Of_Amazon_Hill/resources", "splashScreen");
        System.out.println(ConsoleColors.YELLOW + "Press Enter key to continue..." + ConsoleColors.RESET);
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }


    public static String getUserInput() {
        return scanner.nextLine();
    }
}
