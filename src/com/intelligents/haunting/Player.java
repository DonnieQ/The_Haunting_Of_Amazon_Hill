package com.intelligents.haunting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class Player implements java.io.Serializable {

    private static Player playerSingleton;
    private String name;
    private final List<String> journal = new ArrayList<>();
    private final List<String> roomsVisited = new ArrayList<>();


    private Player() {
        if (playerSingleton != null) {
            throw new RuntimeException("Need to use getInstance()");
        }
    }


    static Player getInstance() {
        if (playerSingleton == null) {
            playerSingleton = new Player();
        }
        return playerSingleton;
    }


    void addToRoomsVisited(String roomTitle) {
        roomsVisited.add(roomTitle);
    }

    void setName(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    List<String> getRoomsVisited() {
        return roomsVisited;
    }

    List<String> getJournal() {
        return journal;
    }

    void setJournal(String journal) {
        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy @ HH:mm"));
        this.journal.add("Date: " + localDateString + " -> " + journal);
    }

    @Override
    public String toString() {
        return getName() + "'s"
                + " journal currently shows these items: " + "\uD83D\uDCD6" + ConsoleColors.RESET + "\n\n"
                + ConsoleColors.RED_BOLD_BRIGHT + getJournal() + ConsoleColors.RESET;
    }

}
