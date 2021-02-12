package com.intelligents.haunting;

import java.io.IOException;

public class Controller {


    private boolean introScreen;
    private boolean nameSet;
    private boolean readyToGuess;
    private Game game;

    public Controller(Game game) throws IOException {
        introScreen = true;
        nameSet = false;
        readyToGuess = false;
        this.game = game;
    }

    public void kickoffResponse(String[] response, String promptToUser) throws IOException {
        // Get what chapter the user wants
        if (introScreen) {
            game.intro(response);
            if (response[0].equals("1") || response[0].equals("4")) {
                introScreen = false;
            }
        // Gain the users name
        } else if (!introScreen && !nameSet) {
            game.createPlayer(response);
            nameSet = true;
        // If the user is trying to exit, get if they want to guess or stay inside
        } else if (promptToUser.equals("It seems your journal does not have all of the evidence needed to determine the ghost." +
                " Would you like to GUESS the ghost anyway or go back INSIDE?\n>>") || promptToUser.equals("Invalid input, please decide whether you want to GUESS or go back INSIDE.\n>>")
                || promptToUser.equals("It seems like you could be ready to determine the ghost." +
                " Would you like to GUESS the ghost or go back INSIDE to continue exploring?\n>>")){
            if (response[0].contains("guess")) {
                readyToGuess = true;
            }
            game.guessOrGoBackInside(response[0]);
        // Player has indicated that they are ready to guess
        } else if (readyToGuess) {
            game.userGuess(response[0]);
        // Player has option to write in journal or not
        } else if (promptToUser.equals("Would you like to document anything in your journal? [Yes/No]\n") || promptToUser.equals("Invalid Journal entry. Please look/show again to document again.\n")) {
            game.writeEntryInJournal(response[0]);
        } else if (promptToUser.equals("Your entry:\n>> ")) {
            game.inputEntryInJournal(response[0]);
        } else {
            game.processInput(true, response, game.attemptCount);
        }
    }


}
