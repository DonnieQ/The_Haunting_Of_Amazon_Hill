package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class World implements java.io.Serializable {

    private final transient ReadFiles r = new ReadFiles();

    List<Room> gameMap = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private Room currentRoom = null;

    Room getCurrentRoom() {
        return currentRoom;
    }

    void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    World() {
        //read all room objects in
        populateRoomList();
        currentRoom = rooms.get(0);

        HashMap<String, Room> map = new HashMap<>();
        // create map between room name and room object
        for (Room room : rooms) {
            map.put(room.getRoomTitle(), room);
            gameMap.add(room);
        }
        // get the directions from each room and map a direction to a room object using previous map
        for (Room room : rooms) {
            for (String key : room.directionList.keySet()) {
                String roomName = room.directionList.get(key);
                room.roomExits.put(key, map.get(roomName));
            }
        }

    }

    void populateRoomList() {
        this.setRooms(XMLParser.populateRooms(XMLParser.readXML("Rooms"), "room"));
    }

    List<Room> getRooms() {
        return rooms;
    }

    void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

}
