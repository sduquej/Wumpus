package com.sd217.DynamicObjects;

import com.sd217.DynamicCaveObject;
import com.sd217.Position;
import com.sd217.World;

/**
 * Created by sduquej on 28/09/15.
 */
public class Wumpus extends DynamicCaveObject {
    private static Wumpus ourInstance = new Wumpus();

    public static Wumpus getInstance() {
        return ourInstance;
    }

    private Wumpus() {
        super();
    }

    @Override
    public boolean move(Position p) {
        return false;
    }

    @Override
    public String whoAmI() {
        return "wumpus";
    }
}
