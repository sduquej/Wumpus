package com.sd217;

import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by sduquej on 28/09/15.
 */
public class MessageBuilder {
    public static String optionsInCharStringMap(Map<Character, String> charStringMap, String header){
        StringBuilder sb = new StringBuilder();
        if(header != null) sb.append(header+"\n");
        sb.append("It can be:\n");
        for (Map.Entry<Character, String> entry : charStringMap.entrySet()) {
            sb.append("[" + entry.getKey() + "]\t" + entry.getValue() + "\n");
        }
        return(sb.toString());
    }

    public static String optionsInCharStringMap(Map<Character, String> charStringMap) {
        return optionsInCharStringMap(charStringMap, null);
    }

    public static String gameIntroduction(){
        StringBuilder sb = new StringBuilder();
        sb.append(ColorCodes.GREEN + "Hello and welcome to Wumpus World\n\n" +
                "Let us begin... But first, you will be asked to define a size for the cave\n");
        sb.append("The cave has the shape of a rectangle.");
        return sb.toString();
    }

    public static String askDimension(String dimension){
        return "What should be its " + dimension + "? " +
                "(" + GameRunner.MIN_DIMENSION + " - " + GameRunner.MAX_DIMENSION +")";
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
}
