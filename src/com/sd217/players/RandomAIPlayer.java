package com.sd217.players;

import com.sd217.Player;
import com.sd217.utils.CommonMethods;
import com.sd217.world.CaveRoom;
import com.sd217.world.Position;

/**
 * Created by Sebastián Duque on 01/10/15.
 * <p>This is not a very intelligent agent. Each turn it decides to move as it will never be sure
 * of the location of the Wumpus.</p>
 * <p>It has no state or memory of where it's been and what it's seen.</p>
 * <p>The direction in which it moves is chosen at random on each turn</p>
 */
public class RandomAIPlayer extends Player {
    /**
     * This agent always chooses to move
     */
    @Override
    public String askAction() {
        return "move";
    }

    /**
     * A direction is chosen at random from the possibilities
     */
    @Override
    public String askDirection() {
        Object[] directions = VALID_DIRECTIONS.values().toArray();
        return (String) directions[CommonMethods.getRandomInt(1, directions.length)];
    }

    /**
     * This agent ignores whatever information it's given. It can only move in random directions
     * @param condition String with the condition that changed
     */
    @Override
    public void inform(String condition) {
    }

    /**
     * This agent ignores whatever information it's given. It can only move in random directions
     * No knowledge base is used by this agent.
     * @param position Position in which the room is
     * @param roomInPosition Room in the given position
     */
    @Override
    protected void tellKnowledgeBase(Position position, CaveRoom roomInPosition) {}
}
