package com.intelligents.haunting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    World world = new World();
    private List<Ghost> ghosts = new ArrayList<>();

    private Ghost currentGhost;

    private Random r = new Random();

    public Game() {
        populateGhostList();
        setCurrentGhost(getRandomGhost());
    }

    void start() {
        boolean isValidInput;
        boolean isGameRunning = true;
        String[] input;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Thank you for choosing to play The Haunting on Amazon Hill. " +
                "What would you like your name to be? ");
        System.out.println(">>");

        input = scanner.nextLine().strip().toLowerCase().split(" ");

        Player player = new Player(input[0]);

        //System.out.println(player);
        System.out.println("Good luck to you, Detective " + player.getName());
        System.out.println("---------------------------");


        while (isGameRunning) {
            isValidInput = true;

            System.out.println("Your location is " + world.currentRoom.getRoomTitle());
            System.out.println(world.currentRoom.getDescription());
            System.out.println("To move type: Go North, Go East, Go South, or Go West");
            System.out.println("---------------------------");
                        System.out.println("****************************");
            System.out.println(">>");

            input = scanner.nextLine().strip().toLowerCase().split(" ");


            switch (input[0]) {
                case "read":
                    System.out.println("****************************");
                    System.out.println(player);
                    System.out.println("****************************");
                    break;
                case "show":
                    System.out.println("****************************");
                    System.out.println("Your location is " + world.currentRoom.getRoomTitle());
                    if (world.currentRoom.getRoomItems().isEmpty()) {
                        System.out.println("Currently there are no items in "
                                + world.currentRoom.getRoomTitle());
                    }
                    else{
                        System.out.println("You look and notice an" + world.currentRoom.getRoomItems());
                            System.out.println("Would you like to document anything about this room? " +
                                    ">>>");
                            String journalEntry = scanner.nextLine().strip().toLowerCase();
                            player.setJournal(journalEntry);
                       // System.out.println(world.currentRoom.getRoomItems());

                        System.out.println(world.currentRoom.getRoomItems());
                    }
                    System.out.println("****************************");
                    break;
                case "exit":
                case "quit":
                case "q":
                    isGameRunning = false;
                    break;
                case "move":
                case "go":

                    while (isValidInput) {

                        switch (input[1]) {

                            case "north":
                            case "east":
                            case "south":
                            case "west":
                                if (world.currentRoom.roomExits.containsKey(input[1])) {
                                    world.currentRoom = world.currentRoom.roomExits.get(input[1]);
                                    isValidInput = false;

                                    break;
                                }
                            default:
                                System.out.println("You hit wall. Try again: ");
                                System.out.println(">>");
                                input = scanner.nextLine().strip().toLowerCase().split(" ");
                                break;

                        }

                    }

            }
        }
        System.out.println("Thank you for playing our game!!");
    }

    public void populateGhostList() {
        this.setGhosts(XMLParser.populateGhosts(XMLParser.readGhosts()));
    }

    public void printGhosts() {
        for (Ghost ghost : ghosts) {
            System.out.println(ghost);
        }
    }
    public Ghost getRandomGhost() {
        int index = r.nextInt(ghosts.size());
        return ghosts.get(index);
    }

    // Getters / Setters

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public void setGhosts(List<Ghost> ghosts) {
        this.ghosts = ghosts;
    }
    public Ghost getCurrentGhost() {
        return currentGhost;
    }
    public void setCurrentGhost(Ghost ghost) {
        this.currentGhost = ghost;
    }


}
