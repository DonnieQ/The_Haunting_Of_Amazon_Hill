package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Room implements java.io.Serializable {
    private String roomTitle;
    private String description;
    private String roomEvidence = "";
    private MiniGhost roomMiniGhost = null;
    Map<String, Room> roomExits = new HashMap<>();
    private List<String> roomItems = new ArrayList<>();
    public Map<String, String> directionList = new HashMap<>();

    Room() {

    }

    Room(String title) {
        this.roomTitle = title;
    }

    Room(String title, String description) {
        this.roomTitle = title;
        this.description = description;
    }

    public MiniGhost getRoomMiniGhost() {
        return roomMiniGhost;
    }

    public void setRoomMiniGhost(MiniGhost roomMiniGhost) {
        this.roomMiniGhost = roomMiniGhost;
    }

    String getRoomTitle() {
        return roomTitle;
    }

    void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getRoomEvidence() {
        try {
            return roomEvidence;
        } catch (NullPointerException e) {
            return "";
        }
    }

    void setRoomEvidence(String roomEvidence) {
        this.roomEvidence = roomEvidence;
    }


    void addItemToRoom(String item) {
        roomItems.add(item);
    }

    List<String> getRoomItems() {
        List<String> results = new ArrayList<>();
        if (roomItems.size() == 0) {
            return results;
        }
        return roomItems;
    }

    void setRoomItems(List<String> roomItems) {
        this.roomItems = roomItems;
    }
}
