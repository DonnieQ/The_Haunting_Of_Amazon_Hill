package com.intelligents.haunting;

import com.intelligents.client.Main;
import com.intelligents.haunting.Game;
//import com.intelligents.haunting.Player;
//import com.intelligents.haunting.Room;
//import com.intelligents.haunting.World;  ?? Done: ask team about package private World, Player and Room

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class saveGame {
    private Game game;

    public saveGame() {
    }

    public void save() {
       // Game game = new Game();


        //world.currentRoom.getRoomItems();
        try {
            FileOutputStream fos = new FileOutputStream("usr.save");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(game.getWorld());
            oos.writeObject(game.getCurrentGhost());
            oos.writeObject(game.getGhosts());
            oos.writeObject(game.getPlayer());
            oos.flush();
            oos.close();
            System.out.println("Game saved\n");
        }catch (Exception e) {
            //System.out.println("Serialization Error! Can not save data.\n" + e.getClass() + ": " + e.getMessage() + "\n");
            System.out.println("Sorry currently cannot save game. **coming Soon!**");
            e.printStackTrace();
        }



    }

    @SuppressWarnings("unchecked")  //i wrote this code, so i can guarantee this is an array of objects
    public void loadGame() {
        try {
            FileInputStream fis = new FileInputStream("usr.save");
            ObjectInputStream ois = new ObjectInputStream(fis);

            World world = (World) ois.readObject();
            Ghost ghost = (Ghost) ois.readObject();
            ArrayList<Ghost> ghosts = (ArrayList<Ghost>) ois.readObject();
            Player player = (Player) ois.readObject();
            game.setPlayer(player);
            game.setWorld(world);
            game.setCurrentGhost(ghost);
            game.setGhosts(ghosts);
            ois.close();
        }catch (Exception e) {
            System.out.println("Could not load saved game");
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }










}
