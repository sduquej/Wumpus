package com.sd217;

import com.sd217.utils.MessageBuilder;
import com.sd217.world.CaveRoom;
import com.sd217.world.Position;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Sebastián Duque on 01/10/15.
 */
public abstract class Player {

    protected Map<Character, String> validActions;
    public static final Map<Character, String> VALID_DIRECTIONS;
    static {
        Map<Character, String> validDirections = new Hashtable<>();
        validDirections.put('W', "up");
        validDirections.put('A', "left");
        validDirections.put('S', "down");
        validDirections.put('D', "right");
        VALID_DIRECTIONS = Collections.unmodifiableMap(validDirections);
    }
    public Player() {
        validActions = new HashMap<>();
        this.validActions.put('M', "move");
        this.validActions.put('S', "shoot");
    }

    /**
     * This method is meant to be invoked when the player has to choose an action
     * "move" or "shoot"
     * @return String indicating the action that is to be performed
     */
    public abstract String askAction();

    /**
     * This method is meant to be invoked when the player has to choose a direction
     * "up", "left", "right" or "down"
     * @return String indicating the direction that was selected
     */
    public abstract String askDirection();

    /**
     * Removes a valid action from the possibilities. This is
     * useful when the Player fires the arrow and can no longer shoot.
     * @param key Action to be removed
     * @return String action removed
     */
    public String removeValidAction(Character key) {
        return validActions.remove(key);
    }

    /**
     * Informs the player about the percepts that can be perceived in the room in the given position
     * @param position Position in which the room is
     * @param roomInPosition Room in the given position
     */
    public void getPercepts(Position position, CaveRoom roomInPosition) {
        System.out.println(MessageBuilder.informPlayer(position, roomInPosition));
        tellKnowledgeBase(position, roomInPosition);
    }

    /**
     * Informs the player about a condition that has changed
     * in the game, such as Wumpus being killed, treasure being collected or arrow being shot
     * @param condition String with the condition that changed
     */
    public abstract void inform(String condition);


    /**
     * Gives the known information to the knowledge base. Some players might decide to ignore
     * this step and do nothing.
     * @param position Position in which the room is
     * @param roomInPosition Room in the given position
     */
    protected abstract void tellKnowledgeBase(Position position, CaveRoom roomInPosition);

}
