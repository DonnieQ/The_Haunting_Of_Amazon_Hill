package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.List;

class World implements java.io.Serializable{

    private transient ReadFiles r = new ReadFiles();

    List<Room> gameMap = new ArrayList<>();

    private Room lobby = new Room("Lobby");
    private Room dungeon = new Room("Dungeon");
    private Room diningRoom = new Room("Dining Room");
    private Room balcony = new Room("Balcony");
    private Room furnaceRoom = new Room("Furnace");




    private Room currentRoom = lobby;

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public World() {


        lobby.roomExits.put("east", dungeon);
        lobby.roomExits.put("west", diningRoom);
        lobby.roomExits.put("north", balcony);
        lobby.roomExits.put("south", furnaceRoom);
        lobby.addItemToRoom("apple");

        lobby.setDescription(r.read("Lobby"));
        dungeon.setDescription(r.read("Dungeon"));
        diningRoom.setDescription(r.read("DinningRoom"));
        balcony.setDescription(r.read("Balcony"));
        furnaceRoom.setDescription(r.read("FurnaceRoom"));
        gameMap.add(lobby);


        //Moving from rooms to lobby
        dungeon.roomExits.put("west", lobby);
        gameMap.add(dungeon);

        diningRoom.roomExits.put("east", lobby);
        gameMap.add(diningRoom);

        balcony.roomExits.put("south", lobby);
        gameMap.add(balcony);

        furnaceRoom.roomExits.put("north", lobby);
        gameMap.add(furnaceRoom);

    }

//    boolean roomVisted() {
//        int count = 0;
//
//        try {
//            Object[] arr = gameMap.toArray();
//            for (Object o : arr) {
//                String x = (String) o;
//                String[] f = x.split(" ");
//                for (String s : f) {
//                    if () {
//                        count++;
//                    }
//                }
//            }
//
//        } catch (NullPointerException e) {
//            System.out.println("Keep trying");
//        }
//
//        return count == 2;
//    }


}
