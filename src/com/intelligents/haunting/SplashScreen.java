package com.intelligents.haunting;

import java.util.Scanner;

public class SplashScreen implements java.io.Serializable {
    public static Scanner scanner = new Scanner(System.in);
    PrintFiles p = new PrintFiles();
    SaveGame save = new SaveGame();
    private final MusicPlayer themeSong = new MusicPlayer("resources/Sounds/VIKINGS THEME SONG.wav");

    public SplashScreen() {
        themeSong.playSoundEffect();
        themeSong.setVolume((float) -10.69);
    }

    public void splash() {
//generates game selection

        System.out.print(ConsoleColors.YELLOW_BRIGHT + "What game would you like to play?\n " +
                "Chapter 1. The Haunting of Amazon Hill\n " +
                "Chapter 2. Chasing Ghosts (COMING SOON!)\n " +
                "Chapter 3. Hangman's Gallows (COMING SOON!)\n " +
                "Press 4. to load saved game\n" +
                "Please enter a number for Chapter:\n" +
                ConsoleColors.RESET + ">>");
        String gameType = getUserInput();
        // If 1 was selected then a new game is loaded
        if (gameType.matches("1")) {
            themeSong.stopSoundEffect();
            Game g = new Game();

            p.print("resources", "introText");

            p.printAlternateColor("resources", "settingTheScene");

            g.start(false);
        //If loaded game was selected then the saved file is loaded
        } else if (gameType.matches("4")) {
            try {
                themeSong.stopSoundEffect();
                Game g = new Game();
                save.setGame(g);
                save.loadGame();
                g.start(true);
            } catch (NullPointerException e) {
                splash();
            }

        } else {
            System.out.println("Invalid selection , please enter 1.");
            splash();
        }
    }

    public void pressEnterToContinue() {
        p.print("resources", "splashScreen");
        System.out.println(ConsoleColors.YELLOW + "Press Enter key to continue..." + ConsoleColors.RESET);
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static String getUserInput() {
        return scanner.nextLine();
    }
}
