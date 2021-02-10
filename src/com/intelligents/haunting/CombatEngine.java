package com.intelligents.haunting;

public class CombatEngine {

    public static String runCombat(String[] userCommands, Game game) {
        String result = "";
        MiniGhost miniGhost = game.getWorld().getCurrentRoom().getRoomMiniGhost();
        if (userCommands[0].equals("fight")) {
            result = "You punch the " + miniGhost.getName() + " in the face. Your hand passes through, but it dissipates anyways.";
            game.getWorld().getCurrentRoom().setRoomMiniGhost(null);
            System.out.println(game.getWorld().getCurrentRoom().getRoomMiniGhost());
        }
        if (userCommands[0].equals("run")){
            result = "Frightened to the point of tears, you flee back the way you came.";
            game.changeRoom(true, invertPlayerRoom(game.getPlayer().getMostRecentExit()), 0);
        }
        return result;
    }

    private static String[] invertPlayerRoom(String mostRecentExit) {
        String[] opposite = new String[]{"go", null};
        switch (mostRecentExit){
            case "east":
                opposite[1] = "west";
                break;
            case "north":
                opposite[1] = "south";
                break;
            case "south":
                opposite[1] = "north";
                break;
            // default case is west, which will make the player go east in case most recent exit is null from just starting
            default:
                opposite[1] = "east";
                break;
        }
        return opposite;
    }
}
