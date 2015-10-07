package utils;

import world.CaveRoom;
import world.Position;

import java.util.Map;

/**
 * This class contains helper methods for console I/O
 * @author Sebastián Duque on 28/09/15.
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
        sb.append("The cave has the shape of a rectangle but its edges are connected, think of it as a torus\n");
        sb.append("These are the symbols you need to be familiar with to read the map of the cave\n" + printMapLegend());
        return sb.toString();
    }

    public static String askDimension(String dimension, int minValue, int maxValue){
        return ColorCodes.GREEN + "What should be its " + dimension + "? " +
                "(" + minValue + " - " + maxValue +") ";
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

    public static String printMapLegend(){
        return  ColorCodes.BLUE   + "A\t Adventurer\n" +
                ColorCodes.RED    + "W\t Wumpus\n" +
                ColorCodes.GREEN  + "S\t Stench\n" +
                ColorCodes.RED    + "P\t Pit\n" +
                ColorCodes.CYAN   + "~\t Breeze\n" +
                ColorCodes.YELLOW + "T\t Treasure\n" +
                ColorCodes.YELLOW + "£\t Glitter\n" +
                ColorCodes.PURPLE + "B\t Superbat\n" +
                ColorCodes.WHITE  + "E\t Exit\n";
    }
}
