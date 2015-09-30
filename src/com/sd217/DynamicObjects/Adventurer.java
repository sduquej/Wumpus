package com.sd217.DynamicObjects;

import com.sd217.DynamicCaveObject;
import com.sd217.Position;

/**
 * Created by sduquej on 28/09/15.
 */
public class Adventurer extends DynamicCaveObject {
    private static Adventurer ourInstance = new Adventurer();

    public static Adventurer getInstance() {
        return ourInstance;
    }

    private Adventurer() {
        super();
    }

    @Override
    public boolean move(Position p) {
        boolean moved = false;
        if(isAlive()){
            if(!getCurrentPosition().equals(p)){
                setCurrentPosition(p);
                moved = true;
            }
        }
        return moved;
    }

    @Override
    public String whoAmI() {
        return "adventurer";
    }
}
