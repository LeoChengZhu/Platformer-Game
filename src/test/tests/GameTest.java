package tests;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    public Game game;
    public World world;
    public JsonReader reader;

    @BeforeEach
    public void setup() {
        reader = new JsonReader("./data/save.json");
        game = new Game();
        world = new World(2, 2);
    }

    @Test
    public void testGame() {
        assertEquals("", game.getInput());
        assertFalse(game.getSimulating());
    }

    @Test
    public void testCreateWorld() {
        game.createWorld(4, 4);
        assertEquals("aaaaaaaaaaaaaaaa", game.getGameWorld());
    }

    @Test
    // Actual update is tested in SimulateTest
    public void testUpdateWhenGameIsOver() {
        game.createWorld(4, 4);
        try {
            game.getWorld().setBlock("Spawn", 2, 2);
        } catch (Exception e) {
            fail();
        }
        game.play();
        assertEquals("Player", game.getWorld().getBlock(2, 1).getType());
        assertTrue(game.getSimulating());
        game.getSimulate().setGameOver();
        game.update();
        assertFalse(game.getSimulating());
    }

    @Test
    public void testHandleInput() {
        try {
            game.handleInput('a');
        } catch (Exception e) {
            fail();
        }
        assertEquals("a", game.getInput());

        try {
            game.handleInput('b');
        } catch (Exception e) {
            fail();
        }
        assertEquals("ab", game.getInput());
    }

    @Test
    public void testHandleInputSimulating() {
        game.createWorld(4, 4);
        try {
            game.getWorld().setBlock("Spawn", 2, 2);
        } catch (Exception e) {
            fail();
        }
        game.play();
        assertEquals("Player", game.getWorld().getBlock(2, 1).getType());
        assertTrue(game.getSimulating());

        try {
            game.handleInput('a');
        } catch (Exception e) {
            fail();
        }
        assertEquals("", game.getInput());
    }

    @Test
    public void testHandleInputBackSpace() {
        try {
            game.handleInput('a');
        } catch (Exception e) {
            fail();
        }
        assertEquals("a", game.getInput());

        try {
            game.handleInput('\b');
        } catch (Exception e) {
            fail();
        }
        assertEquals("", game.getInput());
    }

    @Test
    public void testHandleInputBackSpaceNoSave() {
        try {
            game.handleInput('\b');
        } catch (Exception e) {
            fail();
        }
        assertEquals("", game.getInput());
    }

    @Test
    public void testHandleInputReturn() {
        game.createWorld(4, 4);
        try {
            game.handleInput('s');
            game.handleInput('e');
            game.handleInput('t');
            game.handleInput(' ');
            game.handleInput('T');
            game.handleInput('i');
            game.handleInput('l');
            game.handleInput('e');
            game.handleInput(' ');
            game.handleInput('2');
            game.handleInput(' ');
            game.handleInput('3');
        } catch (Exception e) {
            fail();
        }
        assertEquals("set Tile 2 3", game.getInput());


        try {
            game.handleInput('\n');
        } catch (Exception e) {
            fail();
        }
        assertEquals("", game.getInput());
        assertEquals("Tile", game.getWorld().getBlock(2, 3).getType());
    }

    @Test
    public void testHandleInputReturnNoSave() {
        try {
            game.handleInput('\n');
        } catch (Exception e) {
            fail();
        }
        assertEquals("", game.getInput());
    }

    @Test
    public void testHandleInputIsControl() {
        try {
            game.handleInput('\t');
        } catch (Exception e) {
            fail();
        }
        assertEquals("", game.getInput());
    }

    @Test
    public void testHandleCreateCommandPlay() {
        game.createWorld(4, 4);
        try {
            game.getWorld().setBlock("Spawn", 2, 2);
            game.handleCreateCommand("play");
        } catch (Exception e) {
            fail();
        }

        assertEquals("Player", game.getWorld().getBlock(2, 1).getType());
        assertTrue(game.getSimulating());
        assertNotNull(game.getSimulating());

    }

    @Test
    public void testHandleCreateCommandPlace() {
        game.createWorld(4, 4);
        try {
            game.handleCreateCommand("set Tile 2 2");
        } catch (Exception e) {
            fail();
        }
        assertEquals("Tile", game.getWorld().getBlock(2, 2).getType());
    }

    @Test
    public void testHandleCreateCommandQuit() {
        game.setWorld(world);
        try {
            game.handleCreateCommand("quit");
        } catch (Exception e) {
            fail();
        }
        assertTrue(game.isOver());
        try {
            World testWorld = reader.read("name");
            assertEquals("Empty", testWorld.getBlock(0, 0).getType());
            assertEquals("Empty", testWorld.getBlock(1, 0).getType());
            assertEquals("Empty", testWorld.getBlock(0, 1).getType());
            assertEquals("Empty", testWorld.getBlock(1, 1).getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testHandleCreateCommandSave() {
        game.setWorld(world);
        try {
            game.handleCreateCommand("save");
        } catch (Exception e) {
            fail();
        }
        assertFalse(game.isOver());
        try {
            World testWorld = reader.read("name");
            assertEquals("Empty", testWorld.getBlock(0, 0).getType());
            assertEquals("Empty", testWorld.getBlock(1, 0).getType());
            assertEquals("Empty", testWorld.getBlock(0, 1).getType());
            assertEquals("Empty", testWorld.getBlock(1, 1).getType());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testHandleCreateCommandInvalid() {
        game.createWorld(4, 4);
        try {
            game.handleCreateCommand("set Tile 2 -1");
            fail();
        } catch (Exception e) {
            //pass
        }
    }


    @Test
    public void testHandleCreateCommandFill() {
        game.createWorld(6, 6);
        try {
            game.handleCreateCommand("fill Tile 1 2 4 5");
        } catch (Exception e) {
            fail();
        }

        for (int i = 1; i <= 4; i++) {
            for (int j = 2; j <= 5; j++) {
                assertEquals("Tile", game.getWorld().getBlock(i,j).getType());
                assertEquals(i, game.getWorld().getBlock(i,j).getXpos());
                assertEquals(j, game.getWorld().getBlock(i,j).getYpos());
            }
        }

    }

    @Test
    public void testHandleCreateCommandInvalidInput() {
        game.createWorld(4, 4);

        try {
            game.handleCreateCommand("set Tile 3 e");
            fail();
        } catch (Exception e) {
            // do nothing when exception caught
        }

        try {
            game.handleCreateCommand("set Tile -1 -2");
            fail();
        } catch (Exception e) {
            // do nothing when exception caught
        }

        try {
            game.handleCreateCommand("set Tile 1.1 2");
            fail();
        } catch (Exception e) {
            // do nothing when exception caught
        }

        try {
            game.handleCreateCommand("fill Spawn 0 0 2 2");
            fail();
        } catch (Exception e) {
            // do nothing when exception caught
        }

        try {
            game.handleCreateCommand("fill Spawn 0.1 0 2 2");
            fail();
        } catch (Exception e) {
            // do nothing when exception caught
        }

        try {
            game.handleCreateCommand("fill Tile -1 -1 2 2");
            fail();
        } catch (Exception e) {
            // do nothing when exception caught
        }

        try {
            game.handleCreateCommand("fill Tile a b c d");
            fail();
        } catch (Exception e) {
            // do nothing when exception caught
        }

        try {
            game.handleCreateCommand("fill Tile 1 3");
        } catch (Exception e) {
            fail();
        }
    }



    @Test
    public void testGetGameWorld() {
        game.createWorld(4, 4);
        try {
            game.getWorld().setBlock("Tile", 0, 0);
            game.getWorld().setBlock("Empty", 0, 1);
            game.getWorld().addBlock(new Player(0, 2));
            game.getWorld().setBlock("End", 0, 3);
            game.getWorld().setBlock("Death", 1, 0);
            game.getWorld().setBlock("Spawn", 1, 3);
        } catch (Exception e) {
            fail();
        }
        assertEquals("tdaaaaaapaaaesaa", game.getGameWorld());
    }

    @Test
    public void testEmptyVariationsNotSimulating() {
        assertEquals("a", game.emptyVariations());
    }

    @Test
    public void testEmptyVariationsSimulating() {
        game.createWorld(4, 4);
        try {
            game.getWorld().setBlock("Spawn", 2, 2);
        } catch (Exception e) {
            fail();
        }
        game.play();
        assertTrue(game.getSimulating());
        assertEquals(" ", game.emptyVariations());
        assertNotNull(game.getSimulating());
    }

    @Test
    public void testPlay() {
        game.createWorld(4, 4);
        try {
            game.getWorld().setBlock("Spawn", 2, 2);
        } catch (Exception e) {
            fail();
        }
        game.play();
        assertEquals("Player", game.getWorld().getBlock(2, 1).getType());
        assertTrue(game.getSimulating());
        assertNotNull(game.getSimulating());
    }


    @Test
    public void testCheckForSpawnNoSpawn() {
        game.createWorld(4, 4);
        assertFalse(game.checkForSpawn());
    }

    @Test
    public void testCheckForSpawn() {
        game.createWorld(4, 4);
        try {
            game.getWorld().setBlock("Spawn", 2, 2);
        } catch (Exception e) {
            fail();
        }
        assertTrue(game.checkForSpawn());
    }

    @Test
    public void testStop() {
        game.stop();
        assertFalse(game.getSimulating());

        game.createWorld(4, 4);
        try {
            game.getWorld().setBlock("Spawn", 2, 2);
        } catch (Exception e) {
            fail();
        }
        game.play();
        assertTrue(game.getSimulating());

        game.stop();
        assertFalse(game.getSimulating());
    }

    @Test
    public void testMakeGameOver() {
        assertFalse(game.isOver());
        game.makeGameOver();
        assertTrue(game.isOver());
    }

    @Test
    public void testMakeSetWorld() {
        game.setWorld(world);
        assertEquals(world, game.getWorld());
    }

    @Test
    public void testSave() {
        game.setWorld(world);
        game.save("name");
        try {
            World testWorld = reader.read("name");
            assertEquals("Empty", testWorld.getBlock(0, 0).getType());
            assertEquals("Empty", testWorld.getBlock(1, 0).getType());
            assertEquals("Empty", testWorld.getBlock(0, 1).getType());
            assertEquals("Empty", testWorld.getBlock(1, 1).getType());
        } catch (Exception e) {
            fail();
        }
    }
}