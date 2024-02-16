package tests;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WorldTest {
    public World world;
    public World maxWorld;
    public World minWorld;
    public Blocks b1;
    public Blocks spawn;

    @BeforeEach
    public void setup() {
        world = new World (5, 6);
        maxWorld = new World(99, 99);
        minWorld = new World(0, 0);
        b1 = new Tile(3, 2);
        spawn = new Spawn(3, 3);
    }

    @Test
    public void testWorld() {
        assertEquals(6, world.getHeight());
        assertEquals(5, world.getWidth());
        assertEquals(30, world.getWorld().size());
        for (Blocks block:world.getWorld()) {
            assertEquals("Empty", block.getType());
        }

        assertEquals(32, maxWorld.getHeight());
        assertEquals(32, maxWorld.getWidth());
        assertEquals(32 * 32, maxWorld.getWorld().size());
        for (Blocks block:maxWorld.getWorld()) {
            assertEquals("Empty", block.getType());
        }

        assertEquals(2, minWorld.getHeight());
        assertEquals(2, minWorld.getWidth());
        assertEquals(4, minWorld.getWorld().size());
        for (Blocks block:minWorld.getWorld()) {
            assertEquals("Empty", block.getType());
        }
    }

    @Test
    public void testSetBlock() {
        try {
            world.setBlock("Tile", 2, 1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(1, world.getBlock(2,1).getYpos());
        assertEquals(2, world.getBlock(2,1).getXpos());
        assertEquals("Tile", world.getBlock(2,1).getType());

        try {
            world.setBlock("Empty", 2, 1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(1, world.getBlock(2,1).getYpos());
        assertEquals(2, world.getBlock(2,1).getXpos());
        assertEquals("Empty", world.getBlock(2,1).getType());

        try {
            world.setBlock("Spawn", 3, 2);
        } catch (Exception e) {
            fail();
        }
        assertEquals(2, world.getBlock(3,2).getYpos());
        assertEquals(3, world.getBlock(3,2).getXpos());
        assertEquals("Spawn", world.getBlock(3,2).getType());

        try {
            world.setBlock("End", 3, 2);
        } catch (Exception e) {
            fail();
        }
        assertEquals(2, world.getBlock(3,2).getYpos());
        assertEquals(3, world.getBlock(3,2).getXpos());
        assertEquals("End", world.getBlock(3,2).getType());

        try {
            world.setBlock("Death", 3, 2);
        } catch (Exception e) {
            fail();
        }
        assertEquals(2, world.getBlock(3,2).getYpos());
        assertEquals(3, world.getBlock(3,2).getXpos());
        assertEquals("Death", world.getBlock(3,2).getType());
    }

    @Test
    public void testSetBlockSpawnAlreadyExists() {
        try {
            world.setBlock("Spawn", 3, 2);
        } catch (Exception e) {
            fail();
        }
        assertEquals(2, world.getBlock(3,2).getYpos());
        assertEquals(3, world.getBlock(3,2).getXpos());
        assertEquals("Spawn", world.getBlock(3,2).getType());
        try {
            world.setBlock("Spawn", 3, 3);
            fail();
        } catch (Exception e) {
            assertEquals(2, world.getBlock(3,2).getYpos());
            assertEquals(3, world.getBlock(3,2).getXpos());
            assertEquals("Spawn", world.getBlock(3,2).getType());
        }
    }

    @Test
    public void testSetBlockInvalidSpawn() {
        try {
            world.setBlock("Spawn", 3, 0);
            fail();
        } catch (Exception e) {
            assertEquals(0, world.getBlock(3,0).getYpos());
            assertEquals(3, world.getBlock(3,0).getXpos());
            assertEquals("Empty", world.getBlock(3,0).getType());
        }
    }

    @Test
    public void testAddBlock() {
        try {
            world.addBlock(b1);
            assertEquals(2, world.getBlock(3, 2).getYpos());
            assertEquals(3, world.getBlock(3, 2).getXpos());
            assertEquals("Tile", world.getBlock(3, 2).getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddBlockAboveSpawn() {
        try {
            world.addBlock(spawn);
        } catch (Exception e) {
            fail();
        }
        assertEquals(3, world.getBlock(3, 3).getYpos());
        assertEquals(3, world.getBlock(3, 3).getXpos());
        assertEquals("Spawn", world.getBlock(3, 3).getType());
        try {
            world.addBlock(b1);
            fail();
        } catch (BlockAboveSpawnException e) {
            assertEquals(2, world.getBlock(3, 2).getYpos());
            assertEquals(3, world.getBlock(3, 2).getXpos());
            assertEquals("Empty", world.getBlock(3, 2).getType());
        }
    }

    @Test
    public void testFill() {
        try {
            world.fill("Tile",2, 3, 4, 5);
        } catch (Exception e) {
            fail();
        }
        for (int i = 2; i <= 4; i++) {
            for (int j = 3; j <= 5; j++) {
                assertEquals("Tile", world.getBlock(i,j).getType());
                assertEquals(i, world.getBlock(i,j).getXpos());
                assertEquals(j, world.getBlock(i,j).getYpos());
            }
        }
    }

    @Test
    public void testFillSpawn() {
        try {
            world.fill("Spawn",2, 3, 4, 5);
            fail();
        } catch (FillWithSpawnException e) {
            for (Blocks block: world.getWorld()) {
                assertEquals("Empty", block.getType());
            }
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testPlaceSpawn() {
        try {
            world.placeSpawn(2, 2);
        } catch (Exception e) {
            fail();
        }
        assertEquals("Spawn", world.getBlock(2, 2).getType());
        assertEquals("Empty", world.getBlock(2, 1).getType());
    }

    @Test
    public void testPlaceSpawnSpawnAlreadyExists() {
        try {
            world.placeSpawn(2, 2);
        } catch (Exception e) {
            fail();
        }
        assertEquals("Spawn", world.getBlock(2, 2).getType());
        assertEquals("Empty", world.getBlock(2, 1).getType());

        try {
            world.placeSpawn(0, 1);
            fail();
        } catch (SpawnAlreadyExistsException e) {
            assertEquals("Spawn", world.getBlock(2, 2).getType());
            assertEquals("Empty", world.getBlock(2, 1).getType());
            assertEquals("Empty", world.getBlock(0, 1).getType());
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    public void testPlaceSpawnIllegalSpawn() {
        try {
            world.placeSpawn(2, 0);
            fail();
        } catch (IllegalSpawnException e) {
            assertEquals("Empty", world.getBlock(2, 0).getType());
        } catch (Exception e) {
            fail();
        }

    }






}