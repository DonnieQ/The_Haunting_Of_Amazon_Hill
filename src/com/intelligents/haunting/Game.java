package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game implements java.io.Serializable {
    private World world = new World();
    private List<Ghost> ghosts = new ArrayList<>();
    private final SaveGame SaveGame = new SaveGame();
    private Ghost currentGhost;
    private final Random r = new Random();
    private final String divider = "*******************************************************************************************";
    private Player player;
    private final transient PrintFiles p = new PrintFiles();
    private MusicPlayer mp = new MusicPlayer("The_Haunting_Of_Amazon_Hill/resources/Sounds/Haunted Mansion.wav");
    private MusicPlayer soundEffect = new MusicPlayer("The_Haunting_Of_Amazon_Hill/resources/Sounds/page-flip-4.wav");
    private MusicPlayer walkEffect = new MusicPlayer("The_Haunting_Of_Amazon_Hill/resources/Sounds/footsteps-4.wav");
    private MusicPlayer keyboardEffect = new MusicPlayer("The_Haunting_Of_Amazon_Hill/resources/Sounds/fast-pace-typing.wav");
    private MusicPlayer paperFalling = new MusicPlayer("The_Haunting_Of_Amazon_Hill/resources/Sounds/paper flutter (2).wav");
    private final Scanner scanner = new Scanner(System.in);

    public Game() {
        populateGhostList();
        setCurrentGhost(getRandomGhost());
        assignRandomEvidenceToMap();
    }

    void start(boolean isGameLoaded) {
        boolean isValidInput;
        boolean isGameRunning = true;

        String[] input;


        mp.startMusic();
        if (!isGameLoaded) {


            System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Thank you for choosing to play The Haunting of Amazon Hill. " +
                    "What would you like your name to be? " + ConsoleColors.RESET);
            System.out.println(">>");

            input = scanner.nextLine().strip().split(" ");

            player = Player.getInstance();

            player.setName(input[0]);

            System.out.printf("%175s%n", ConsoleColors.CYAN_UNDERLINED + " --> If you're new to the game type help for assistance" + ConsoleColors.RESET);

            //System.out.println(player);
            System.out.printf("%70s%n%n", ConsoleColors.WHITE_BOLD_BRIGHT + "Good luck to you, " + player.getName() + ConsoleColors.RESET);

        }
        //has access to entire Game object. tracking all changes
        SaveGame.setGame(this);


        while (isGameRunning) {
            isValidInput = true;

            String currentLoc = ConsoleColors.BLUE_BOLD + "Your location is " + world.getCurrentRoom().getRoomTitle() + ConsoleColors.RESET;
            String moveGuide = ConsoleColors.RESET + ConsoleColors.YELLOW + "To move type: Go North, Go East, Go South, or Go West" + ConsoleColors.RESET;

            System.out.printf("%45s %95s %n", currentLoc, moveGuide);

            System.out.println();
            // System.out.println(ConsoleColors.RED_BOLD + world.getCurrentRoom().getDescription() + ConsoleColors.RESET);

            narrateRooms(getRandomRoomFromWorld().getDescription());

            System.out.println(">>");


            input = scanner.nextLine().strip().toLowerCase().split(" ");


//            if (input[0].equals("volume") && input[1].equals())


            // Checks if current room is in roomsVisited List. If not adds currentRoom to roomsVisited
            checkIfRoomVisited();
            try {
                switch (input[0]) {
                    case "chris":
                        chrisIsCool();
                        break;
                    case "volume":
                        if (input[1].equals("up")) {
                            mp.setVolume(5.0f);
                        } else {
                            mp.setVolume(-15.0f);
                        }
                        break;

                    case "read":
                        printJournal();
                        soundEffect.playSoundEffect();
                        break;
                    case "save":
                        SaveGame.save();
                        break;
                    case "load":
                        SaveGame.loadGame();
                        break;
                    case "help":
                        //  p.print("The_Haunting_Of_Amazon_Hill/resources", "Rules");
                        p.print("The_Haunting_Of_Amazon_Hill/resources", "Rules");
                        break;
                    case "open":
                        p.print("The_Haunting_Of_Amazon_Hill/resources", "Map");
                        break;
                    case "look":
                    case "show":
                        System.out.println(divider);
                        System.out.printf("%46s%n", currentLoc);
                        if (world.getCurrentRoom().getRoomEvidence().isEmpty()) {
                            narrate("Currently there are no items in "
                                    + world.getCurrentRoom().getRoomTitle() + "\n");
                            narrate("Would you like to document anything about this room? [Yes/No]\n" + ">>>");
                            writeEntryInJournal();
                        } else {
                            narrate("You look and notice: " + world.getCurrentRoom().getRoomEvidence() + "\n\n");
                            narrate("Journal currently opened, would you like to document anything about this room? [Yes/No]\n " + ">>>");
                            writeEntryInJournal();
                            // System.out.println(world.currentRoom.getRoomItems());
                        }
                        System.out.println(divider);
                        break;
                    case "exit":
                        if (userAbleToExit()) {
                            // In order to win, user has to have correct evidence and guessed right ghost
                            if (!checkIfHasAllEvidenceIsInJournal()) {
                                narrate("It seems your journal does not have all of the evidence needed to determine the ghost." +
                                        " Would you like to guess the ghost anyway or go back inside?");
                                String ans = "";
                                boolean validEntry = false;
                                while (!validEntry) {
                                    ans = scanner.nextLine().strip().toLowerCase();
                                    if (ans.contains("guess") || ans.contains("inside")) {
                                        validEntry = true;
                                    } else {
                                        narrate("Invalid input, please decide whether you want to guess or go back inside");
                                    }
                                }
                                if (ans.contains("inside")) {
                                    break;
                                }
                            }
                            String userGuess = getTypeOfGhostFromUser();
                            if (userGuess.equalsIgnoreCase(currentGhost.getType())) {
                                narrate("You won");
                                getGhostBackstory();
                                isGameRunning = false;
                            } else {
                                narrate("Unfortunately, the ghost you determined was incorrect. The correct ghost was \n"
                                        + currentGhost.toString() + "You have been loaded into a new world. Good luck trying again.\n");
                                resetWorld();
                            }
                        }
                        break;
                    case "quit":
                    case "q":
                        //clip.close();
                        mp.quitMusic();
                        isGameRunning = false;
                        break;
                    case "pause":
                        // clip.stop();
                        mp.pauseMusic();
                        break;
                    case "play":
                        // clip.start();
                        mp.startMusic();
                        break;
                    case "move":
                    case "go":

                        while (isValidInput) {
                            switch (input[1]) {

                                case "north":
                                case "east":
                                case "south":
                                case "west":
                                    if (world.getCurrentRoom().roomExits.containsKey(input[1])) {
                                        world.setCurrentRoom(world.getCurrentRoom().roomExits.get(input[1]));
                                        isValidInput = false;
                                        walkEffect.playSoundEffect();
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
                System.out.println("Make sure to add a verb e.g. 'move', 'go', 'open', 'read' then a noun e.g. 'north', 'map', 'journal' ");
            }

        }
//        } catch (ArrayIndexOutOfBoundsException | LineUnavailableException | IOException | UnsupportedAudioFileException e) {
//            e.printStackTrace();
//        }
        System.out.println("Thank you for playing our game!!");
    }

    private String getTypeOfGhostFromUser() {
        narrate("You've collected all the evidence you could find. " +
                "Based on your expertise, make an informed decision on what type of " +
                "ghost is haunting Amazon Hill.");
        narrate("Here are all the possible ghosts");
        System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT);
        ghosts.forEach(ghost -> System.out.println(ghost.getType()));
        System.out.print(ConsoleColors.RESET);
        narrate("Which Ghost do you think it is?");
        String userGuessed = scanner.nextLine().strip();
        narrate("Good job gathering evidence " + player.getName() + "\nYou guessed " + userGuessed);
        return userGuessed;
    }

    private void writeEntryInJournal() {
        String journalEntry = scanner.nextLine().strip();
        if (journalEntry.equals("no")) {
            narrate("Journal Closed.");
        } else if (journalEntry.equalsIgnoreCase("yes")) {
            narrate("Your entry: ");
            journalEntry = scanner.nextLine().strip();
            player.setJournal(journalEntry);
        } else {
            System.out.println("Invalid Journal entry. Please look/show again to document again.");
        }
    }


    private void printJournal() {
        String ghostEmoji = "\uD83D\uDC7B ";
        String houseEmoji = "\uD83C\uDFE0";
        String bookEmoji = "\uD83D\uDCD6";
        System.out.println(divider + "\n");
        System.out.println(ConsoleColors.BLACK_BACKGROUND + bookEmoji + " " + player + ConsoleColors.RESET + "\n");
        System.out.printf("%45s%n%n", ConsoleColors.BLACK_BACKGROUND + ghostEmoji + "Possible Ghosts " + ghostEmoji + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + ghosts.toString() + ConsoleColors.RESET + "\n");
        System.out.printf("%43s%n%n", ConsoleColors.BLACK_BACKGROUND + houseEmoji + " Rooms visited " + houseEmoji + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE_BOLD + player.getRoomsVisited() + ConsoleColors.RESET);
        System.out.println(divider);
    }

    void populateGhostList() {
        this.setGhosts(XMLParser.populateGhosts(XMLParser.readGhosts()));
    }

    void printGhosts() {
        for (Ghost ghost : ghosts) {
            System.out.println(ghost.toString());
        }
    }

    Ghost getRandomGhost() {
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
        } catch (NullPointerException e) {
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

    void printEverythingInWorld() {
        for (Room room : world.gameMap) {
            System.out.println(room.toString());
        }
    }

    void printGhostsDesc() {
        ghosts.forEach(ghost -> System.out.println(ConsoleColors.BLACK_BACKGROUND_BRIGHT + ConsoleColors.GREEN_BRIGHT + ghost.toString() + ConsoleColors.RESET + "\n"));
    }
    // Getters / Setters


    Player getPlayer() {
        return player;
    }

    void setPlayer(Player player) {
        this.player = player;
    }

    List<Ghost> getGhosts() {
        return ghosts;
    }

    void setGhosts(List<Ghost> ghosts) {
        this.ghosts = ghosts;
    }

    Ghost getCurrentGhost() {
        return currentGhost;
    }

    void setCurrentGhost(Ghost ghost) {
        this.currentGhost = ghost;
    }

    World getWorld() {
        return world;
    }

    void setWorld(World world) {
        this.world = world;
    }

    private void getGhostBackstory() {
    }

    private boolean userAbleToExit() {
        boolean ableToExit = true;
        // Is player currently in lobby? Has user visited any other rooms? Is so size of roomsVisited would be greater than 1
        if (!world.getCurrentRoom().getRoomTitle().equals("Lobby")) {
            System.out.println("You can only exit from Lobby");
            return false;
        }
        if (player.getRoomsVisited().size() == 1) {
            System.out.println("You must visit more than one room to exit");
            return false;
        }
        return ableToExit;
    }

    private void resetWorld() {
        removeAllEvidenceFromWorld();
        setCurrentGhost(getRandomGhost());
        assignRandomEvidenceToMap();
    }

    private void removeAllEvidenceFromWorld() {
        for (Room room : world.gameMap) {
            if (!room.getRoomEvidence().isEmpty()) {
                room.setRoomEvidence("");
            }
        }
    }

    boolean checkWinnerTest() {
        // Testing purposes
        return checkIfHasAllEvidenceIsInJournal();
    }

    private boolean checkIfHasAllEvidenceIsInJournal() {
        boolean hasAllEvidence = true;
        // grab characteristics of currentGhost
        ArrayList<String> evidence = currentGhost.getEvidence();
        // grab contents of journal
        // make everything in journal lower case
        // grab list of last word of ghost evidence which should be the noun we are looking for
        // for each noun in list of nouns see if its in journal
        for (String e : evidence) {
            String nounToLookFor = e.substring(e.lastIndexOf(" ") + 1);
            if (!player.getJournal().toString().toLowerCase().contains(nounToLookFor.toLowerCase())) {
                hasAllEvidence = false;
                break;
            }
        }
        return hasAllEvidence;
    }

    private void narrate(String input) {
        int seconds = 1;
        int numChars = input.toCharArray().length;
        long sleepTime = (long) seconds * 1000 / numChars;
        System.out.print(ConsoleColors.RED);
        try {
            keyboardEffect.playSoundEffect();
            for (Character c : input.toCharArray()) {
                System.out.print(c);
                Thread.sleep(sleepTime);
            }
            keyboardEffect.stopSoundEffect();
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(ConsoleColors.RESET);
    }

    private void chrisIsCool() {
        String url_open = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
            mp.quitMusic();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void narrateRooms(String input) {
        int seconds = 1;
        int numChars = input.toCharArray().length;
        long sleepTime = (long) seconds * 4000 / numChars;
        System.out.print(ConsoleColors.RED_BRIGHT);
        try {
            paperFalling.playSoundEffect();
            for (Character c : input.toCharArray()) {
                System.out.print(c);
                Thread.sleep(sleepTime);
            }
            paperFalling.stopSoundEffect();
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(ConsoleColors.RESET);
    }

}
