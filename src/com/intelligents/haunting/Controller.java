package com.intelligents.haunting;

import java.io.IOException;

public class Controller {
    private boolean introScreen;
    private boolean addPlayerName;
    private Game game;

    public Controller(Game game) throws IOException {
        introScreen = true;
        addPlayerName = true;
        this.game = game;
    }

    public void kickoffResponse(String[] response) {
        if (introScreen) {
            game.intro(response);
            introScreen = false;
        }
    }
}
