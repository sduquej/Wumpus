package com.sd217.world;

import com.sd217.DynamicCaveObject;
import com.sd217.dynamicobjects.Adventurer;
import com.sd217.dynamicobjects.Wumpus;
import com.sd217.utils.ColorCodes;
import com.sd217.utils.CommonMethods;


/**
 * Created by Sebastián Duque on 17/09/15.
 */
public class World {
    /**
     * Constant. Width of the cave
     */
    public final int WIDTH;
    /**
     * Constant. Length of the cave
     */
    public final int LENGTH;
    /**
     * 2D-Array. Used to model the torus shaped cave and its contents
     */
    private static CaveRoom[][] cave;

    /**
     * Constructor
     * Creates the Wumpus world in which the agent will be trapped
     *  The cave is a 2D-array of the form cave[row][column]
     * @param width  width of the cave
     * @param length length of the cave
     */
    public World(int length, int width) {
        this.LENGTH = length;
        this.WIDTH = width;
        this.cave = new CaveRoom[length][width];


        initiateCave(0.15);
        placeRandomBats(WIDTH * LENGTH / 10);
        placeTreasure();
        identifyPits();
        placeWumpus();
    }

    /**
     * Places bats in random positions that are safe
     * @param numberOfBats number of bats to place
     */
    private void placeRandomBats(int numberOfBats) {
        int batsPlaced = 0;
        Position randomBatPosition;
        CaveRoom candidateRoom;
        do {
            randomBatPosition = getRandomPosition();
            candidateRoom = getRoomInPosition(randomBatPosition);
            if(candidateRoom.isRoomSafe()){
                candidateRoom.setSuperbat(true);
                batsPlaced += 1;
            }
        } while(batsPlaced < numberOfBats);
    }

    /**
     * Places the treasure in a random location that is safe
     */
    private void placeTreasure() {
//        The treasure won't be placed in a pit
        Position randomTreasurePosition;
        CaveRoom candidateRoom;
        do {
            randomTreasurePosition = getRandomPosition();
            candidateRoom = getRoomInPosition(randomTreasurePosition);
        } while(!candidateRoom.isRoomSafe());


        candidateRoom.setContainsTreasure(true);
        setGlitterOnAdjacentCells(randomTreasurePosition.getRow(), randomTreasurePosition.getCol(), true);
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
        Position oldPosition = dynamicObject.getCurrentPosition();
        CaveRoom oldRoom = getRoomInPosition(oldPosition);
        CaveRoom newRoom = getRoomInPosition(p);

        if(p == null) {
//            If a location is not specified, it moves to a random neighbour
            p = obtainAdjacentCells(oldPosition)[CommonMethods.getRandomInt(0, 3 + 1)];
        }
        if(dynamicObject instanceof Adventurer) {
            oldRoom.setAdventurer(false);
            newRoom.setAdventurer(true);
        } else if(dynamicObject instanceof Wumpus) {
            oldRoom.setWumpus(false);
            setStenchOnAdjacentCells(oldPosition.getRow(), oldPosition.getCol(), false);

            newRoom.setWumpus(true);
            setStenchOnAdjacentCells(p.getRow(), p.getCol(), true);
        }
        dynamicObject.move(p);
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
        CaveRoom treasureRoom = getRoomInPosition(treasurePosition);
        if(treasureRoom.containsTreasure()){
            treasureRoom.setContainsTreasure(false);
            setGlitterOnAdjacentCells(posRow, posCol, false);
            treasureRemoved = true;
        }
        return treasureRemoved;
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
            wumpus.init(posCol, posRow);
        }
        getRoomInPosition(randomWumpusPosition).setWumpus(true);
        setStenchOnAdjacentCells(posRow, posCol, true);
    }

    /**
     * Retrieves a random position on the cave
     * @return Position that was obtained randomly
     */
    public Position getRandomPosition(){
        int randomNumber = CommonMethods.getRandomInt(1, (LENGTH * WIDTH) + 1);
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
     * Updates the rooms affected by the Wumpus when it is killed
     * @param wumpus Instance of a dead wumpus
     * @return boolean, whether the wumpus was dead or not
     */
    public boolean wumpusKilled(DynamicCaveObject wumpus) {
        boolean wumpusKilled = false;
        Position wumpusPosition = wumpus.getCurrentPosition();
        CaveRoom wumpusRoom = getRoomInPosition(wumpusPosition);

        if(!wumpus.isAlive() && wumpusRoom.hasWumpus()){
            wumpusRoom.setWumpus(false);
            setStenchOnAdjacentCells(wumpusPosition.getRow(), wumpusPosition.getCol(), false);
            wumpusKilled = true;
        }
        return wumpusKilled;
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
            CaveRoom room = getRoomInPosition(pos);
            if(!room.isPit()) {
                room.setStenchy(stench);
            }
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
            CaveRoom room = getRoomInPosition(pos);
            if(!room.isPit()) {
                room.setGlittery(treasure);
            }
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
     * Given the position of a room returns an array containing the neighbouring rooms
     *
     * @param p Position of the desired room
     * @return Array with the neighbour positions
     *           first element - right
     *           second element - left
     *           third element - up
     *           fourth element - down
     */
    public Position[] obtainAdjacentCells(Position p) {
        return obtainAdjacentCells(p.getRow(), p.getCol());
    }


    /**
     * Given the position of a room returns an array containing the neighbouring rooms
     * *
     * @param row    row in which the centre is located
     * @param column column in which the centre is located
     * @return Array with the neighbour positions
     *           first element - right
     *           second element - left
     *           third element - up
     *           fourth element - down
     */
    public Position[] obtainAdjacentCells(int row, int column) {
        Position[] neighbours = new Position[4];

        neighbours[0] = getNeighbourRight(column, row);
        neighbours[1] = getNeighbourLeft(column, row);

        neighbours[2] = getNeighbourDown(column, row);
        neighbours[3] = getNeighbourUp(column, row);

        return neighbours;
    }

    /**
     * Returns the neighbour position in a specific direction, given the starting position
     * @param positionFrom Position from which the neighbour will be found
     * @param direction Direction in which the neighbour will be found
     * @return Position corresponding to the neighbour in the given direction from the given Position
     */
    public Position getNeighbourInDirection(Position positionFrom, String direction){
        Position pos = null;
        if(positionFrom != null) {
            switch (direction) {
                case "up":
                    pos = getNeighbourUp(positionFrom);
                    break;
                case "down":
                    pos = getNeighbourDown(positionFrom);
                    break;
                case "left":
                    pos = getNeighbourLeft(positionFrom);
                    break;
                case "right":
                    pos = getNeighbourRight(positionFrom);
                    break;
            }
        }
        return pos;
    }

    /**
     * Returns the direction in which the neighbour in positionTo is from positionFrom or null
     * if they're not neighbours
     * @param positionFrom First room
     * @param positionTo Target room
     * @return String with the direction of the relation, or null if the positions are not neighbours
     */
    public String getDirectionBetweenNeighbours(Position positionFrom, Position positionTo){
        if(getNeighbourUp(positionFrom).equals(positionTo)) return "up";
        if(getNeighbourDown(positionFrom).equals(positionTo)) return "down";
        if(getNeighbourLeft(positionFrom).equals(positionTo)) return "left";
        if(getNeighbourRight(positionFrom).equals(positionTo)) return "right";

        return null;
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
        for (CaveRoom room : row) {
            String padded = String.format("%" + ((3 + ColorCodes.RESET_STRING_LENGTH) +
                    (ColorCodes.COLOR_STRING_LENGTH * room.getNumberOfObjects())) + "s", room);
            if(room.wasVisited()){
                padded = padded.replace("1m", "4m");
            }
            sb.append(ColorCodes.RESET + "|" + padded);
        }
        sb.append(ColorCodes.RESET +"|\n");
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
        StringBuilder sb = new StringBuilder(ColorCodes.RESET);
        sb.append("\t");
        for (int i = 0; i < WIDTH; i++) {
            sb.append(String.format(" %3s", header ? String.valueOf(i + 1) + " " : DASHES));
        }
        sb.append("\n");
        return sb.toString();
    }

//  END OF   Printing the world
}
