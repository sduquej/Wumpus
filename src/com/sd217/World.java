package com.sd217;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by sduquej on 17/09/15.
 */
public class World {
    private int width;
    private int length;
    private static CaveRoom[][] cave;

    private class Position {
        private int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    /**
     * Constructor
     * Creates the Wumpus world in which the agent will be trapped
     *
     * @param width  width of the cave
     * @param length length of the cave
     */
    public World(int length, int width) {
        this.width = width;
        this.length = length;
        this.cave = new CaveRoom[length][width];


        initiateCave(0.20);
        identifyPits();
        placeWumpus();
//        openExit();
//        placeTreasure();
    }

    /**
     * Puts the Wumpus in a random room inside the cave and spreads the stench to the adjacent rooms
     */
    private void placeWumpus() {
        int wumpusPosition, row, col;

        wumpusPosition = ThreadLocalRandom.current().nextInt(1, (this.length * this.width) + 1);
        row = wumpusPosition / this.width;
        col = wumpusPosition % this.width;
        if (col != 0) {
            col = col - 1;
        } else {
            row = row - 1;
            col = this.width - 1;
        }
        cave[row][col].setWumpus(true);
        createStenchOnAdjacentCells(row, col);
    }

    /**
     * Initiates the rooms of the cave either empty or being a pit
     *
     * @param pitPercentage the percentage of rooms that will be a pit
     */
    private void initiateCave(double pitPercentage) {
        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.width; j++) {
                cave[i][j] = new CaveRoom(Math.random() < pitPercentage); //? CaveItem.PIT : CaveItem.EMPTY;
            }
        }
    }

    /**
     * This method should only be invoked once the cave has been initialized.
     * It will identify those cells where there are pits and create a breeze in the adjacent ones
     */
    private void identifyPits() {
        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.width; j++) {
                if (cave[i][j].isPit()) {
                    createBreezeOnAdjacentCells(i, j);
                }
            }
        }
    }

    /**
     * Given the position of a pit, creates breeze in the adjacent cells.
     * Simpler API that uses obtainNeighbours()
     *
     * @param column column in which the pit is located
     * @param row    row in which the pit is located
     */
    private void createBreezeOnAdjacentCells(int column, int row) {
        Position[] neighbours = obtainAdjacentCells(column, row);
        for (Position pos : neighbours) {
            cave[pos.x][pos.y].setBreezy(true);
        }
    }

    /**
     * Given the position of a wumpus, creates stench in the adjacent cells.
     * Simpler API that uses obtainNeighbours()
     *
     * @param column column in which the wumpus is located
     * @param row    row in which the wumpus is located
     */
    private void createStenchOnAdjacentCells(int row, int column) {
        Position[] neighbours = obtainAdjacentCells(row, column);
        for (Position pos : neighbours) {
            cave[pos.x][pos.y].setStenchy(true);
        }
    }

    /**
     * Given the position of a cell, creates the given signal in the adjacent cells.
     * Because the cave is a torus it must be considered when the pit is in one of the, otherwise, edges of the cave
     *
     * @param row    row in which the centre is located
     * @param column column in which the centre is located
     */
    private Position[] obtainAdjacentCells(int row, int column) {
        Position[] neighbours = new Position[4];

        neighbours[0] = new Position(((row + 1) % this.length + this.length) % this.length, column);
        neighbours[1] = new Position(((row - 1) % this.length + this.length) % this.length, column);

        neighbours[2] = new Position(row, ((column + 1) % this.width + this.width) % this.width);
        neighbours[3] = new Position(row, ((column - 1) % this.width + this.width) % this.width);

        return neighbours;
    }

    /**
     * Given the coordinates of a cell, adds the signal to it
     * TODO: instead of just replacing empty cells make it so multiple signals can be together
     * @param column column in which the signal is added
     * @param row row in which the signal is added
     * @param signal signal that will be perceived in the cell
     * @return boolean indicating whether the signal was added or not
     */
//    private boolean addSignalToCell(int row, int column, CaveItem signal){
//        if(cave[row][column] == CaveItem.EMPTY){
//            cave[row][column] = signal;
//            return true;
//        }
//        return false;
//    }

    //    TODO remove this method
//    public void placeItemInCave(int x, int y, CaveItem item){
//        cave[x][y] = item;
//    }

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
            sb.append(String.format("|%" + (12 + (10 * i.getNumberOfObjects())) + "s", cellRepresentation));
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
        for (int i = 0; i < width; i++) {
            sb.append(String.format(" %3s", header ? String.valueOf(i + 1) + " " : DASHES));
        }
        sb.append("\n");
        return sb.toString();
    }
}
