package com.intelligents.haunting;

import java.io.IOException;

public class Controller {


    private boolean introScreen;


    private boolean nameSet;
    private Game game;

    public Controller(Game game) throws IOException {
        introScreen = true;
        nameSet = false;
        this.game = game;
    }

    public void kickoffResponse(String[] response) {
        if (introScreen) {
            game.intro(response);
            if (response[0].equals("1") || response[0].equals("4")) {
                introScreen = false;
            }
        } else if (!introScreen && !nameSet) {
            game.createPlayer(response);
            nameSet = true;
        } else {
            game.processInput(true, response, 0);
        }
    }


}
