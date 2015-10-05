package com.sd217.utils;

import com.sd217.GameRunner;
import com.sd217.world.CaveRoom;
import com.sd217.world.Position;

import java.util.Map;

/**
 * Created by Sebastián Duque on 28/09/15.
 * This class contains helper methods for console I/O
 */
public class MessageBuilder {
    public static String optionsInMap(Map<?, String> somethingStringMap, String header){
        StringBuilder sb = new StringBuilder();
        if(header != null) sb.append(header+"\n");
        sb.append("It can be:\n");
        for (Map.Entry<?, String> entry : somethingStringMap.entrySet()) {
            sb.append("[" + entry.getKey() + "]\t" + entry.getValue() + "\n");
        }
        return(sb.toString());
    }

    public static String optionsInMap(Map<Character, String> charStringMap) {
        return optionsInMap(charStringMap, null);
    }

    public static String gameIntroduction(){
        StringBuilder sb = new StringBuilder(ColorCodes.CLEAR_SCREEN);
        sb.append(ColorCodes.GREEN + "Hello and welcome to Wumpus World\n\n" +
                "Let us begin... But first, you will be asked to define a size for the cave\n");
        sb.append("The cave has the shape of a rectangle but its edges are connected, think of it as a torus");
        return sb.toString();
    }

    public static String askDimension(String dimension){
        return ColorCodes.GREEN + "What should be its " + dimension + "? " +
                "(" + GameRunner.MIN_DIMENSION + " - " + GameRunner.MAX_DIMENSION +") ";
    }

    public static String wumpusHit() {
        return ColorCodes.PURPLE + "ARRRRGHHHHHH!\t" + ColorCodes.WHITE + "What was that noise?";
    }

    public static String arrowLost() {
        return ColorCodes.GREEN + "You fire and ... nothing\n" + "Say goodbye to your " + ColorCodes.YELLOW + "arrow";
    }

    public static String decisionMade(String action, String direction) {
        return "You decided to " + action.toUpperCase() + " in the " + direction.toUpperCase() + " direction";
    }

    public static String choosePlayer(Map<Integer, String> validPlayers) {
        return optionsInMap(validPlayers, "Who will be playing this game?");
    }

    public static String informPlayer(Position p, CaveRoom room) {
        return "You are now in " + p + ". It looks like this: " + room;
    }
}
