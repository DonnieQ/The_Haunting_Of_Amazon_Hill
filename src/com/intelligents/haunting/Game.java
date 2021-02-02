package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game implements java.io.Serializable{
    private World world = new World();
    private List<Ghost> ghosts = new ArrayList<>();
    private SaveGame SaveGame = new SaveGame();
    private Ghost currentGhost;

    private Random r = new Random();
    Player player;
    private transient PrintFiles p = new PrintFiles();
    public Game() {
        populateGhostList();
        setCurrentGhost(getRandomGhost());
        assignRandomEvidenceToMap();
    }

    void start(boolean isGameLoaded) {
        boolean isValidInput;
        boolean isGameRunning = true;
        String divider = "*******************************************************************************************";
        String[] input;
        Scanner scanner = new Scanner(System.in);
        if (!isGameLoaded) {



            System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Thank you for choosing to play The Haunting of Amazon Hill. " +
                    "What would you like your name to be? " + ConsoleColors.RESET);
            System.out.println(">>");

            input = scanner.nextLine().strip().split(" ");

            player = new Player(input[0]);

            p.print("The_Haunting_Of_Amazon_Hill/resources", "Rules");

            //System.out.println(player);
            System.out.printf("%70s%n%n", ConsoleColors.WHITE_BOLD_BRIGHT + "Good luck to you, " + player.getName() + ConsoleColors.RESET);
           // System.out.println("---------------------------");

        }
        //has access to entire Game object. tracking all changes
        SaveGame.setGame(this);
        while (isGameRunning && !checkForWinner()) {
            isValidInput = true;
            checkForWinner();

            String ghostEmoji = "\uD83D\uDC7B ";

            String currentLoc = ConsoleColors.BLUE_BOLD + "Your location is " + world.getCurrentRoom().getRoomTitle() + ConsoleColors.RESET;
            String moveGuide = ConsoleColors.RESET+ConsoleColors.YELLOW + "To move type: Go North, Go East, Go South, or Go West" + ConsoleColors.RESET;

            System.out.printf("%45s %95s %n", currentLoc,moveGuide);

            System.out.println();
            System.out.println(ConsoleColors.RED_BOLD + world.getCurrentRoom().getDescription() + ConsoleColors.RESET);


            System.out.println(">>");

            input = scanner.nextLine().strip().toLowerCase().split(" ");

            String ghostString = ghosts.toString();


            try {
                switch (input[0]) {
                    case "read":
                        System.out.println(divider + "\n");
                        System.out.println(ConsoleColors.WHITE_BRIGHT + player  + ConsoleColors.RESET + "\n");
                        System.out.printf("%45s%n%n",ConsoleColors.BLACK_BACKGROUND + ghostEmoji + "Possible Ghosts " + ghostEmoji + ConsoleColors.RESET);
                        System.out.println(ConsoleColors.GREEN_BOLD + ghosts.toString() + ConsoleColors.RESET + "\n\n");
                        System.out.println(ConsoleColors.BLACK_BACKGROUND + "Rooms Visited: " + ConsoleColors.RESET);
                        System.out.println(ConsoleColors.BLUE_BOLD + player.getRoomsVisited() + ConsoleColors.RESET);
                        System.out.println(divider);

                        break;
                    case "save":
                        SaveGame.save();
                        break;
                    case "load":
                        SaveGame.loadGame();
                        break;
                    case "help":
                        p.print("The_Haunting_Of_Amazon_Hill/resources", "Rules");
                    case "open":
                        p.print("The_Haunting_Of_Amazon_Hill/resources","Map");
                        break;
                    case "look":
                    case "show":
                        System.out.println(divider);
                        System.out.printf("%46s%n", currentLoc);
                        if (world.getCurrentRoom().getRoomEvidence().isEmpty()) {
                            System.out.println("Currently there are no items in "
                                    + world.getCurrentRoom().getRoomTitle() +"\n");
                            System.out.println("Would you like to document anything about this room? \n" + ">>>");
                            String journalEntry = scanner.nextLine().strip();
                            if (journalEntry.equals("no")){
                                break;
                            }
                            player.setJournal(journalEntry);
                        }
                        else{
                            System.out.println("You look and notice: " + world.getCurrentRoom().getRoomEvidence() + "\n\n");
                                System.out.println("Would you like to document anything about this room? \n " + ">>>");
                                String journalEntry = scanner.nextLine().strip();
                                if (journalEntry.equals("no")){
                                    break;
                                }
                                player.setJournal(journalEntry);
                           // System.out.println(world.currentRoom.getRoomItems());

                            System.out.println(world.getCurrentRoom().getRoomEvidence());
                        }
                        System.out.println(divider);
                        break;
                    case "exit":
                    case "quit":
                    case "q":
                        isGameRunning = false;
                        break;
                    case "move":
                    case "go":

                        while (isValidInput) {
                            checkIfRoomVisited();
                            switch (input[1]) {

                                case "north":
                                case "east":
                                case "south":
                                case "west":
                                    if (world.getCurrentRoom().roomExits.containsKey(input[1])) {
                                        world.setCurrentRoom(world.getCurrentRoom().roomExits.get(input[1]));
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

    private void checkIfRoomVisited() {
        if (!player.getRoomsVisited().contains(world.getCurrentRoom().getRoomTitle())) {
            player.addToRoomsVisited(world.getCurrentRoom().getRoomTitle());
        }
    }

    public void printEverythingInWorld() {
        for (Room room : world.gameMap) {
            System.out.println(room.toString());
        }
    }
    public void printGhostsDesc() {
        ghosts.forEach(ghost -> System.out.println(ConsoleColors.BLACK_BACKGROUND_BRIGHT + ConsoleColors.GREEN_BRIGHT + ghost.toString() + ConsoleColors.RESET + "\n"));
    }
    // Getters / Setters


    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

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

    public void setWorld(World world) {
        this.world = world;
    }
    boolean checkForWinner() {

        int count = 0;

//        try {
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

//        } catch (NullPointerException e) {
//            System.out.println("Keep trying");
//        }

        return count == 2;
    }

}
