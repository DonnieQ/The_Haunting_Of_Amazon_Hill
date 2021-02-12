package com.intelligents.haunting;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.*;

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
    private int guessCounter = 0;
    boolean isGameRunning = true;
    String moveGuide = "To move type: Go North, Go East, Go South, or Go West";
    String currentRoom = world.getCurrentRoom().getRoomTitle();
    private String currentLoc = ConsoleColors.BLUE_BOLD + "Your location is " + currentRoom + ConsoleColors.RESET;
    private boolean isSound = true;
    int attemptCount = 0;

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
            quickNarrateFormatted("        ***********************************************************************************************************************\n" +
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
                    "        ***********************************************************************************************************************");

            simpleOutputInlineSetting("\n" + ConsoleColors.GREEN_BOLD + "Thank you for choosing to play The Haunting of Amazon Hill. " +
                    "What would you like your name to be?\n" + ConsoleColors.RESET + ">>");

            jFrame.stopThemeSong();
            mp.startMusic();
            //If loaded game was selected then the saved file is loaded
        } else if (gameType[0].matches("4")) {
            try {
                save.setGame(this);
                save.loadGame();

                jFrame.stopThemeSong();
                mp.startMusic();
                SaveGame.setGame(this);
                narrateNoNewLine("Loading game!!!");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            simpleOutputInlineSetting("Invalid selection , please enter 1.");
        }
    }

    void createPlayer(String[] nameInput) {
        updateCurrentRoom();

        player = Player.getInstance();

        player.setName(nameInput[0]);

        String formatted = String.format("%175s%n", ConsoleColors.CYAN_UNDERLINED + " --> If you're new to the game type help for assistance" + ConsoleColors.RESET);
        quickNarrateFormatted(formatted);

        formatted = String.format("%70s%n%n", ConsoleColors.WHITE_BOLD_BRIGHT + "Good luck to you, " + player.getName() + ConsoleColors.RESET);
        narrateNoNewLine(formatted);

    }

    private void updateCurrentRoom() {
        currentRoom = world.getCurrentRoom().getRoomTitle();
        currentLoc = ConsoleColors.BLUE_BOLD + "Your location is " + currentRoom + ConsoleColors.RESET;
    }


    void processInput(boolean isValidInput, String[] input, int attempt) {
        updateCurrentRoom();
        checkIfRoomVisited();
        try {
            switch (input[0]) {
                /* Case for original developer easter egg, disabled for security. Uncomment to enable
                and also related function at the bottom of Game.java
                case "chris":
                    chrisIsCool();
                    break;
                 */
                //Allows for volume to be increased or decreased
                case "volume":
                    if (input[1].equals("up")) {
                        mp.setVolume(5.0f);
                    } else if (input[1].equals("down")){
                        mp.setVolume(-5.0f);
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
                case "?":
                case "help":
                    p.print("resources", "Rules");
                    break;
                case "open":
                    openMap();
                    break;
                //Displays room contents/evidence
                case "look":
                case "view":
                case "show":
                    narrateNoNewLine(divider + "\n");

                    String formatted = String.format("%46s%n", currentLoc);
                    simpleOutputInlineSetting(formatted);

                    if (world.getCurrentRoom().getRoomEvidence().isEmpty()) {
                        narrateNoNewLine("Currently there are no items in "
                                + world.getCurrentRoom().getRoomTitle() + "\n\n");
                    } else {
                        addEvidenceToJournal();
                        narrateNoNewLine("You look and notice: " + world.getCurrentRoom().getRoomEvidence() + "\n\n");
                        narrateNoNewLine("Evidence logged into your journal.\n");
                    }
                    narrateNoNewLine(divider + "\n");
                    break;
                case "write":
                    quickNarrateFormatted("Would you like to document anything in your journal? [Yes/No]\n");
                    break;
                //Allows user to leave if more than one room has been input into RoomsVisted
                case "exit":
                    if (userAbleToExit()) {
                        // In order to win, user has to have correct evidence and guessed right ghost
                        if (!checkIfHasAllEvidenceIsInJournal()) {
                            quickNarrateFormatted("It seems your journal does not have all of the evidence needed to determine the ghost." +
                                    " Would you like to GUESS the ghost anyway or go back INSIDE?\n>>");
                        } else {
                            quickNarrateFormatted("It seems like you could be ready to determine the ghost." +
                                    " Would you like to GUESS the ghost or go back INSIDE to continue exploring?\n>>");
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
            narrateNoNewLine("Make sure to add a verb e.g. 'move', 'go', 'open', 'read' then a noun e.g. 'north', 'map', 'journal'.\n");
        }
    }

    void guessOrGoBackInside(String ans) {
        if (ans.contains("guess")) {
            quickNarrateFormatted("You've collected all the evidence you could find.\n" +
                    "Based on your expertise, make an informed decision on what type of " +
                    "ghost is haunting Amazon Hill?\n" +
                    "Here are all the possible ghosts:\n");
            ghosts.forEach(ghost -> simpleOutputInlineSetting(ConsoleColors.GREEN_BOLD_BRIGHT + ghost.getType() +
                    ConsoleColors.RESET + "\n"));
            simpleOutputInlineSetting(ConsoleColors.RED + "Which Ghost do you think it is?\n" +
                    ConsoleColors.RESET + ">>");
        } else if (ans.contains("inside")) {
            quickNarrateFormatted("You are back inside");
        } else {
            quickNarrateFormatted("Invalid input, please decide whether you want to GUESS or go back INSIDE.\n>>");
        }
    }

    void userGuess(String ans) {
        quickNarrateFormatted("Good job gathering evidence, " + player.getName() + ".\nYou guessed: " + ans + "\n");
        if (ans.equalsIgnoreCase(currentGhost.getType())) {
            narrateNoNewLine("You won!\n");
            narrateNoNewLine(getGhostBackstory() + "\n");
            isGameRunning = false;
        } else {
            if (guessCounter < 1) {
                narrateNoNewLine("Unfortunately, the ghost you determined was incorrect. The correct ghost was \n"
                        + currentGhost.toString() + "\nYou have been loaded into a new world. Good luck trying again.\n");
                resetWorld();
            } else {
                resetWorld();
            }
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


    public String normalizeText(String input) {
        List<String> northOptions = Arrays.asList("north", "up");
        List<String> southOptions = Arrays.asList("south", "down");
        List<String> eastOptions = Arrays.asList("east", "right");
        List<String> westOptions = Arrays.asList("west", "left");
        if (northOptions.contains(input.toLowerCase())) {
            return "north";
        }
        if (southOptions.contains(input.toLowerCase())) {
            return "south";
        }
        if (eastOptions.contains(input.toLowerCase())) {
            return "east";
        }
        if (westOptions.contains(input.toLowerCase())) {
            return "west";
        }
        return "";
    }

    public void changeRoom(boolean isValidInput, String[] input, int attemptCount) {
        while (isValidInput) {
            String normalize = normalizeText(input[1]);
            try {
                if (world.getCurrentRoom().roomExits.containsKey(normalize)) {
                    player.setMostRecentExit(normalize);
                    world.setCurrentRoom(world.getCurrentRoom().roomExits.get(normalize));
                    isValidInput = false;
                    if (isSound) {
                        walkEffect.playSoundEffect();
                    }
                    Thread.sleep(1800);
                    narrateRooms(world.getCurrentRoom().getDescription());
                    break;
                } else {
                    quickNarrateFormatted("You hit a wall. Try again:\n>> ");
                    attemptCount++;
                    if (attemptCount >= 2) {
                        simpleOutputInlineSetting("\n");
                        openMap();
                        simpleOutputInlineSetting("Where would you like to go?\n>> ");
                    }
                    break;
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        if (world.getCurrentRoom().getRoomMiniGhost() != null) {
            narrateNoNewLine("You have run into a " + world.getCurrentRoom().getRoomMiniGhost().getName() +
                    ". What will you do? [Fight/Run]\n>>");
//            input = scanner.nextLine().strip().toLowerCase().split(" ");
//            narrateNoNewLine(runCombat(input, this, scanner) + "\n");
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



    private void addEvidenceToJournal() {
        if (!world.getCurrentRoom().getRoomEvidence().isEmpty()) {
            String journalEntry = (world.getCurrentRoom().getRoomTitle() + ": " + world.getCurrentRoom().getRoomEvidence() + "(Automatically Logged)");
            player.setJournal(journalEntry);
        }
    }


     void writeEntryInJournal(String journalEntry) {
        if (journalEntry.equals("no")) {
            narrateNoNewLine("Journal Closed.\n");
        } else if (journalEntry.equalsIgnoreCase("yes")) {
            quickNarrateFormatted("Your entry:\n>> ");
        } else {
            narrateNoNewLine("Invalid Journal entry. Please look/show again to document again.\n");
        }
    }

    void inputEntryInJournal(String journalEntry) {
        player.setJournal(journalEntry);
        quickNarrateFormatted("Entry Saved!");
    }

    private void printJournal() {
        String ghostEmoji = "\uD83D\uDC7B ";
        String houseEmoji = "\uD83C\uDFE0";
        String bookEmoji = "\uD83D\uDCD6";
        simpleOutputInlineSetting(ConsoleColors.RED + divider + ConsoleColors.RESET + "\n");
        narrateNoNewLine(ConsoleColors.BLACK_BACKGROUND + bookEmoji + " " + player + ConsoleColors.RESET + "\n");
        String formatted = String.format("%45s%n%n", ConsoleColors.BLACK_BACKGROUND + ghostEmoji + "Possible Ghosts " + ghostEmoji + ConsoleColors.RESET);
        simpleOutputInlineSetting(formatted);
        narrateNoNewLine(ConsoleColors.GREEN_BOLD + ghosts.toString() + ConsoleColors.RESET + "\n");
        formatted = String.format("%43s%n%n", ConsoleColors.BLACK_BACKGROUND + houseEmoji + " Rooms visited " + houseEmoji + ConsoleColors.RESET);
        simpleOutputInlineSetting(formatted);
        narrateNoNewLine(ConsoleColors.BLUE_BOLD + player.getRoomsVisited() + ConsoleColors.RESET + "\n");
        simpleOutputInlineSetting(ConsoleColors.RED + divider + ConsoleColors.RESET + "\n");
    }

    public void openNewWindowJournalWithUpdatedInfo() {
        String ghostEmoji = "\uD83D\uDC7B ";
        String houseEmoji = "\uD83C\uDFE0";
        String bookEmoji = "\uD83D\uDCD6";
        jFrame.textDisplayJournal.setText(
                bookEmoji + " " + player + "\n" +
                        ghostEmoji + "Possible Ghosts " + ghostEmoji +
                        ghosts.toString() + "\n" +
                        houseEmoji + " Rooms visited " + houseEmoji +
                        player.getRoomsVisited() + ConsoleColors.RESET + "\n"
        );
    }

    void populateGhostList() {
        this.setGhosts(XMLParser.populateGhosts(XMLParser.readXML("Ghosts"), "ghost"));
    }

    void populateMiniGhostList() {
        this.setMiniGhosts(XMLParser.populateMiniGhosts(XMLParser.readXML("Ghosts"), "minighost"));
    }

    void printGhosts() {
        for (Ghost ghost : ghosts) {
            narrateNoNewLine(ghost.toString() + "\n");
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
                    if (x.getRoomEvidence().equals("")) {
                        x.setRoomEvidence(currentGhost.getEvidence().get(i));
                        addedEvidence = true;
                    }
                }

            }
        } catch (NullPointerException e) {
            simpleOutputInlineSetting("The data given is empty, cannot perform function");
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
                    if (x.getRoomMiniGhost() == (null)) {
                        x.setRoomMiniGhost(miniGhosts.get(i));
                        addedMiniGhost = true;
                    }
                }

            }
        } catch (NullPointerException e) {
            simpleOutputInlineSetting("There is no minighost to add to the room.\n");
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
            narrateNoNewLine(room.toString() + "\n");
        }
    }

    void printGhostsDesc() {
        ghosts.forEach(ghost -> narrateNoNewLine(ConsoleColors.BLACK_BACKGROUND_BRIGHT + ConsoleColors.GREEN_BRIGHT + ghost.toString() + ConsoleColors.RESET + "\n\n"));
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
            simpleOutputInlineSetting("You can only exit from Lobby.\n");
            return false;
        }
        if (player.getRoomsVisited().size() == 1) {
            simpleOutputInlineSetting("You must visit more than one room to exit.\n");
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
            String formatted = String.format("%95s%n%n", ConsoleColors.YELLOW_BOLD + "Sorry, you've made too many incorrect guesses. GAME OVER." + ConsoleColors.RESET);
            simpleOutputInlineSetting(formatted);
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
    /* OLD narrate function - replaced all references with \n new lines as needed, and removed the newline extra call
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
            simpleOutputInlineSetting("\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(ConsoleColors.RESET);
    }
     */

    // Used to add narration by appending to the GUI without removing any currently displayed text
    public void narrateNoNewLine(String input) {
        int seconds = 1;
        int numChars = input.toCharArray().length;
        long sleepTime = (long) seconds * 1000 / numChars;
        System.out.print(ConsoleColors.RED);
//        try {
            if (isSound) {
                keyboardEffect.playSoundEffect();
            }
//            for (Character c : input.toCharArray()) {
//                System.out.print(c);
//                Thread.sleep(sleepTime);
//            }
        jFrame.appendToTextBox(input);
            keyboardEffect.stopSoundEffect();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.print(ConsoleColors.RESET);
    }

    // Add narration to the GUI by removing all prior text added
    public void quickNarrateFormatted(String input) {
//        try {
            if (isSound) {
                keyboardEffect.playSoundEffect();
            }
//            for (Character c : input.toCharArray()) {
//                System.out.print(c);
//                Thread.sleep(1);
//            }
        jFrame.setTextBox(input);
            keyboardEffect.stopSoundEffect();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    // Removes all prior text presented in GUI and displays new room narration
    private void narrateRooms(String input) {
        int seconds = 1;
        int numChars = input.toCharArray().length;
        long sleepTime = (long) seconds * 4000 / numChars;
        System.out.print(ConsoleColors.RED_BRIGHT);
//        try {
            if (isSound) {
                paperFalling.playSoundEffect();
            }
//            for (Character c : input.toCharArray()) {
//                System.out.print(c);
//                Thread.sleep(sleepTime);
//            }
        jFrame.setTextBox(input);
            paperFalling.stopSoundEffect();
//            simpleOutputInlineSetting("\n");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.print(ConsoleColors.RESET);
    }

    // Appends to GUI without altering prior added text
    public void simpleOutputInlineSetting(String input) {
        jFrame.appendToTextBox(input);
    }

    /* Possible use for this function to feed in colors if inline colors don't work in jframe
    public void simpleOutputWithColor(String input, ConsoleColors color){
        System.out.print(color);
        System.out.print(input);
        System.out.print(ConsoleColors.RESET);
    }
    */

    /*Disabling original developer easter egg for security, but leaving it in the code.
    private void chrisIsCool() {
        String url_open = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
            mp.quitMusic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}