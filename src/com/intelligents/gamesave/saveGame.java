package com.intelligents.gamesave;

import com.intelligents.client.Main;
import com.intelligents.haunting.Game;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class saveGame {

    public saveGame() {
    }

    public static void save() {
        Game game = new Game();
        game.getCurrentGhost();
        try {
            FileOutputStream fos = new FileOutputStream("usr.save");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.flush();
            oos.close();
            System.out.println("Game saved\n");
        }catch (Exception e) {
            //System.out.println("Serialization Error! Can not save data.\n" + e.getClass() + ": " + e.getMessage() + "\n");
            System.out.println("Sorry currently cannot save game. **coming Soon!**");
        }
    }










}
