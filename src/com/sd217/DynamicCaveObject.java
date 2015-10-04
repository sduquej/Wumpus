package com.sd217;

import com.sd217.exceptions.AlreadyDeadException;
import com.sd217.utils.ColorCodes;
import com.sd217.world.Position;

/**
 * Created by Sebastián Duque on 28/09/15.
 * Abstract class for Dynamic Objects. Objects of this type can move during
 * the game, occupying different rooms at different timespans.
 * Concrete classes are expected to be the Adventurer and the Wumpus
 */
public abstract class DynamicCaveObject {
    private Position currentPosition;
    private boolean alive, init;
    private boolean hasTreasure;
    protected DynamicCaveObject() {
        this.alive = true;
    }

    /**
     * Initialises the object by giving it its initial position
     * @param column column of the initial position
     * @param row row of the initial position
     * @return same object after being initialised
     */
    public DynamicCaveObject init(int column, int row) {
        if(!init) {
            this.currentPosition = new Position(column, row);
            init = true;
        }
        return this;
    }

    /**
     * Moves the dynamic object to the specified position
     * @param p new Position where the dynamic object will be moving to
     * @return true if the object moved, false otherwise.
     */
    public abstract boolean move(Position p);

    /**
     * Identifies the dynamic object, with a unique String representation
     * @return String that identifies the nature of the dynamic object.
     */
    public abstract String whoAmI();

    /**
     * Ends the live of the dynamic object
     * @param killer String with the identification of the killer
     * @return String message composed of who the dynamic object was and what killed it
     * @throws AlreadyDeadException If the dynamic object is already dead, this exception is thrown.
     */
    public String killedBy(String killer) throws AlreadyDeadException{
        if(!this.isAlive()) throw new AlreadyDeadException(whoAmI());
        setAlive(false);
        return ColorCodes.RED + whoAmI() + ColorCodes.GREEN + " has been killed by the " +
                ColorCodes.RED + killer + ColorCodes.RESET;
    }

    /**
     * Checks whether the dynamic object's current location matches the given location
     * @param p Location where the dynamic object is thought to be
     * @return boolean, whether the locations match or not
     */
    public boolean atLocation(Position p){
        return this.getCurrentPosition().equals(p);
    }

    @Override
    public String toString() {
        return whoAmI();
    }

    //    Getters and Setters
    public int getColumn() {
        return getCurrentPosition().getCol();
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    protected void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getRow() {
        return getCurrentPosition().getRow();
    }

    public boolean isAlive() {
        return alive;
    }

    private void setAlive(boolean alive) {
        this.alive = alive;
    }
    public boolean isInit() {
        return init;
    }

    public boolean hasTreasure() {
        return hasTreasure;
    }

    public void setTreasure(boolean hasTreasure) {
        System.out.println(ColorCodes.GREEN + whoAmI() + " has collected the " + ColorCodes.YELLOW + "treasure.");
        this.hasTreasure = hasTreasure;
    }
}
