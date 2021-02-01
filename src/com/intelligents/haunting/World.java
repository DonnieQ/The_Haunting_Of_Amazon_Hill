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


        //  dungeon.roomExits.put("east", dungeon);
        dungeon.roomExits.put("west", lobby);
        //    dungeon.roomExits.put("north", balcony);
        //    dungeon.roomExits.put("south", furnaceRoom);

        gameMap.add(dungeon);


        //  diningRoom.roomExits.put("east", dungeon);
        diningRoom.roomExits.put("east", lobby);
        //   diningRoom.roomExits.put("north", balcony);
        //  diningRoom.roomExits.put("south", furnaceRoom);

        gameMap.add(diningRoom);


        //  balcony.roomExits.put("east", dungeon);
        //   balcony.roomExits.put("west", diningRoom);
        //   balcony.roomExits.put("north", balcony);
        balcony.roomExits.put("south", lobby);

        gameMap.add(balcony);


        // furnaceRoom.roomExits.put("east", dungeon);
        //    furnaceRoom.roomExits.put("west", diningRoom);
        furnaceRoom.roomExits.put("north", lobby);
        // furnaceRoom.roomExits.put("south", furnaceRoom);

        gameMap.add(furnaceRoom);

    }


}
