package tests;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulateTest {
    public Simulate simulate;
    public World world;

    @BeforeEach
    public void setup() {
        simulate = new Simulate();
        world = new World(6,6);
        try {
            world.placeSpawn(0, 5);
        } catch (Exception e) {
            //Assume no exception caught since manual initialization
        }
    }

    @Test
    public void testSimulate() {
        assertFalse(simulate.isGameOver());
    }

    @Test
    public void testInitializeSimulation() {
        simulate.initializeSimulation(world);
        assertEquals("Player", world.getBlock(0,4).getType());
    }

    @Test
    public void testUpdateWhenOver() {
        simulate.initializeSimulation(world);
        simulate.setGameOver();

        for (Blocks block:world.getWorld()) {
            if (block.getXpos() != 0 && block.getYpos() != 5) {
                assertEquals("Empty", block.getType());
            } else if (block.getXpos() == 0 && block.getYpos() == 5) {
                assertEquals("Spawn", block.getType());
            }
        }
    }

    @Test
    public void testKeyEvent() {
        simulate.keyEvent('a');
        assertEquals('a', simulate.getInput());

        simulate.keyEvent('b');
        assertEquals('b', simulate.getInput());
    }

    @Test
    public void testReflectPlayerToWorld() {
        simulate.initializeSimulation(world);
        simulate.reflectPlayerToWorld();
        assertEquals("Player", world.getBlock(0,4).getType());
        for (Blocks block:world.getWorld()) {
            if (block.getXpos() != 0 && block.getYpos() != 4) {
                assertNotEquals("Player", block.getType());
            }
        }
    }

    @Test
    public void testUpdatePlayerPositions() {
    }

    @Test
    public void testLeftBehaviours() {
    }

    @Test
    public void testRightBehaviours() {
    }

    @Test
    public void testFallingBehaviours() {
    }

    @Test
    public void testJumpingBehaviours() {
    }

    @Test
    public void testNeutralBehaviours() {
    }

    @Test
    public void testRespawn() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().moveRight();
        simulate.respawn();
        assertEquals(0, simulate.getPlayer().getXpos());
        assertEquals(4, simulate.getPlayer().getYpos());
        assertFalse(simulate.getPlayer().getFalling());
        assertFalse(simulate.getPlayer().getJumping());
    }

    @Test
    public void testSetGameOver() {
        assertFalse(simulate.isGameOver());
        simulate.setGameOver();
        assertTrue(simulate.isGameOver());
    }

    @Test
    public void testRemovePlayer() {
        simulate.initializeSimulation(world);
        simulate.removePlayer();
        for (Blocks block:world.getWorld()) {
            assertNotEquals("Player", block.getType());
        }
    }






}