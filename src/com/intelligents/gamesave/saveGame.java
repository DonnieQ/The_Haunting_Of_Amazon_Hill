package com.intelligents.gamesave;

import com.intelligents.client.Main;
import com.intelligents.haunting.Game;
//import com.intelligents.haunting.Player;
//import com.intelligents.haunting.Room;
//import com.intelligents.haunting.World;  ?? TODO: ask team about package private World, Player and Room

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class saveGame {

    public saveGame() {
    }

    public static void save() {
        Game game = new Game();
       // World world = new World();
       // Room room = new Room();
       // Player player = new Player();

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
