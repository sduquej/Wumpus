package dynamicobjects;

import exceptions.AlreadyDeadException;
import world.Position;
import world.World;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Sebastián Duque on 04/10/15.
 */
public class AdventurerTest {
    DynamicCaveObject adventurer;
    World w;
    @Before
    public void setUp() throws Exception {
        w = new World(2,3);
        adventurer = Adventurer.getInstance().init(0, 1);
    }

    @After
    public void tearDown() throws Exception {
        w = null;
        adventurer = null;
    }

    @Test
    public void currentPositionIsUpdatedOnMove() throws Exception {
        Position oldPosition = adventurer.getCurrentPosition();
        Position newPosition;
        do{
            newPosition = w.getRandomPosition();
        } while(!oldPosition.equals(newPosition));
        adventurer.move(newPosition);

        assertTrue(adventurer.getCurrentPosition().equals(newPosition));
    }

    @Test
    public void testWhoAmI() throws Exception {
        assertEquals("should identify as adventurer", "adventurer", adventurer.whoAmI());
    }

    @Test
    public void killingAdventurerAffectsAlive() throws Exception {
        assert adventurer.isAlive();
        adventurer.killedBy("");
        assertFalse(adventurer.isAlive());
    }

    @Test(expected=AlreadyDeadException.class)
    public void canNotKillADeadAdventurer() throws Exception {
        assert !adventurer.isAlive();
        adventurer.killedBy("");
    }
}