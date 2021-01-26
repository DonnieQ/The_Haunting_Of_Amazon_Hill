import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Game {
    Ghosts currentGhost;

    List<Rooms> gameMap = new ArrayList<>();

    Rooms lobby = new Rooms("Treasure Room");
    Rooms dungeon = new Rooms("Dungeon");
    Rooms diningRoom = new Rooms("Dining Room");
    Rooms balcony = new Rooms("Balcony");
    Rooms furnaceRoom = new Rooms("Furnace Room");

    Rooms currentRoom = dungeon;

    public Game() {

        lobby.roomExits.put("east", dungeon);
        lobby.roomExits.put("west", diningRoom);
        lobby.roomExits.put("north", balcony);
        lobby.roomExits.put("south", furnaceRoom);
        lobby.roomItems = Ghosts.SAMCA.getEvidence()[0];

        gameMap.add(lobby);


        //  dungeon.roomExits.put("east", dungeon);
        dungeon.roomExits.put("west", diningRoom);
        //    dungeon.roomExits.put("north", balcony);
        //    dungeon.roomExits.put("south", furnaceRoom);
        dungeon.roomItems = Ghosts.SAMCA.getEvidence()[1];

        gameMap.add(dungeon);


        //  diningRoom.roomExits.put("east", dungeon);
        diningRoom.roomExits.put("east", lobby);
        //   diningRoom.roomExits.put("north", balcony);
        //  diningRoom.roomExits.put("south", furnaceRoom);

        gameMap.add(diningRoom);


        //  balcony.roomExits.put("east", dungeon);
        //   balcony.roomExits.put("west", diningRoom);
        //   balcony.roomExits.put("north", balcony);
        balcony.roomExits.put("south", lobby);

        gameMap.add(balcony);


        // furnaceRoom.roomExits.put("east", dungeon);
        //    furnaceRoom.roomExits.put("west", diningRoom);
        furnaceRoom.roomExits.put("north", lobby);
        // furnaceRoom.roomExits.put("south", furnaceRoom);

        gameMap.add(furnaceRoom);

    }

    void start() {
        boolean isValid = true;

        Scanner scanner = new Scanner(System.in);
        String input = "";
        System.out.println("Please choose a direction to move: ");
        System.out.println(">>");

        input = scanner.nextLine().strip().toLowerCase();

        while (isValid) {

            switch (input) {

                case "north":
                case "east":
                case "south":
                case "west":
                    if (currentRoom.roomExits.containsKey(input)) {
                        currentRoom = currentRoom.roomExits.get(input);
                        isValid = false;
                        break;
                    }
                default:
                    System.out.println("You hit wall. Try again: ");
                    input = scanner.nextLine().strip().toLowerCase();
                    break;

            }

        }
        System.out.println(currentRoom.getRoomTitle());
        System.out.println(currentRoom.roomItems);

    }

    public static void main(String[] args) {
        Game g = new Game();
        g.start();
    }
}
