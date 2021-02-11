package com.intelligents.haunting;

import java.awt.*;
import java.io.IOException;
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
    private HauntingJFrame jFrame;
    SaveGame save = new SaveGame();
    private final transient PrintFiles p = new PrintFiles();
    private final MusicPlayer mp = new MusicPlayer("resources/Sounds/Haunted Mansion.wav");
    private final MusicPlayer soundEffect = new MusicPlayer("resources/Sounds/page-flip-4.wav");
    private final MusicPlayer walkEffect = new MusicPlayer("resources/Sounds/footsteps-4.wav");
    private final MusicPlayer keyboardEffect = new MusicPlayer("resources/Sounds/fast-pace-typing.wav");
    private final MusicPlayer paperFalling = new MusicPlayer("resources/Sounds/paper flutter (2).wav");
    private final Scanner scanner = new Scanner(System.in);
    private int guessCounter = 0;
    boolean isGameRunning = true;
    String moveGuide = "To move type: Go North, Go East, Go South, or Go West";
    String currentRoom = world.getCurrentRoom().getRoomTitle();
    private String currentLoc = "Your location is " + currentRoom;
    private boolean isSound = true;

    public Game(HauntingJFrame jFrame) throws IOException {
        //populates the main ghost list and sets a random ghost for the current game session
        populateGhostList();
        populateMiniGhostList();
        setCurrentGhost(getRandomGhost());
        assignRandomEvidenceToMap();
        assignRandomMiniGhostToMap();
        this.jFrame = jFrame;
    }

    public void intro(String[] gameType) {
        if (gameType[0].matches("1")) {
//            themeSong.stopSoundEffect();
            narrate("        ***********************************************************************************************************************\n" +
                    "        ring...ring...ring...click\n" +
                    "        *voicemail* \"Detective, we have a situation. There seems to be a disturbance over on Amazon Hill. The residents have\n" +
                    "        been experiencing some unexplained events taking place in their home. After speaking to the madam, this may be up your\n" +
                    "        alley. I know you may not be inclined due to the previous turning out to be a hoax... but I assure you, this one is\n" +
                    "        not sitting right. The details have been sent by courier, happy hunting...\n" +
                    "\n" +
                    "        I slowly walked towards the house, the leaves crunching under my feet. The broken gates were open, creaking in the wind\n" +
                    "        and felt wet and as cold as ice. On either side were demented gargoyles. It was a dark, cold night. The lightning lit up\n" +
                    "        the house and the thunder sent shock-waves down my spine. The house was dark and gloomy. It had narrow broken windows and\n" +
                    "        an open wooden porch, it looked abandoned but it shouldn't be, a light was on in the attic room. I started to hear faint\n" +
                    "        singing in the distance as my heart began to beat faster as I continued to walk past the bare trees towards the front door.\n" +
                    "        ***********************************************************************************************************************" +
                    "\n" + "Thank you for choosing to play The Haunting of Amazon Hill. " +
                    "What would you like your name to be? ");

//            start(false);
            jFrame.stopThemeSong();
            mp.startMusic();
            //If loaded game was selected then the saved file is loaded
        } else if (gameType[0].matches("4")) {
            try {
//                themeSong.stopSoundEffect();
                save.setGame(this);
                save.loadGame();

//                start(true);
                jFrame.stopThemeSong();
                mp.startMusic();
                SaveGame.setGame(this);
                narrate("Loading game!!!");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            narrate("Invalid selection , please enter 1.");
        }
    }

    void createPlayer(String[] nameInput) {
        updateCurrentRoom();

        player = Player.getInstance();

        player.setName(nameInput[0]);

        narrate(" --> If you're new to the game type help for assistance\n" +
                "Good luck to you, " + player.getName() + "\n" +
                "\n" + currentLoc + "\n" +
                moveGuide);

    }

    private void updateCurrentRoom() {
        currentRoom = world.getCurrentRoom().getRoomTitle();
        currentLoc = "Your location is " + currentRoom;
    }


    void processInput(boolean isValidInput, String[] input, int attempt) {
        updateCurrentRoom();
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
                                + world.getCurrentRoom().getRoomTitle() + "\n" +
                                divider);
                    } else {
                        addEvidenceToJournal();
                        narrate("You look and notice: " + world.getCurrentRoom().getRoomEvidence() + "\n" +
                                "Evidence logged into your journal.\n" +
                                divider);
                    }
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
        } catch (ArrayIndexOutOfBoundsException | IOException e) {
            narrate("Make sure to add a verb e.g. 'move', 'go', 'open', 'read' then a noun e.g. 'north', 'map', 'journal' ");
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

    public void changeRoom(boolean isValidInput, String[] input, int attemptCount) throws IOException {
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
                    narrate("You hit a wall. Try again: ");
                    attemptCount++;
                    if (attemptCount >= 2) {
                        openMap();
                        narrate("Where would you like to go? ");
                    }
                    input = jFrame.userResponse;
                    break;

            }

        }
        if (world.getCurrentRoom().getRoomMiniGhost() != null) {
            narrate("You have run into a " + world.getCurrentRoom().getRoomMiniGhost().getName() +
                    ". What will you do? [Fight/Run]");
            System.out.print(">>");
            input = jFrame.userResponse;
            narrate(runCombat(input, this, scanner));
        }
    }

    private void openMap() throws IOException {
        switch (world.getCurrentRoom().getRoomTitle()) {
            case "Dining Room":
                jFrame.showMap();
                break;
            case "Balcony":
                jFrame.showMap();
                break;
            case "Attic":
                jFrame.showMap();
                break;
            case "Dungeon":
                jFrame.showMap();
                break;
            case "Furnace Room":
                jFrame.showMap();
                break;
            case "Garden Of Eden":
                jFrame.showMap();
                break;
            case "Library":
                jFrame.showMap();
                break;
            case "Lobby":
                jFrame.showMap();
                break;
            case "Secret Tunnel":
                jFrame.showMap();
                break;
        }
    }

    private String getTypeOfGhostFromUser() {
        narrate("You've collected all the evidence you could find. " +
                "Based on your expertise, make an informed decision on what type of " +
                "ghost is haunting Amazon Hill.\n" +
                "Here are all the possible ghosts");
        ghosts.forEach(ghost -> System.out.println(ghost.getType()));
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
        String journalEntry = scanner.nextLine().strip();
        if (journalEntry.equals("no")) {
            narrate("Journal Closed.");
        } else if (journalEntry.equalsIgnoreCase("yes")) {
            narrate("Your entry: \n" +
                    ">>>");
            player.setJournal(journalEntry);
        } else {
            narrate("Invalid Journal entry. Please look/show again to document again.");
        }
    }

    private void printJournal() {
        String ghostEmoji = "\uD83D\uDC7B ";
        String houseEmoji = "\uD83C\uDFE0";
        String bookEmoji = "\uD83D\uDCD6";
        narrate(divider + "\n" +
                bookEmoji + " " + player + "\n" +
                ghostEmoji + "Possible Ghosts " + ghostEmoji +
                ghosts.toString() + "\n" + houseEmoji + " Rooms visited " + houseEmoji +
                player.getRoomsVisited() +
                divider);
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
            narrate("The data given is empty, cannot perform function");
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
            narrate("You can only exit from Lobby");
            return false;
        }
        if (player.getRoomsVisited().size() == 1) {
            narrate("You must visit more than one room to exit");
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
            narrate("Sorry, you've made too many incorrect guesses. GAME OVER.");
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
        jFrame.textDisplayGameWindow.setForeground(Color.RED);
//        try {
        if (isSound) {
            keyboardEffect.playSoundEffect();
        }
//            for (Character c : input.toCharArray()) {
//                System.out.print(c);
//                Thread.sleep(sleepTime);
//            }
            jFrame.setTextbox(input);
            keyboardEffect.stopSoundEffect();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        jFrame.textDisplayGameWindow.setForeground(Color.white);

    }

    public void narrateNoNewLine(String input) {
        int seconds = 1;
        int numChars = input.toCharArray().length;
        long sleepTime = (long) seconds * 1000 / numChars;
//        System.out.print(ConsoleColors.RED);
//        try {
            if (isSound) {
                keyboardEffect.playSoundEffect();
            }
//            for (Character c : input.toCharArray()) {
//                System.out.print(c);
//                Thread.sleep(sleepTime);
//            }
        jFrame.setTextbox(input);
            keyboardEffect.stopSoundEffect();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.print(ConsoleColors.RESET);
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
//        System.out.print(ConsoleColors.RED_BRIGHT);
//        try {
            if (isSound) {
                paperFalling.playSoundEffect();
            }
//            for (Character c : input.toCharArray()) {
//                System.out.print(c);
//                Thread.sleep(sleepTime);
//            }
        jFrame.setTextbox(input);
            paperFalling.stopSoundEffect();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.print(ConsoleColors.RESET);
    }
}

