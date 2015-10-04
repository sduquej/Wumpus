package com.sd217.world;

/**
 * Created by Sebastián Duque on 28/09/15.
 */
public class Position {
    private int col;
    private int row;

    public Position(int col, int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public String toString() {
        return "position: [" +
                (col+1) +
                "," + (row+1) +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (getCol() != position.getCol()) return false;
        return getRow() == position.getRow();
    }

    @Override
    public int hashCode() {
        int result = getCol();
        result = 31 * result + getRow();
        return result;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}