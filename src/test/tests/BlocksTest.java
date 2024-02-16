package tests;

import model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class BlocksTest {
    public Blocks end;
    public Blocks empty;
    public Blocks death;
    public Blocks tile;
    public Blocks spawn;

    @BeforeEach
    public void setup() {
        end = new End(1, 2);
        empty = new Empty(3, 4);
        death = new Death(5, 6);
        tile = new Tile(7, 8);
        spawn = new Spawn(9, 10);
    }

    @Test
    public void testConstructors() {
        assertEquals(1, end.getXpos());
        assertEquals(2, end.getYpos());
        assertEquals("End", end.getType());

        assertEquals(3, empty.getXpos());
        assertEquals(4, empty.getYpos());
        assertEquals("Empty", empty.getType());

        assertEquals(5, death.getXpos());
        assertEquals(6, death.getYpos());
        assertEquals("Death", death.getType());

        assertEquals(7, tile.getXpos());
        assertEquals(8, tile.getYpos());
        assertEquals("Tile", tile.getType());

        assertEquals(9, spawn.getXpos());
        assertEquals(10, spawn.getYpos());
        assertEquals("Spawn", spawn.getType());
    }

}