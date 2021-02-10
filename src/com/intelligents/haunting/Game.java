package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static com.intelligents.haunting.CombatEngine.runCombat;

public class Game implements java.io.Serializable {
    private World world = new World();
    private List<Ghost> ghosts = new ArrayList<>();
    private List<MiniGhost> miniGhosts = new ArrayList<>();
    private final SaveGame SaveGame = new SaveGame();
    private Ghost currentGhost;
    private final Random r = new Random();
    private final String divider = "*******************************************************************************************";
    private Player player;
    private final transient PrintFiles p = new PrintFiles();
    private final MusicPlayer mp = new MusicPlayer("resources/Sounds/Haunted Mansion.wav");
    private final MusicPlayer soundEffect = new MusicPlayer("resources/Sounds/page-flip-4.wav");
    private final MusicPlayer walkEffect = new MusicPlayer("resources/Sounds/footsteps-4.wav");
    private final MusicPlayer keyboardEffect = new MusicPlayer("resources/Sounds/fast-pace-typing.wav");
    private final MusicPlayer paperFalling = new MusicPlayer("resources/Sounds/paper flutter (2).wav");
    private final Scanner scanner = new Scanner(System.in);
    private int guessCounter = 0;
    boolean isGameRunning = true;
    private boolean isSound = true;

    public Game() {
        //populates the main ghost list and sets a random ghost for the current game session
        populateGhostList();
        populateMiniGhostList();
        setCurrentGhost(getRandomGhost());
        assignRandomEvidenceToMap();
        assignRandomMiniGhostToMap();
    }

    void start(boolean isGameLoaded) {
        // this isn't actually used to error check anywhere - change so that it checks a parser to actually check validity
        boolean isValidInput;


        String[] input;


        mp.startMusic();
        if (!isGameLoaded) {


            System.out.println("\n" + ConsoleColors.GREEN_BOLD + "Thank you for choosing to play The Haunting of Amazon Hill. " +
                    "What would you like your name to be? " + ConsoleColors.RESET);
            System.out.print(">>>");

            input = scanner.nextLine().strip().split(" ");

            player = Player.getInstance();

            player.setName(input[0]);

            System.out.printf("%175s%n", ConsoleColors.CYAN_UNDERLINED + " --> If you're new to the game type help for assistance" + ConsoleColors.RESET);


            System.out.printf("%70s%n%n", ConsoleColors.WHITE_BOLD_BRIGHT + "Good luck to you, " + player.getName() + ConsoleColors.RESET);

        }

        narrateRooms(world.getCurrentRoom().getDescription());

        //has access to entire Game object. tracking all changes
        SaveGame.setGame(this);


        while (isGameRunning) {
            isValidInput = true;
            int attempt = 0;

            String currentLoc = ConsoleColors.BLUE_BOLD + "Your location is " + world.getCurrentRoom().getRoomTitle() + ConsoleColors.RESET;
            String moveGuide = ConsoleColors.RESET + ConsoleColors.YELLOW + "To move type: Go North, Go East, Go South, or Go West" + ConsoleColors.RESET;

            System.out.printf("%45s %95s %n", currentLoc, moveGuide);

            System.out.println();

            System.out.print(">>>");

            input = scanner.nextLine().strip().toLowerCase().split(" ");


            // Checks if current room is in roomsVisited List. If not adds currentRoom to roomsVisited
            processInput(isValidInput, input, attempt, currentLoc);

        }
        System.out.println("Thank you for playing our game!!");
    }


    private void processInput(boolean isValidInput, String[] input, int attempt, String currentLoc) {
        checkIfRoomVisited();
        try {
            switch (input[0]) {
                case "chris":
                    chrisIsCool();
                    break;
                //Allows for volume to be increased or decreased
                case "volume":
                    if (input[1].equals("up")) {
                        mp.setVolume(5.0f);
                    } else {
                        mp.setVolume(-15.0f);
                    }
                    break;
                //Prints journal and plays page turning sound effect
                case "read":
                    printJournal();
                    if (isSound) {
                        soundEffect.playSoundEffect();
                    }
                    break;
                //Creates a save file that can be loaded
                case "save":
                    SaveGame.save();
                    break;
                //Reads the loaded usr.save file
                case "load":
                    SaveGame.loadGame();
                    break;
                //
                case "help":
                    p.print("resources", "Rules");
                    break;
                case "open":
                    openMap();
                    break;
                //Displays room contents/evidence
                case "look":
                case "show":
                    System.out.println(divider);
                    System.out.printf("%46s%n", currentLoc);

                    if (world.getCurrentRoom().getRoomEvidence().isEmpty()) {
                        narrate("Currently there is no evidence in the "
                                + world.getCurrentRoom().getRoomTitle() + "\n");
                    } else {
                        addEvidenceToJournal();
                        narrate("You look and notice: " + world.getCurrentRoom().getRoomEvidence() + "\n");
                        narrate("Evidence logged into your journal.");
                    }
                    System.out.println(divider);
                    break;
                case "write":
                    narrate("Would you like to document anything in your journal? [Yes/No]");
                    writeEntryInJournal();
                    break;
                //Allows user to leave if more than one room has been input into RoomsVisted
                case "exit":
                    if (userAbleToExit()) {
                        // In order to win, user has to have correct evidence and guessed right ghost
                        if (!checkIfHasAllEvidenceIsInJournal()) {
                            narrate("It seems your journal does not have all of the evidence needed to determine the ghost." +
                                    " Would you like to GUESS the ghost anyway or go back INSIDE?");
                            String ans = "";
                            boolean validEntry = false;
                            while (!validEntry) {
                                ans = scanner.nextLine().strip().toLowerCase();
                                if (ans.contains("guess") || ans.contains("inside")) {
                                    validEntry = true;
                                } else {
                                    narrate("Invalid input, please decide whether you want to GUESS or go back INSIDE.");
                                }
                            }
                            if (ans.contains("inside")) {
                                break;
                            }
                        }
                        String userGuess = getTypeOfGhostFromUser();
                        if (userGuess.equalsIgnoreCase(currentGhost.getType())) {
                            narrate("You won");
                            narrate(getGhostBackstory());
                            isGameRunning = false;
                        } else {
                            if (guessCounter < 1) {
                                narrate("Unfortunately, the ghost you determined was incorrect. The correct ghost was \n"
                                        + currentGhost.toString() + "You have been loaded into a new world. Good luck trying again.\n");
                                resetWorld();
                            } else {
                                resetWorld();
                            }
                        }
                    }
                    break;
                case "quit":
                case "q":
                    mp.quitMusic();
                    isGameRunning = false;
                    break;
                case "pause":
                    mp.pauseMusic();
                    break;
                case "stop":
                    stopSound();
                    break;
                case "play":
                    mp.startMusic();
                    break;
                case "move":
                case "go":

                    changeRoom(isValidInput, input, attempt);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Make sure to add a verb e.g. 'move', 'go', 'open', 'read' then a noun e.g. 'north', 'map', 'journal' ");
        }
    }

    private void stopSound() {
        mp.pauseMusic();
        soundEffect.stopSoundEffect();
        walkEffect.stopSoundEffect();
        keyboardEffect.stopSoundEffect();
        paperFalling.stopSoundEffect();
        isSound = false;
    }

    public void changeRoom(boolean isValidInput, String[] input, int attemptCount) {
        while (isValidInput) {
            switch (input[1]) {
                case "north":
                case "east":
                case "south":
                case "west":
                    try {
                        if (world.getCurrentRoom().roomExits.containsKey(input[1])) {
                            player.setMostRecentExit(input[1]);
                            world.setCurrentRoom(world.getCurrentRoom().roomExits.get(input[1]));
                            isValidInput = false;
                            if (isSound) {
                                walkEffect.playSoundEffect();
                            }
                            Thread.sleep(1800);
                            narrateRooms(world.getCurrentRoom().getDescription());
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                default:
                    System.out.println("You hit a wall. Try again: ");
                    System.out.print(">>>");
                    attemptCount++;
                    if (attemptCount >= 2) {
                        System.out.println();
                        openMap();
                        System.out.println("Where would you like to go? ");
                        System.out.print(">>>");
                    }
                    input = scanner.nextLine().strip().toLowerCase().split(" ");
                    break;

            }

        }
        if (world.getCurrentRoom().getRoomMiniGhost() != null) {
            narrate("You have run into a " + world.getCurrentRoom().getRoomMiniGhost().getName() +
                    ". What will you do? [Fight/Run]");
            System.out.print(">>");
            input = scanner.nextLine().strip().toLowerCase().split(" ");
            narrate(runCombat(input, this, scanner));
        }
    }

    private void openMap() {
        switch (world.getCurrentRoom().getRoomTitle()) {
            case "Dining Room":
                p.print("resources", "Map(DiningRoom)");
                break;
            case "Balcony":
                p.print("resources", "Map(Balcony)");
                break;
            case "Attic":
                p.print("resources", "Map(Attic)");
                break;
            case "Dungeon":
                p.print("resources", "Map(Dungeon)");
                break;
            case "Furnace Room":
                p.print("resources", "Map(FurnaceRoom)");
                break;
            case "Garden Of Eden":
                p.print("resources", "Map(GardenOfEden)");
                break;
            case "Library":
                p.print("resources", "Map(Library)");
                break;
            case "Lobby":
                p.print("resources", "Map(Lobby)");
                break;
            case "Secret Tunnel":
                System.out.println("You're in a super secret tunnel!!! ");
                break;
        }
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

    private void addEvidenceToJournal() {
        if(!world.getCurrentRoom().getRoomEvidence().isEmpty()) {
            String journalEntry = (world.getCurrentRoom().getRoomTitle() + ": " + world.getCurrentRoom().getRoomEvidence() + "(Automatically Logged)");
            player.setJournal(journalEntry);
        }
    }

    private void writeEntryInJournal() {
        System.out.print(">>>");
        String journalEntry = scanner.nextLine().strip();
        if (journalEntry.equals("no")) {
            narrate("Journal Closed.");
        } else if (journalEntry.equalsIgnoreCase("yes")) {
            narrate("Your entry: ");
            System.out.print(">>>");
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
        this.setGhosts(XMLParser.populateGhosts(XMLParser.readXML("Ghosts"), "ghost"));
    }

    void populateMiniGhostList() {
        this.setMiniGhosts(XMLParser.populateMiniGhosts(XMLParser.readXML("Ghosts"), "minighost"));
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

    private void assignRandomMiniGhostToMap() {
        try {
            //for each minighost, get rooms from world.gamemap equivalent to the number of evidences.
            for (int i = 0; i < miniGhosts.size(); i++) {
                // Success condition
                boolean addedMiniGhost = false;

                // Loop while no success
                while (!addedMiniGhost) {
                    Room x = getRandomRoomFromWorld();
                    // System.out.println("random room chosen is " + x.getRoomTitle());
                    if (x.getRoomMiniGhost() == (null)) {
                        x.setRoomMiniGhost(miniGhosts.get(i));
                        // System.out.println("added " + currentGhost.getEvidence().get(i) + " to " + x.getRoomTitle());
                        addedMiniGhost = true;
                    }
                }

            }
        } catch (NullPointerException e) {
            System.out.println("There is no minighost to add to the room.");
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

    List<MiniGhost> getMiniGhosts() {
        return miniGhosts;
    }

    void setGhosts(List<Ghost> ghosts) {
        this.ghosts = ghosts;
    }

    void setMiniGhosts(List<MiniGhost> miniGhosts) {
        this.miniGhosts = miniGhosts;
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

    private String getGhostBackstory() {
        return currentGhost.getBackstory();
    }

    private boolean userAbleToExit() {
        // Is player currently in lobby? Has user visited any other rooms? Is so size of roomsVisited would be greater than 1
        if (!world.getCurrentRoom().getRoomTitle().equals("Lobby")) {
            System.out.println("You can only exit from Lobby");
            return false;
        }
        if (player.getRoomsVisited().size() == 1) {
            System.out.println("You must visit more than one room to exit");
            return false;
        }
        return true;
    }

    private void resetWorld() {
        //resets world and adds a new ghost. guessCounter is incremented with a maximum allowable guesses
        // set at 2.
        guessCounter++;
        if (guessCounter <= 1) {
            removeAllEvidenceFromWorld();
            setCurrentGhost(getRandomGhost());
            assignRandomEvidenceToMap();
            player.resetPlayer();
        } else {
            System.out.printf("%95s%n%n", ConsoleColors.YELLOW_BOLD + "Sorry, you've made too many incorrect guesses. GAME OVER." + ConsoleColors.RESET);
            isGameRunning = false;
        }
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

    public void narrate(String input) {
        int seconds = 1;
        int numChars = input.toCharArray().length;
        long sleepTime = (long) seconds * 1000 / numChars;
        System.out.print(ConsoleColors.RED);
        try {
            if (isSound) {
                keyboardEffect.playSoundEffect();
            }
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

    public void narrateNoNewLine(String input) {
        int seconds = 1;
        int numChars = input.toCharArray().length;
        long sleepTime = (long) seconds * 1000 / numChars;
        System.out.print(ConsoleColors.RED);
        try {
            if (isSound) {
                keyboardEffect.playSoundEffect();
            }
            for (Character c : input.toCharArray()) {
                System.out.print(c);
                Thread.sleep(sleepTime);
            }
            keyboardEffect.stopSoundEffect();
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
            if (isSound) {
                paperFalling.playSoundEffect();
            }
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

