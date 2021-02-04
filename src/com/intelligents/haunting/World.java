package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class World implements java.io.Serializable {

    private final transient ReadFiles r = new ReadFiles();

    List<Room> gameMap = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    private final Room lobby = new Room("Lobby");
    private final Room dungeon = new Room("Dungeon");
    private final Room diningRoom = new Room("Dining Room");
    private final Room balcony = new Room("Balcony");
    private final Room furnaceRoom = new Room("Furnace");


    private Room currentRoom = null;

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public World() {
        //read all room objects in
        populateRoomList();
        currentRoom = rooms.get(0);

        HashMap<String, Room> map = new HashMap<>();
        // create map between room name and room object
        for (Room room: rooms) {
            map.put(room.getRoomTitle(),room);
            gameMap.add(room);
        }
        // get the directions from each room and map a direction to a room object using previous map
        for (Room room: rooms) {
            for (String key :room.directionList.keySet()) {
                String roomName = room.directionList.get(key);
                room.roomExits.put(key,map.get(roomName));
            }
        }

//        lobby.roomExits.put("east", dungeon);
//        lobby.roomExits.put("west", diningRoom);
//        lobby.roomExits.put("north", balcony);
//        lobby.roomExits.put("south", furnaceRoom);
//        lobby.addItemToRoom("apple");
//
//        lobby.setDescription(r.read("Lobby"));
//        dungeon.setDescription(r.read("Dungeon"));
//        diningRoom.setDescription(r.read("DinningRoom"));
//        balcony.setDescription(r.read("Balcony"));
//        furnaceRoom.setDescription(r.read("FurnaceRoom"));
//        gameMap.add(lobby);
//
//
//        //Moving from rooms to lobby
//        dungeon.roomExits.put("west", lobby);
//        gameMap.add(dungeon);
//
//        diningRoom.roomExits.put("east", lobby);
//        gameMap.add(diningRoom);
//
//        balcony.roomExits.put("south", lobby);
//        gameMap.add(balcony);
//
//        furnaceRoom.roomExits.put("north", lobby);
//        gameMap.add(furnaceRoom);

    }
    public void populateRoomList() {
        this.setRooms(XMLParserRoom.populateRooms(XMLParserRoom.readRooms()));
    }
    public List<Room> getRooms() {
        return rooms;
    }
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
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
