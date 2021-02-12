package com.intelligents.haunting;

import javax.swing.*;
import java.io.IOException;

public class CombatEngine {

    public static String runCombat(String userChoice, Game game) throws IOException {
        String result = "";
        if (userChoice.equals("fight")) {
            boolean inFight = true;
            while (inFight) {
                String fightResult = mortalCombat(game, result);
                if (fightResult.contains("invalid") || fightResult.contains("hoping")) {
                    //output result message and loop again
                    game.narrateNoNewLine(fightResult + "\n");
                } else if (fightResult.contains("dissipates") || fightResult.contains("whence")) {
                    game.getWorld().getCurrentRoom().setRoomMiniGhost(null);
                    result = fightResult;
                    inFight = false;
                } else {
                    result = fightResult;
                    inFight = false;
                    game.changeRoom(true, invertPlayerRoom(game.getPlayer().getMostRecentExit()), 0);
                }
            }
        }
        // adding comment for github connection
        if (userChoice.equals("run")) {
            result = "Frightened to the point of tears, you flee back the way you came.";
            game.changeRoom(true, invertPlayerRoom(game.getPlayer().getMostRecentExit()), 0);
        }
        return result;
    }

    private static String mortalCombat(Game game, String result) {
        showStatus(game);
        return processChoice(game, result);
    }

    private static void showStatus(Game game) {
        game.narrateNoNewLine("Combat commencing...");
    }

    private static String processChoice(Game game, String result) {
        MiniGhost battleGhost = game.getWorld().getCurrentRoom().getRoomMiniGhost();
        String choices = JOptionPane.showInputDialog("Choose your action: \n" +
                "1 - Swing Iron Bar!\n" +
                "2 - Sweat on it!\n" +
                "3 - Punch it!\n" +
                "4 - Run!\n");
        game.narrateNoNewLine(choices + ">>");
//        String input = userInput.getText().strip().toLowerCase();
        switch (choices) {
            case "1":
                result = "You swing the iron bar, and the " + battleGhost.getName() + " dissipates.";
                break;
            case "2":
                result = "You collect an impressive amount of sweat from your body " +
                        "and throw it at the " + battleGhost.getName() + ". While gross, the " +
                        "extreme salt content in your perspiration banishes the " +
                        battleGhost.getName() + " back to whence it came.";
                break;
            case "3":
                result = "You punch at the " + battleGhost.getName() + " , but your hand passes right through. " +
                        "What were you hoping to achieve?";
                break;
            case "4":
                result = "You think better about your choices, and decide to flee back the way you came.";
                break;
            default:
                result = "That is an invalid option, please pick 1-4.";
                break;
        }
        return result;
    }

    private static String[] invertPlayerRoom(String mostRecentExit) {
        String[] opposite = new String[]{"go", null};
        switch (mostRecentExit) {
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
