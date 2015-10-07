package dynamicobjects;

import world.Position;

/**
 * @author Sebastián Duque on 28/09/15.
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
        return "wumpus";
    }
}
