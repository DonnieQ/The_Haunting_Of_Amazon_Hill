package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Room {
    private String roomTitle;
    private String description;
    private String roomEvidence;
    Map<String, Room> roomExits = new HashMap<>();
    private List<String> roomItems = new ArrayList<>();

    public Room(String title) {
        this.roomTitle = title;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoomEvidence() {
        try {
        return roomEvidence;
        }
        catch (NullPointerException e) {
            return "";
        }
    }

    public void setRoomEvidence(String roomEvidence) {
        this.roomEvidence = roomEvidence;
    }

    public Room(String description, String ghostTracks) {
        this.description = description;

    }

    public void addItemToRoom(String item) {
        roomItems.add(item);
    }

    public List<String> getRoomItems() {
        List<String> results = new ArrayList<>();
        if (roomItems.size() == 0) {
            return results;
        }
        return roomItems;
    }
    public void setRoomItems(List<String> roomItems) {
        this.roomItems = roomItems;
    }
}
