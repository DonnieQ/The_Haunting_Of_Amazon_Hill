import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Map {
    Ghost currentGhost;

    List<Room> gameMap = new ArrayList<>();

    Room lobby = new Room("Lobby");
    Room dungeon = new Room("Dungeon");
    Room diningRoom = new Room("Dining Room");
    Room balcony = new Room("Balcony");
    Room furnaceRoom = new Room("Furnace Room");

    Room currentRoom = lobby;

    public Map() {


        lobby.roomExits.put("east", dungeon);
        lobby.roomExits.put("west", diningRoom);
        lobby.roomExits.put("north", balcony);
        lobby.roomExits.put("south", furnaceRoom);
        lobby.setRoomItems(Ghost.SAMCA.getEvidence()[0]);

        gameMap.add(lobby);


        //  dungeon.roomExits.put("east", dungeon);
        dungeon.roomExits.put("west", lobby);
        //    dungeon.roomExits.put("north", balcony);
        //    dungeon.roomExits.put("south", furnaceRoom);
        dungeon.roomItems = Ghost.SAMCA.getEvidence()[1];

        gameMap.add(dungeon);


        //  diningRoom.roomExits.put("east", dungeon);
        diningRoom.roomExits.put("east", lobby);
        //   diningRoom.roomExits.put("north", balcony);
        //  diningRoom.roomExits.put("south", furnaceRoom);
        diningRoom.setRoomItems("");

        gameMap.add(diningRoom);


        //  balcony.roomExits.put("east", dungeon);
        //   balcony.roomExits.put("west", diningRoom);
        //   balcony.roomExits.put("north", balcony);
        balcony.roomExits.put("south", lobby);
        balcony.setRoomItems("");

        gameMap.add(balcony);


        // furnaceRoom.roomExits.put("east", dungeon);
        //    furnaceRoom.roomExits.put("west", diningRoom);
        furnaceRoom.roomExits.put("north", lobby);
        // furnaceRoom.roomExits.put("south", furnaceRoom);
        furnaceRoom.setRoomItems("");

        gameMap.add(furnaceRoom);

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

        System.out.println(player);
        System.out.println("Good luck to you, " + player.getName());
        System.out.println("---------------------------");


        while (isGameRunning) {
            isValidInput = true;

            System.out.println("To move type: Go North, Go East, Go South, or Go West");
            System.out.println("---------------------------");
            System.out.println("Your location is " + currentRoom.getRoomTitle());
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
                    System.out.println("Your location is " + currentRoom.getRoomTitle());
                    System.out.println(currentRoom.getRoomItems());
                    System.out.println("****************************");
                    break;
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
                                if (currentRoom.roomExits.containsKey(input[1])) {
                                    currentRoom = currentRoom.roomExits.get(input[1]);
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


}
