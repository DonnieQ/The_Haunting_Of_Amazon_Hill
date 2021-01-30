package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game implements java.io.Serializable{
    private World world = new World();
    private List<Ghost> ghosts = new ArrayList<>();
    private saveGame saveGame = new saveGame();
    private Ghost currentGhost;

    private Random r = new Random();
    Player player;
    printFiles p = new printFiles();
    public Game() {
        populateGhostList();
        setCurrentGhost(getRandomGhost());
        assignRandomEvidenceToMap();
    }

    void start() {
        boolean isValidInput;
        boolean isGameRunning = true;
        String[] input;
        Scanner scanner = new Scanner(System.in);

        System.out.println(ConsoleColors.GREEN_BOLD+ "Thank you for choosing to play The Haunting on Amazon Hill. " +
                "What would you like your name to be? " + ConsoleColors.RESET);
        System.out.println(">>");

        input = scanner.nextLine().strip().toLowerCase().split(" ");

        player = new Player(input[0]);

        p.print("The_Haunting_Of_Amazon_Hill/resources", "Rules");

        //System.out.println(player);
        System.out.println("Good luck to you, Detective " + player.getName());
        System.out.println("---------------------------");


        while (isGameRunning && !checkForWinner()) {
            isValidInput = true;
            checkForWinner();

            System.out.println(ConsoleColors.RED + "Your location is " + world.currentRoom.getRoomTitle() + ConsoleColors.RESET);
            System.out.println(ConsoleColors.RED + world.currentRoom.getDescription() + ConsoleColors.RESET);
            System.out.println(ConsoleColors.YELLOW + "To move type: Go North, Go East, Go South, or Go West" + ConsoleColors.RESET);
           // System.out.println("---------------------------");
           // System.out.println("****************************");
            System.out.println(">>");

            input = scanner.nextLine().strip().toLowerCase().split(" ");


            try {
                switch (input[0]) {
                    case "read":
                        System.out.println("****************************");
                        System.out.println(ConsoleColors.BLACK_BACKGROUND + player + ConsoleColors.RESET);
                        System.out.println("****************************");
                        System.out.println(ConsoleColors.BLACK_BACKGROUND + "Possible Ghosts: " + ConsoleColors.RESET);
                        printGhostsDesc();
                        System.out.println("****************************");
                        break;
                    case "save":
                        saveGame.save();
                        break;
                    case "help":
                        p.print("The_Haunting_Of_Amazon_Hill/resources", "Rules");
                    case "open":
                        p.print("The_Haunting_Of_Amazon_Hill/resources","Map");
                        break;
                    case "look":
                    case "show":
                        System.out.println("****************************");
                        System.out.println("Your location is " + world.currentRoom.getRoomTitle());
                        if (world.currentRoom.getRoomEvidence().isEmpty()) {
                            System.out.println("Currently there are no items in "
                                    + world.currentRoom.getRoomTitle());
                            System.out.println("Would you like to document anything about this room? " +
                                    ">>>");
                            String journalEntry = scanner.nextLine().strip();
                            if (journalEntry.equals("no")){
                                break;
                            }
                            player.setJournal(journalEntry);
                        }
                        else{
                            System.out.println("You look and notice: " + world.currentRoom.getRoomEvidence());
                                System.out.println("Would you like to document anything about this room? " +
                                        ">>>");
                                String journalEntry = scanner.nextLine().strip();
                                if (journalEntry.equals("no")){
                                    break;
                                }
                                player.setJournal(journalEntry);
                           // System.out.println(world.currentRoom.getRoomItems());

                            System.out.println(world.currentRoom.getRoomEvidence());
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
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Please say \'Move\' or \'Go\' before choosing a direction!");
            }
        }
        System.out.println("Thank you for playing our game!!");
    }

    public void populateGhostList() {
        this.setGhosts(XMLParser.populateGhosts(XMLParser.readGhosts()));
    }

    public void printGhosts() {
        for (Ghost ghost : ghosts) {
            System.out.println(ghost.toString());
        }
    }
    public Ghost getRandomGhost() {
        int index = r.nextInt(ghosts.size());
        return ghosts.get(index);
    }
    private void assignRandomEvidenceToMap() {
        try {
            //for each evidence from monster, get rooms from world.gamemap equivalent to the number of evidences.
            for (int i = 0; i < currentGhost.getEvidence().size(); i++) {
                // Success condition
                boolean addedEvidence = false;

                // Loop while no success
                while (!addedEvidence) {
                    Room x = getRandomRoomFromWorld();
                    // System.out.println("random room chosen is " + x.getRoomTitle());
                    if (x.getRoomEvidence().equals("")) {
                        x.setRoomEvidence(currentGhost.getEvidence().get(i));
                        // System.out.println("added " + currentGhost.getEvidence().get(i) + " to " + x.getRoomTitle());
                        addedEvidence = true;
                    }
                }

            }
        }
        catch (NullPointerException e){
            System.out.println("The data given is empty, cannot perform function");
        }
    }
    private Room getRandomRoomFromWorld() {
        int index = r.nextInt(world.gameMap.size());
        return world.gameMap.get(index);
    }

    public void printEverythingInWorld() {
        for (Room room : world.gameMap) {
            System.out.println(room.toString());
        }
    }
    public void printGhostsDesc() {
        ghosts.forEach(ghost -> System.out.println(ConsoleColors.BLACK_BACKGROUND_BRIGHT + ConsoleColors.GREEN_BRIGHT + ghost.toString() + ConsoleColors.RESET));
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
    public World getWorld() {
        return world;
    }
    boolean checkForWinner() {

        int count = 0;

        try {
            Object[] arr = player.getJournal().toArray();
            for (Object o : arr) {
                String x = (String) o;
                String[] f = x.split(" ");
                for (String s : f) {
                    if (currentGhost.getEvidence().contains(s)) {
                        count++;
                    }
                }
            }

        } catch (NullPointerException e) {
            System.out.println("Keep trying");
        }

        return count == 2;
    }

}
