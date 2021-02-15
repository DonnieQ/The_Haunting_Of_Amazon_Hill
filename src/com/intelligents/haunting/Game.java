package com.intelligents.haunting;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.intelligents.haunting.CombatEngine.runCombat;

public class Game implements java.io.Serializable {
    private final String resourcePath;
    private final ClassLoader cl;
    private World world;
    private List<Ghost> ghosts = new ArrayList<>();
    private List<MiniGhost> miniGhosts = new ArrayList<>();
    private final SaveGame SaveGame = new SaveGame();
    private Ghost currentGhost;
    private final Random r = new Random();
    private final String divider = "************************************************************************************************";
    private Player player;
    private HauntingJFrame jFrame;
    SaveGame save = new SaveGame();
    private final transient PrintFiles p = new PrintFiles();
    private MusicPlayer mp;
    private MusicPlayer soundEffect;
    private MusicPlayer walkEffect;
    private MusicPlayer keyboardEffect;
    private MusicPlayer paperFalling;
    private int guessCounter = 0;
    boolean isGameRunning = true;
    String moveGuide = "To move type: Go North, Go East, Go South, or Go West";
    String currentRoom;
    private String currentLoc;
    private boolean isSound = true;
    int attemptCount = 0;


    public Game(HauntingJFrame jFrame, String pathStartSounds, String pathStartResources, ClassLoader classLoader
            , PrintFiles printer) throws IOException {
        //populates the main ghost list and sets a random ghost for the current game session
        resourcePath = pathStartResources;
        cl = classLoader;
        world = new World(cl,resourcePath);
        player = Player.getInstance();
        currentRoom = world.getCurrentRoom().getRoomTitle();
        currentLoc = ConsoleColors.BLUE_BOLD + "Your location is " + currentRoom + ConsoleColors.RESET;
        setMusic(pathStartSounds);
        populateGhostList();
        populateMiniGhostList();
        setCurrentGhost(getRandomGhost());
        assignRandomEvidenceToMap();
        assignRandomMiniGhostToMap();
        this.jFrame = jFrame;
    }

    private void setMusic(String pathStart){
        mp = new MusicPlayer(pathStart + "Haunted Mansion.wav",cl);
        soundEffect = new MusicPlayer(pathStart + "page-flip-4.wav", cl);
        walkEffect = new MusicPlayer(pathStart + "footsteps-4.wav", cl);
        keyboardEffect = new MusicPlayer(pathStart + "fast-pace-typing.wav",cl );
        paperFalling = new MusicPlayer(pathStart + "paper flutter (2).wav",cl );
    }

    public void intro(String[] gameType) throws IOException {
        if (gameType[0].matches("1")) {
            jFrame.textDisplayGameWindow.setForeground(Color.green);
            quickNarrateFormatted(Files.readString(Paths.get("resources/settingTheScene"), StandardCharsets.UTF_8));

            simpleOutputInlineSetting("\n" + "Thank you for choosing to play The Haunting of Amazon Hill. " +
                    "What would you like your name to be?\n" + ">>");

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

        player.setName(nameInput[0]);

        jFrame.textDisplayGameWindow.setForeground(Color.cyan);
        String formatted = String.format("If you're new to the game type help for assistance.");
        quickNarrateFormatted(formatted);

        formatted = String.format("\nGood luck to you, " + player.getName() + "!");
        narrateNoNewLine(formatted);

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
                    } else if (input[1].equals("down")) {
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
                    jFrame.textDisplayGameWindow.setForeground(Color.PINK);
                    quickNarrateFormatted(Files.readString(Paths.get(resourcePath + "Rules"), StandardCharsets.UTF_8));
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
                    jFrame.textDisplayGameWindow.setForeground(Color.white);
                    quickNarrateFormatted("Would you like to document anything in your journal? [Yes/No]\n");
                    break;
                //Allows user to leave if more than one room has been input into RoomsVisted
                case "exit":
                    if (userAbleToExit()) {
                        // In order to win, user has to have correct evidence and guessed right ghost
                        if (!checkIfHasAllEvidenceIsInJournal()) {
                            jFrame.textDisplayGameWindow.setForeground(Color.green);
                            quickNarrateFormatted("It seems your journal does not have all of the evidence needed to determine the ghost." +
                                    " Would you like to GUESS the ghost anyway or go back INSIDE?\n>>");
                        } else {
                            jFrame.textDisplayGameWindow.setForeground(Color.green);
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
                    break;
//                case "fight":
//                case "run":
//                    narrateNoNewLine(runCombat(input, this) + "\n");
//                    break;

            }
        } catch (ArrayIndexOutOfBoundsException | FileNotFoundException e) {
            narrateNoNewLine("Make sure to add a verb e.g. 'move', 'go', 'open', 'read' then a noun e.g. 'north', 'map', 'journal'.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void guessOrGoBackInside(String ans) {
        if (ans.contains("guess")) {
            jFrame.textDisplayGameWindow.setForeground(Color.green);
            quickNarrateFormatted("You've collected all the evidence you could find.\n" +
                    "Based on your expertise, make an informed decision on what type of " +
                    "ghost is haunting Amazon Hill?\n" +
                    "Here are all the possible ghosts:\n");
            ghosts.forEach(ghost -> simpleOutputInlineSetting(ghost.getType() + "\n"));
            simpleOutputInlineSetting("Which Ghost do you think it is?\n" +
                   ">>");
        } else if (ans.contains("inside")) {
            jFrame.textDisplayGameWindow.setForeground(Color.white);
            quickNarrateFormatted("You are back inside");
        } else {
            jFrame.textDisplayGameWindow.setForeground(Color.white);
            quickNarrateFormatted("Invalid input, please decide whether you want to GUESS or go back INSIDE.\n>>");
        }
    }

    void userGuess(String ans) {
        jFrame.textDisplayGameWindow.setForeground(Color.white);
        quickNarrateFormatted("Good job gathering evidence, " + player.getName() + ".\nYou guessed: " + ans + "\n");
        if (ans.equalsIgnoreCase(currentGhost.getType())) {
            jFrame.textDisplayGameWindow.setForeground(Color.red);
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

    public void changeRoom(boolean isValidInput, String[] input, int attemptCount) throws IOException {
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
                    jFrame.textDisplayGameWindow.setForeground(Color.red);
                    narrateRooms(world.getCurrentRoom().getDescription());
                    break;
                } else {
                    jFrame.textDisplayGameWindow.setForeground(Color.red);
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
            String fightChoice = JOptionPane.showInputDialog("You have run into a " + world.getCurrentRoom().getRoomMiniGhost().getName() +
                    ". What will you do? [Fight/Run]\n>>").strip().toLowerCase();
            switch (fightChoice) {
                case "fight":
                case "run":
                    simpleOutputInlineSetting(runCombat(fightChoice, this));
                    break;
            }
        }
    }

    private void openMap() throws IOException {
        jFrame.showMap();
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
            jFrame.textDisplayGameWindow.setForeground(Color.white);
            quickNarrateFormatted("Your entry:\n>> ");
        } else {
            narrateNoNewLine("Invalid Journal entry. Please look/show again to document again.\n");
        }
    }

    void inputEntryInJournal(String journalEntry) {
        player.setJournal(journalEntry);
        jFrame.textDisplayGameWindow.setForeground(Color.white);
        quickNarrateFormatted("Entry Saved!");
    }

    private void printJournal() {
        jFrame.textDisplayGameWindow.setForeground(Color.yellow);
        quickNarrateFormatted(divider + "\n");
        narrateNoNewLine(  player + "\n");
        String formatted = String.format("Possible Ghosts ");
        simpleOutputInlineSetting(formatted);
        narrateNoNewLine(ghosts.toString() + "\n");
        formatted = String.format(" Rooms visited ");
        simpleOutputInlineSetting(formatted);
        narrateNoNewLine(player.getRoomsVisited() + "\n");
        simpleOutputInlineSetting(divider + "\n");
    }

    public void openNewWindowJournalWithUpdatedInfo() {

        jFrame.textDisplayGameWindow.setForeground(Color.yellow);
        jFrame.textDisplayJournal.setText(
                        player + "\n" +
                        "Possible Ghosts " +
                        ghosts.toString() + "\n" +
                        " Rooms visited " +
                        player.getRoomsVisited() + "\n"
        );
    }

    void populateGhostList() {
        this.setGhosts(XMLParser.populateGhosts(XMLParser.readXML(resourcePath + "Ghosts",cl ), "ghost"));
    }

    void populateMiniGhostList() {
        this.setMiniGhosts(XMLParser.populateMiniGhosts(XMLParser.readXML(resourcePath + "Ghosts",cl ), "minighost"));
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
            String formatted = String.format("Sorry, you've made too many incorrect guesses. GAME OVER.");
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

        if (isSound) {
            keyboardEffect.playSoundEffect();
        }
        jFrame.appendToTextBox(input);
        keyboardEffect.stopSoundEffect();
    }

    // Add narration to the GUI by removing all prior text added
    public void quickNarrateFormatted(String input) {
        if (isSound) {
            keyboardEffect.playSoundEffect();
        }
        jFrame.setTextBox(input);
        keyboardEffect.stopSoundEffect();
    }

    // Removes all prior text presented in GUI and displays new room narration
    private void narrateRooms(String input) {

        if (isSound) {
            paperFalling.playSoundEffect();
        }
        jFrame.setTextBox(input);
        paperFalling.stopSoundEffect();
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