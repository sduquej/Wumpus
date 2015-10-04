package com.sd217.players;

import com.sd217.Player;
import com.sd217.utils.CommonMethods;
import com.sd217.world.CaveRoom;
import com.sd217.world.Position;
import com.sd217.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sebastián Duque on 02/10/15.
 * <p>This agent plays the Hunt the Wumpus game making use of a knowledge base.</p>
 * <p>Unlike other agents ({@link RandomAIPlayer} and {@link AlternateUpRightPlayer}) this agent uses information
 * perceived from the different rooms to build a model of the World as its being discovered and increase
 * its possibilities of winning the game.</p>
 */
public class OnlySafeAI extends Player {
    private Position currentPosition;
    private KnowledgeBase kb;

    public OnlySafeAI(Position currentPosition, World world) {
        this.currentPosition = currentPosition;
        this.kb = new KnowledgeBase(world, currentPosition);
    }

    /**
     * The agent uses its knowledge to decide which is the best action to perform
     */
    @Override
    public String askAction() {
        return validActions.get(kb.askAction(currentPosition));
    }

    /**
     * The agent uses its knowledge base to decide which is the best direction to choose
     */
    @Override
    public String askDirection() {
        return kb.askDirection(currentPosition);
    }

    /**
     * The agent tells to the knowledge base the condition that changed
     */
    @Override
    public void inform(String condition) {
        kb.tell(condition);
    }

    /**
     * The agent tells to the knowledge base the different percepts that
     * have been received in the given position
     * @param roomInPosition Room in the given position with percepts
     */
    @Override
    protected void tellKnowledgeBase(Position position, CaveRoom roomInPosition) {
        setCurrentPosition(position);
        kb.tell(position, roomInPosition);
    }

    private void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }
}

/**
 * <p>This class represents the KB of the smart agent.
 * It has a HashMap where it stores knowledge pertaining to all
 * visited rooms and the possibilities of their neighbours.</p>
 *
 */
class KnowledgeBase {
    Map<Position, CaveRoom> rooms;
    Position expectedPosition;
    Position previousPosition;
    World world;

    boolean arrowFired;
    boolean wumpusKilled;
    boolean treasureCollected;

    /**
     * <p>This list is used to keep track of those rooms that have been found to be stenchy in the cave.</p>
     */
    List<Position> stenchyRooms;
    /**
     * <p>Based on the known stenchy rooms their adjacent positions a list containing possible Wumpus locations
     * is kept.</p>
     * <p>The Wumpus' position has been found when there is just one element on this list.</p>
     */
    List<Position> wumpusPossibilities;

    public KnowledgeBase(World world, Position position) {
        this.rooms = createRoomsKB(world.LENGTH, world.WIDTH);
        this.world = world;
        this.expectedPosition = position;
        stenchyRooms = new ArrayList<>(4);
    }

    private Map<Position, CaveRoom> createRoomsKB(int length, int width) {
        Map<Position, CaveRoom> rooms = new HashMap<>();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                rooms.put(new Position(j, i), new CaveRoom());
            }
        }
        return rooms;
    }

    /**
     * Given a position it returns the direction of the neighbouring room
     * where the Wumpus is believed to be or null otherwise
     *
     * @param position Initial position
     * @return String with the direction of the room where the Wumpus is. Null if its
     * not any of the neighbours
     */
    public String directionToWumpus(Position position) {
        Position wumpusRoom = wumpusPossibilities.get(0);
        return world.getDirectionBetweenNeighbours(position, wumpusRoom);
    }

    /**
     * <p>If the Wumpus has been found and its location is a neighbouring position
     * then the best option is to shoot and kill the Wumpus rather than moving.</p>
     */
    public char askAction(Position p) {
        if (!arrowFired && foundWumpus() && directionToWumpus(p) != null) {
            return 'S';
        } else return 'M';
    }

    /**
     * <p>Depending on the action chosen to be performed a direction is selected.</p>
     * <p>If the agent decided to shoot then the direction in which the Wumpus is is returned</p>
     * <p>If the agent decided to move then all neighbouring rooms are ranked based on the current knowledge.
     * The direction returned is that of the highest ranked room. If there is a tie, this is solved
     * by choosing one of them at random.</p>
     */
    public String askDirection(Position p) {
        if (askAction(p) == 'S') {
            return directionToWumpus(p);
        }
        Position[] candidateRooms = world.obtainAdjacentCells(p.getRow(), p.getCol());
        CommonMethods.shuffleArray(candidateRooms);
        int maxScore = Integer.MIN_VALUE;
        int maxScoreIndex = 0;
        for (int i = 0; i < candidateRooms.length; i++) {
            int score = scoreRoomInPosition(candidateRooms[i]);
            if (score > maxScore) {
                maxScore = score;
                maxScoreIndex = i;
            }
        }
        expectedPosition = candidateRooms[maxScoreIndex];
        String direction = world.getDirectionBetweenNeighbours(p, expectedPosition);

        previousPosition = p;
        return direction;
    }

    public void tell(Position where, CaveRoom what) {
        if (!isNewRoom(where)) {
            return;
        }
//            If Adventurer is standing somewhere else, it was teleported!
        if (!(expectedPosition.equals(where))) {
            rooms.get(expectedPosition).setSuperbat(true);
        }

        what.setVisited(true);
        rooms.put(where, what);


        if (what.isStenchy()) stenchyRooms.add(where);
        if (what.isStenchy() || isNeighbourOfStenchy(where)) updateWumpusLocations();
        if (!what.isBreezy() && !what.isStenchy()) trustRoomNeighbours(where);

    }

    private boolean isNeighbourOfStenchy(Position where) {
        for (Position neighbour : world.obtainAdjacentCells(where)) {
            for (Position stenchyRoom : stenchyRooms) {
                if (neighbour.equals(stenchyRoom)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int updateWumpusLocations() {
        wumpusPossibilities = new ArrayList<>();
        if (stenchyRooms.size() == 1) {
            for (Position neighbourOf1 : world.obtainAdjacentCells(stenchyRooms.get(0))) {
                if (!rooms.get(neighbourOf1).isKnown()) {
                    wumpusPossibilities.add(neighbourOf1);
                }
            }
        }
        if (stenchyRooms.size() == 2) {
            for (Position neighbourOf1 : world.obtainAdjacentCells(stenchyRooms.get(0))) {
                for (Position neighbourOf2 : world.obtainAdjacentCells(stenchyRooms.get(1))) {
                    if (neighbourOf1.equals(neighbourOf2) && !rooms.get(neighbourOf2).isKnown()) {
                        wumpusPossibilities.add(neighbourOf1);
                    }
                }
            }
        }
        if (stenchyRooms.size() == 3) {
            for (Position neighbourOf1 : world.obtainAdjacentCells(stenchyRooms.get(0))) {
                for (Position neighbourOf2 : world.obtainAdjacentCells(stenchyRooms.get(1))) {
                    for (Position neighbourOf3 : world.obtainAdjacentCells(stenchyRooms.get(2))) {
                        if (neighbourOf1.equals(neighbourOf2) && neighbourOf1.equals(neighbourOf3)) {
                            wumpusPossibilities.add(neighbourOf1);
                        }
                    }
                }
            }
        }
        return wumpusPossibilities.size();
    }

    private void trustRoomNeighbours(Position room) {
        rooms.get(room).setKnown(true);
        Position[] neighbours = world.obtainAdjacentCells(room);
        for (Position neighbour : neighbours) {
            rooms.get(neighbour).setKnown(true);
        }
    }

    public void tell(String attribute) {
        if (attribute.equals("wumpusKilled")) {
            wumpusKilled = true;
        } else if (attribute.equals("treasureCollected")) {
            treasureCollected = true;
        } else if (attribute.equals("arrowFired")) {
            arrowFired = true;
        }

    }

    /**
     * <p>A room is evaluated based on the current knowledge that the KB has.</p>
     * <p>Previously unexplored rooms that are known to be safe are more points over those of
     * which not much or just bad things are known.</p>
     * <p>A penalty is induced on the previously visited room, to promote exploration of the
     * cave in different directions</p>
     * <p>Once the adventurer has collected the treasure, being near the exit becomes
     * the goal and points are highly given to the room that has it (the exit).</p>
     * @param position Position in which the room to be evaluated is
     * @return int - score obtained by the room
     */
    private int scoreRoomInPosition(Position position) {
        int result = 0;
        CaveRoom cr = rooms.get(position);
        if (cr.wasVisited()) {
            result -= 500;
//        Will win
            if (cr.isExit() && treasureCollected) result += 10000;
            if (position.equals(previousPosition)) result -= 1000;
        } else {
//        Will lose
            if (cr.isPit()) result -= 10000;
            if (!wumpusKilled && cr.hasWumpus()) result -= 10000;
            if (cr.isKnown()) result += 500;
            if (!cr.isKnown()) result -= 2000;
            if (cr.hasSuperbat()) result -= 200;
            if (cr.isGlittery()) result += 200;
        }


        System.out.println(position + " score " + result);
        return result;
    }

    private boolean isNewRoom(Position where) {
        return !rooms.get(where).wasVisited();
    }

    private boolean foundWumpus() {
        if (wumpusPossibilities == null) return false;
        return wumpusPossibilities.size() == 1;
    }
}