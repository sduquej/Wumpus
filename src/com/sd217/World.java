package com.sd217;

import com.sd217.DynamicObjects.Adventurer;
import com.sd217.DynamicObjects.Wumpus;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by sduquej on 17/09/15.
 */
public class World {
    public final int WIDTH, LENGTH;
    private static CaveRoom[][] cave;

    /**
     * Constructor
     * Creates the Wumpus world in which the agent will be trapped
     *  The cave is a 2D-array of the form cave[row][column]
     * @param width  width of the cave
     * @param length length of the cave
     */
    public World(int length, int width) {
        this.WIDTH = width;
        this.LENGTH = length;
        this.cave = new CaveRoom[length][width];


        initiateCave(0.10);
        placeTreasure();
        identifyPits();
        placeWumpus();
//      placeSomeRandomBats

    }

    /**
     * Places the treasure in a random location that is safe
     */
    private void placeTreasure() {
//        The treasure won't be placed in a pit
        Position randomTreasurePosition;
        CaveRoom candidateRoom;
        int posRow;
        int posCol;
        do {
            randomTreasurePosition = getRandomPosition();
            posRow = randomTreasurePosition.getRow();
            posCol = randomTreasurePosition.getCol();
            candidateRoom = cave[posRow][posCol];
        } while(!candidateRoom.isRoomSafe());


        candidateRoom.setContainsTreasure(true);
        setGlitterOnAdjacentCells(posRow, posCol, true);
    }

    /**
     * Removes the treasure from the given position, provided it is there.
     * @param treasurePosition Position from which the treasure is to be removed
     * @return boolean - true if the room in the given position had the treasure and it was removed, false otherwise.
     */
    public boolean removeTreasure(Position treasurePosition) {
        boolean treasureRemoved = false;
        int posRow = treasurePosition.getRow();
        int posCol = treasurePosition.getCol();
        CaveRoom treasureRoom = cave[posRow][posCol];
        if(treasureRoom.containsTreasure()){
            treasureRoom.setContainsTreasure(false);
            setGlitterOnAdjacentCells(posRow, posCol, false);
            treasureRemoved = true;
        }
        return treasureRemoved;
    }

    /**
     * This method must be called to place the adventurer in its starting position.
     * The current cave design places the exit in this same room.
     * @param adventurer adventurer that will be placed in the cave
     */
    public void placeAdventurer(DynamicCaveObject adventurer){
        Adventurer adv = (Adventurer) adventurer;
        if(adv.isInit()) {
            CaveRoom room = getRoomInPosition(adv.getCurrentPosition());
            room.setAdventurer(true);
            room.setExit(true);
        }
    }

    /**
     * Moves the given dynamic object to a specified position, or to a random neighbouring position otherwise
     * @param dynamicObject Dynamic Object to be moved - Adventurer or Wumpus
     * @param p - Position to which the object will be moved. If is null it will be chosen at random from one of the
     *          neighbours.
     */
    public void moveDynamicObject(DynamicCaveObject dynamicObject, Position p){
        int oldRow = dynamicObject.getRow();
        int oldCol = dynamicObject.getColumn();
        if(p == null) {
//            If a location is not specified, it moves to a random neighbour
            p = obtainAdjacentCells(oldRow, oldCol)[ThreadLocalRandom.current().nextInt(0, 3 + 1)];
        }
        int newRow = p.getRow();
        int newCol = p.getCol();
        if(dynamicObject instanceof Adventurer) {
            cave[oldRow][oldCol].setAdventurer(false);
            cave[newRow][newCol].setAdventurer(true);
        } else if(dynamicObject instanceof Wumpus){
            cave[oldRow][oldCol].setWumpus(false);
            setStenchOnAdjacentCells(oldRow, oldCol, false);

            cave[newRow][newCol].setWumpus(true);
            setStenchOnAdjacentCells(newRow, newCol, true);
        }
        dynamicObject.move(p);
    }

    /**
     * Puts the Wumpus in a random room inside the cave and spreads the stench to the adjacent rooms
     */
    private void placeWumpus() {
        Position randomWumpusPosition = getRandomPosition();
        int posRow = randomWumpusPosition.getRow();
        int posCol = randomWumpusPosition.getCol();
        Wumpus wumpus = Wumpus.getInstance();
        if(!wumpus.isInit()){
            wumpus.init(posCol, posRow, this);
        }
        cave[posRow][posCol].setWumpus(true);
        setStenchOnAdjacentCells(posRow, posCol, true);
    }

    /**
     * Retrieves a random position on the cave
     * @return Position that was obtained randomly
     */
    public Position getRandomPosition(){
        int randomNumber = ThreadLocalRandom.current().nextInt(1, (LENGTH * WIDTH) + 1);
        int row = randomNumber / WIDTH;
        int col = randomNumber % WIDTH;

        if (col != 0) {
            col = col - 1;
        } else {
            row = row - 1;
            col = WIDTH - 1;
        }

        return new Position(col, row);
    }

    /**
     * Initiates the rooms of the cave either empty or being a pit
     *
     * @param pitPercentage the percentage of rooms that will be a pit
     */
    private void initiateCave(double pitPercentage) {
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                cave[i][j] = new CaveRoom(Math.random() < pitPercentage);
            }
        }
    }

    /**
     * This method should only be invoked once the cave has been initialized.
     * It will identify those cells where there are pits and create a breeze in the adjacent ones
     */
    private void identifyPits() {
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (cave[i][j].isPit()) {
                    createBreezeOnAdjacentCells(i, j);
                }
            }
        }
    }

    /**
     * Given the position of a pit, creates breeze in the adjacent cells.
     *
     * @param column column in which the pit is located
     * @param row    row in which the pit is located
     */
    private void createBreezeOnAdjacentCells(int column, int row) {
        Position[] neighbours = obtainAdjacentCells(column, row);
        for (Position pos : neighbours) {
            CaveRoom room = cave[pos.getRow()][pos.getCol()];
            if(!room.isPit()){
                room.setBreezy(true);
            }
        }
    }

    /**
     * Given the position of a wumpus, sets the stench in the adjacent cells.
     *
     * @param column column in which the wumpus is located
     * @param row    row in which the wumpus is located
     * @param stench whether the cells are to be stenchy or not
     */
    private void setStenchOnAdjacentCells(int row, int column, boolean stench) {
        Position[] neighbours = obtainAdjacentCells(row, column);
        for (Position pos : neighbours) {
            cave[pos.getRow()][pos.getCol()].setStenchy(stench);
        }
    }

    /**
     * Given the position of the treasure, creates a glitter effect in the adjacent cells.
     *
     * @param row    row in which the treasure is located
     * @param column column in which the treasure is located
     * @param treasure
     */
    private void setGlitterOnAdjacentCells(int row, int column, boolean treasure) {
        Position[] neighbours = obtainAdjacentCells(row, column);
        for (Position pos : neighbours) {
            cave[pos.getRow()][pos.getCol()].setGlittery(treasure);
        }
    }

    /**
     * Given a position returns the room in the cave that is occupying it
     * @param p Position of interest
     * @return CaveRoom that is in the given position
     */
    public CaveRoom getRoomInPosition(Position p){
        int row = p.getRow();
        int col = p.getCol();

        return cave[row][col];
    }
//    Locating neighbours and adjacent cells
    /**
     * Given the position of a cell, creates the given signal in the adjacent cells.
     * Because the cave is a torus it must be considered when the pit is in one of the, otherwise, edges of the cave
     *
     * @param row    row in which the centre is located
     * @param column column in which the centre is located
     */
    public Position[] obtainAdjacentCells(int row, int column) {
        Position[] neighbours = new Position[4];

        neighbours[0] = getNeighbourRight(column, row);
        neighbours[1] = getNeighbourLeft(column, row);

        neighbours[2] = getNeighbourDown(column, row);
        neighbours[3] = getNeighbourUp(column, row);

        return neighbours;
    }

    public Position getNeighbourInDirection(Position p, String direction){
        Position pos = null;
        if(p != null) {
            switch (direction) {
                case "up":
                    pos = getNeighbourUp(p);
                    break;
                case "down":
                    pos = getNeighbourDown(p);
                    break;
                case "left":
                    pos = getNeighbourLeft(p);
                    break;
                case "right":
                    pos = getNeighbourRight(p);
                    break;
            }
        }
        return pos;
    }

    public Position getNeighbourLeft(Position p){
        return getNeighbourLeft(p.getCol(), p.getRow());
    }
    public Position getNeighbourRight(Position p){
        return getNeighbourRight(p.getCol(), p.getRow());
    }
    public Position getNeighbourUp(Position p){
        return getNeighbourUp(p.getCol(), p.getRow());
    }
    public Position getNeighbourDown(Position p){
        return getNeighbourDown(p.getCol(), p.getRow());
    }

    public Position getNeighbourLeft(int column, int row){
        return new Position(((column - 1) % WIDTH + WIDTH) % WIDTH, row);
    }
    public Position getNeighbourRight(int column, int row){
        return new Position(((column + 1) % WIDTH + WIDTH) % WIDTH, row);
    }
    public Position getNeighbourUp(int column, int row){
        return new Position(column, ((row - 1) % LENGTH + LENGTH) % LENGTH);
    }
    public Position getNeighbourDown(int column, int row){
        return new Position(column, ((row + 1) % LENGTH + LENGTH) % LENGTH);
    }
//  END OF  Locating neighbours and adjacent cells

//  BEGIN   Printing the world
    /**
     * Overrides Object.toString() to return a String that can be printed to
     * represent the current status of the WumpusWorld
     *
     * @return String representation of the WumpusWorld
     */
    @Override
    public String toString() {
        String headingNumbers = createHorizontalLine(true);
        String horizontalLine = createHorizontalLine(false);
        StringBuilder sb = new StringBuilder(ColorCodes.RESET);

        sb.append(headingNumbers + horizontalLine);
        for (int i = 0; i < cave.length; i++) {
            sb.append(i + 1 + "\t" + printRow(cave[i]));
            sb.append(horizontalLine);
        }
        return sb.toString();
    }

    private String printRow(CaveRoom[] row) {
        StringBuilder sb = new StringBuilder();
        String cellRepresentation;
        for (CaveRoom i : row) {
            try {
                cellRepresentation = i.printInMap();
            } catch (NullPointerException ex) {
                System.err.println("Empty cell error: " + i);
                cellRepresentation = " ";
            }
            sb.append(String.format("|%" + ((3 + ColorCodes.RESET_STRING_LENGTH) +
                    (ColorCodes.COLOR_STRING_LENGTH * i.getNumberOfObjects())) + "s", cellRepresentation));
        }
        sb.append("|\n");
        return sb.toString();
    }

    /**
     * Creates a horizontal line to be used in the grid that describes the WumpusWorld
     *
     * @param header If true, it indicates that the line should contain the numbers that label each column
     *               If false, it indicates that the line is used to delimit the different rows of the cave
     * @return horizontal line using the given string
     */
    private String createHorizontalLine(boolean header) {
        final String DASHES = "---";
        StringBuilder sb = new StringBuilder();
        sb.append("\t");
        for (int i = 0; i < WIDTH; i++) {
            sb.append(String.format(" %3s", header ? String.valueOf(i + 1) + " " : DASHES));
        }
        sb.append("\n");
        return sb.toString();
    }
//  END OF   Printing the world
}
