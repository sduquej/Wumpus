package players;

import world.CaveRoom;
import world.Position;

/**
 * <p>This is a dumb agent. Each turn it decides to move as it will never be sure
 * of the location of the Wumpus. In fact, it might run into it.</p>
 * <p>It has no state or memory of where it's been and what it's seen.</p>
 * <p>The direction in which it moves alternates between up and right, as this is
 * the pattern it uses to traverse the cave.</p>
 * @author Sebastián Duque on 29/09/15.
 * */
public class AlternateUpRightPlayer extends Player {
    /**
     * Variable used as a flag to control the alternation of the chosen direction
     */
    boolean up;

    /**
     * This agent always chooses to move
     */
    @Override
    public String askAction() {
        return "move";
    }

    /**
     * It begins by moving to the right and then the next turn up. This pattern is
     * followed until the game is over.
     */
    @Override
    public String askDirection() {
        String chosenDirection = up ? "up" : "right";
        up = !up;
        return chosenDirection;
    }

    /**
     * This agent ignores whatever information it's given. It can only move in random directions
     * @param condition String with the condition that changed
     */
    @Override
    public void inform(String condition) {}

    /**
     * This agent ignores whatever information it's given. It can only move in random directions
     * No knowledge base is used by this agent.
     * @param position Position in which the room is
     * @param roomInPosition Room in the given position
     */
    @Override
    protected void tellKnowledgeBase(Position position, CaveRoom roomInPosition) {}
}
