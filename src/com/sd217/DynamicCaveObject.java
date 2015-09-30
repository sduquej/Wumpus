package com.sd217;

import com.sd217.Exceptions.AlreadyDeadException;
import javafx.geometry.Pos;

import java.awt.*;

/**
 * Created by sduquej on 28/09/15.
 */
public abstract class DynamicCaveObject {
    private World world;
    private Position currentPosition;
    private boolean alive, init;

    protected DynamicCaveObject() {
        this.alive = true;
    }

    public DynamicCaveObject init(int column, int row, World world) {
        if(!init) {
            this.world = world;
            this.currentPosition = new Position(column, row);
            init = true;
        }
        return this;
    }

    public abstract boolean move(Position p);
    public abstract String whoAmI();

    public String killedBy(String killer){
        if(!this.isAlive()) throw new AlreadyDeadException(whoAmI());
        setAlive(false);
        return ColorCodes.RED + whoAmI() + ColorCodes.GREEN + " has been killed by the " + ColorCodes.YELLOW + killer;
    }
    public boolean atLocation(int col, int row){
        return getCurrentPosition().getCol() == col & getCurrentPosition().getRow() == row;
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
}
