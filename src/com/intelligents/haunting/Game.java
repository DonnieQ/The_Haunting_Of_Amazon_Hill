package com.intelligents.haunting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.intelligents.haunting.CombatEngine.runCombat;

public class Game implements java.io.Serializable {
    private final PrintFiles p;
    private final String resourcePath;
    private final ClassLoader cl;
    private World world;
    private List<Ghost> ghosts = new ArrayList<>();
    private List<MiniGhost> miniGhosts = new ArrayList<>();
    private final SaveGame SaveGame = new SaveGame();
    private Ghost currentGhost;
    private final Random r = new Random();
    private final String divider = "*******************************************************************************************";
    private Player player;
    private MusicPlayer mp;
    private MusicPlayer soundEffect;
    private MusicPlayer walkEffect;
    private MusicPlayer keyboardEffect;
    private MusicPlayer paperFalling;
    private final Scanner scanner = new Scanner(System.in);
    private int guessCounter = 0;
    boolean isGameRunning = true;
    private boolean isSound = true;


    public Game(String pathStartSounds, String pathStartResources, ClassLoader classLoader
            , PrintFiles printer) {
        //populates the main ghost list and sets a random ghost for the current game session
        p = printer;
        resourcePath = pathStartResources;
        cl = classLoader;
        world = new World(cl,resourcePath);
        setMusic(pathStartSounds);
        populateGhostList();
        populateMiniGhostList();
        setCurrentGhost(getRandomGhost());
        assignRandomEvidenceToMap();
        assignRandomMiniGhostToMap();

    }

    private void setMusic(String pathStart){
        mp = new MusicPlayer(pathStart + "Haunted Mansion.wav",cl);
        soundEffect = new MusicPlayer(pathStart + "page-flip-4.wav", cl);
        walkEffect = new MusicPlayer(pathStart + "footsteps-4.wav", cl);
        keyboardEffect = new MusicPlayer(pathStart + "fast-pace-typing.wav",cl );
        paperFalling = new MusicPlayer(pathStart + "paper flutter (2).wav",cl );
    }

    void start(boolean isGameLoaded) {
        // this isn't actually used to error check anywhere - change so that it checks a parser to actually check validity
        boolean isValidInput;


        String[] input;


        mp.startMusic();
        if (!isGameLoaded) {

            simpleOutputInlineSetting("\n" + ConsoleColors.GREEN_BOLD + "Thank you for choosing to play The Haunting of Amazon Hill. " +
                    "What would you like your name to be?\n" + ConsoleColors.RESET + ">>");

            input = scanner.nextLine().strip().split(" ");

            player = Player.getInstance();

            player.setName(input[0]);

            String formatted = String.format("%175s%n", ConsoleColors.CYAN_UNDERLINED + " --> If you're new to the game type help for assistance" + ConsoleColors.RESET);
            quickNarrateFormatted(formatted);

            formatted = String.format("%70s%n%n", ConsoleColors.WHITE_BOLD_BRIGHT + "Good luck to you, " + player.getName() + ConsoleColors.RESET);
            quickNarrateFormatted(formatted);

        }

        narrateRooms(world.getCurrentRoom().getDescription());

        //has access to entire Game object. tracking all changes
        SaveGame.setGame(this);


        while (isGameRunning) {
            isValidInput = true;
            int attempt = 0;

            String currentLoc = ConsoleColors.BLUE_BOLD + "Your location is " + world.getCurrentRoom().getRoomTitle() + ConsoleColors.RESET;
            String moveGuide = ConsoleColors.RESET + ConsoleColors.YELLOW + "To move type: Go North, Go East, Go South, or Go West" + ConsoleColors.RESET;


            String formatted = String.format("%45s %95s %n", currentLoc, moveGuide);
            quickNarrateFormatted(formatted);

            simpleOutputInlineSetting("\n>>");

            input = scanner.nextLine().strip().toLowerCase().split(" ");

            // Checks if current room is in roomsVisited List. If not adds currentRoom to roomsVisited
            processInput(isValidInput, input, attempt, currentLoc);

        }
        simpleOutputInlineSetting("Thank you for playing our game!!");
    }


    private void processInput(boolean isValidInput, String[] input, int attempt, String currentLoc) {
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
                    p.print(resourcePath, "Rules", cl);
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
                    narrateNoNewLine("Would you like to document anything in your journal? [Yes/No]\n");
                    writeEntryInJournal();
                    break;
                //Allows user to leave if more than one room has been input into RoomsVisted
                case "exit":
                    if (userAbleToExit()) {
                        // In order to win, user has to have correct evidence and guessed right ghost
                        if (!checkIfHasAllEvidenceIsInJournal()) {
                            narrateNoNewLine("It seems your journal does not have all of the evidence needed to determine the ghost." +
                                    " Would you like to GUESS the ghost anyway or go back INSIDE?\n>>");
                            String ans = "";
                            boolean validEntry = false;
                            while (!validEntry) {
                                ans = scanner.nextLine().strip().toLowerCase();
                                if (ans.contains("guess") || ans.contains("inside")) {
                                    validEntry = true;
                                } else {
                                    simpleOutputInlineSetting("Invalid input, please decide whether you want to GUESS or go back INSIDE.\n>>");
                                }
                            }
                            if (ans.contains("inside")) {
                                break;
                            }
                        }
                        String userGuess = getTypeOfGhostFromUser();
                        if (userGuess.equalsIgnoreCase(currentGhost.getType())) {
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
        } catch (ArrayIndexOutOfBoundsException | FileNotFoundException e) {
            narrateNoNewLine("Make sure to add a verb e.g. 'move', 'go', 'open', 'read' then a noun e.g. 'north', 'map', 'journal'.\n");
        } catch (IOException e) {
            e.printStackTrace();
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
                    simpleOutputInlineSetting("You hit a wall. Try again:\n>> ");
                    attemptCount++;
                    if (attemptCount >= 2) {
                        simpleOutputInlineSetting("\n");
                        openMap();
                        simpleOutputInlineSetting("Where would you like to go?\n>> ");
                    }
                    input = scanner.nextLine().strip().toLowerCase().split(" ");
                    //break;
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        if (world.getCurrentRoom().getRoomMiniGhost() != null) {
            narrateNoNewLine("You have run into a " + world.getCurrentRoom().getRoomMiniGhost().getName() +
                    ". What will you do? [Fight/Run]\n>>");
            input = scanner.nextLine().strip().toLowerCase().split(" ");
            narrateNoNewLine(runCombat(input, this, scanner) + "\n");
        }
    }

    private void openMap() throws IOException {
        switch (world.getCurrentRoom().getRoomTitle()) {
            case "Dining Room":
                p.print(resourcePath, "Map(DiningRoom)", cl);
                break;
            case "Balcony":
                p.print(resourcePath, "Map(Balcony)", cl);
                break;
            case "Attic":
                p.print(resourcePath, "Map(Attic)", cl);
                break;
            case "Dungeon":
                p.print(resourcePath, "Map(Dungeon)", cl);
                break;
            case "Furnace Room":
                p.print(resourcePath, "Map(FurnaceRoom)", cl);
                break;
            case "Garden Of Eden":
                p.print(resourcePath, "Map(GardenOfEden)", cl);
                break;
            case "Library":
                p.print(resourcePath, "Map(Library)", cl);
                break;
            case "Lobby":
                p.print(resourcePath, "Map(Lobby)", cl);
                break;
            case "Secret Tunnel":
                simpleOutputInlineSetting("You're in a super secret tunnel!!!");
                break;
        }
    }

    private String getTypeOfGhostFromUser() {
        narrateNoNewLine("You've collected all the evidence you could find.\n" +
                "Based on your expertise, make an informed decision on what type of " +
                "ghost is haunting Amazon Hill?\n" +
                "Here are all the possible ghosts:\n");
        ghosts.forEach(ghost -> simpleOutputInlineSetting(ConsoleColors.GREEN_BOLD_BRIGHT + ghost.getType() +
                ConsoleColors.RESET + "\n"));
        simpleOutputInlineSetting(ConsoleColors.RED + "Which Ghost do you think it is?\n" +
                ConsoleColors.RESET + ">>");
        String userGuessed = scanner.nextLine().strip();
        narrateNoNewLine("Good job gathering evidence, " + player.getName() + ".\nYou guessed: " + userGuessed + "\n");
        return userGuessed;
    }

    private void addEvidenceToJournal() {
        if (!world.getCurrentRoom().getRoomEvidence().isEmpty()) {
            String journalEntry = (world.getCurrentRoom().getRoomTitle() + ": " + world.getCurrentRoom().getRoomEvidence() + "(Automatically Logged)");
            player.setJournal(journalEntry);
        }
    }

    private void writeEntryInJournal() {
        simpleOutputInlineSetting(">>");
        String journalEntry = scanner.nextLine().strip();
        if (journalEntry.equals("no")) {
            narrateNoNewLine("Journal Closed.\n");
        } else if (journalEntry.equalsIgnoreCase("yes")) {
            simpleOutputInlineSetting("Your entry:\n>> ");
            journalEntry = scanner.nextLine().strip();
            player.setJournal(journalEntry);
        } else {
            narrateNoNewLine("Invalid Journal entry. Please look/show again to document again.\n");
        }
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

    public void quickNarrateFormatted(String input) {
        try {
            if (isSound) {
                keyboardEffect.playSoundEffect();
            }
            for (Character c : input.toCharArray()) {
                System.out.print(c);
                Thread.sleep(1);
            }
            keyboardEffect.stopSoundEffect();
        } catch (InterruptedException e) {
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
            simpleOutputInlineSetting("\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(ConsoleColors.RESET);
    }

    public void simpleOutputInlineSetting(String input) {
        System.out.print(input);
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