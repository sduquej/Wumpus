package dynamicobjects;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Sebastián Duque on 04/10/15.
 */
public class WumpusTest {
    DynamicCaveObject wumpus;
    @Before
    public void setUp() throws Exception {
        wumpus = Wumpus.getInstance();
    }

    @Test
    public void testWhoAmI() throws Exception {
        assertEquals("should identify as wumpus", "wumpus", wumpus.whoAmI());
    }
}