package com.sd217.dynamicobjects;

import com.sd217.DynamicCaveObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sebastián Duque on 04/10/15.
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