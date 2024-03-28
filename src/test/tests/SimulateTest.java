package tests;

import model.*;
import model.Blocks;
import model.BlockAboveSpawnException;
import model.IllegalSpawnException;
import model.SpawnAlreadyExistsException;
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
            world.placeSpawn(3, 5);
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
        assertEquals("Player", world.getBlock(3,4).getType());
    }

    @Test
    public void testUpdateWhenOver() {
        simulate.initializeSimulation(world);
        simulate.setGameOver();

        for (Blocks block:world.getWorld()) {
            if (block.getXpos() != 3 && block.getYpos() != 5) {
                assertEquals("Empty", block.getType());
            } else if (block.getXpos() == 3 && block.getYpos() == 5) {
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
        Player player = new Player(0,0);
        try {
            simulate.getGameWorld().getWorld().set(0, player);
        } catch (Exception e) {
            fail();
        }
        simulate.reflectPlayerToWorld();

        assertEquals("Player", world.getBlock(3,4).getType());
        assertEquals("Empty", world.getBlock(0,0).getType());
        for (Blocks block:world.getWorld()) {
            if (block.getXpos() != 3 && block.getYpos() != 4) {
                assertNotEquals("Player", block.getType());
            }
        }
    }

    @Test
    public void testUpdatePlayerPositionsInvalidInput() {
        simulate.initializeSimulation(world);
        simulate.reflectPlayerToWorld();
        simulate.keyEvent('c');
        simulate.updatePlayerPositions();
        assertEquals("Player", world.getBlock(3,4).getType());
        assertEquals('0', simulate.getInput());
        assertEquals(false, simulate.getPlayer().getJumping());
        assertEquals(false, simulate.getPlayer().getFalling());
    }

    @Test
    public void testUpdatePlayerPositionsNeutral() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(5);
        simulate.reflectPlayerToWorld();
        simulate.keyEvent('c');
        simulate.updatePlayerPositions();
        assertEquals("Player", world.getBlock(0,5).getType());
        assertEquals('0', simulate.getInput());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testUpdatePlayerPositionsJumping() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(5);
        simulate.reflectPlayerToWorld();
        assertFalse(simulate.getPlayer().getJumping());
        simulate.keyEvent('w');
        simulate.updatePlayerPositions();
        simulate.reflectPlayerToWorld();
        assertEquals('0', simulate.getInput());
        assertEquals("Player", world.getBlock(0,4).getType());
        assertTrue(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testUpdatePlayerPositionsNeutralToFalling() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(3);
        simulate.reflectPlayerToWorld();
        assertFalse(simulate.getPlayer().getJumping());
        simulate.keyEvent('c');
        simulate.updatePlayerPositions();
        simulate.reflectPlayerToWorld();
        assertEquals('0', simulate.getInput());
        assertEquals("Player", world.getBlock(0,3).getType());
        assertFalse(simulate.getPlayer().getJumping());
        assertTrue(simulate.getPlayer().getFalling());
    }

    @Test
    public void testUpdatePlayerPositionsLeft() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(1);
        simulate.getPlayer().setYpos(5);
        simulate.reflectPlayerToWorld();
        assertFalse(simulate.getPlayer().getJumping());
        simulate.keyEvent('a');
        simulate.updatePlayerPositions();
        simulate.reflectPlayerToWorld();
        assertEquals('0', simulate.getInput());
        assertEquals("Player", world.getBlock(0,5).getType());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testUpdatePlayerPositionsRight() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(4);
        simulate.getPlayer().setYpos(5);
        simulate.reflectPlayerToWorld();
        assertFalse(simulate.getPlayer().getJumping());
        simulate.keyEvent('d');
        simulate.updatePlayerPositions();
        simulate.reflectPlayerToWorld();
        assertEquals('0', simulate.getInput());
        assertEquals("Player", world.getBlock(5,5).getType());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testUpdatePlayerPositionsFalling() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(3);
        simulate.reflectPlayerToWorld();
        simulate.getPlayer().setFalling(true);
        simulate.keyEvent('w');
        simulate.updatePlayerPositions();
        simulate.reflectPlayerToWorld();
        assertEquals('0', simulate.getInput());
        assertEquals("Player", world.getBlock(0,4).getType());
        assertTrue(simulate.getPlayer().getJumping());
        assertTrue(simulate.getPlayer().getFalling());
    }


    @Test
    public void testUpdatePlayerPositionsGameOver() {
        simulate.initializeSimulation(world);
        simulate.setGameOver();
        simulate.keyEvent('o');
        simulate.updatePlayerPositions();

        for (Blocks block:world.getWorld()) {
            if (block.getXpos() != 3 && block.getYpos() != 5) {
                assertEquals("Empty", block.getType());
            } else if (block.getXpos() == 3 && block.getYpos() == 5) {
                assertEquals("Spawn", block.getType());
            }
        }
        assertEquals('0', simulate.getInput());
    }

    @Test
    public void testLeftBehavioursEmpty() {
        simulate.initializeSimulation(world);
        simulate.reflectPlayerToWorld();
        simulate.leftBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(2, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testLeftBehavioursBoundary() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.reflectPlayerToWorld();
        simulate.leftBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(0, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testLeftBehavioursTile() throws
            BlockAboveSpawnException, SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(1);
        simulate.getGameWorld().setBlock("Tile", 0, 4);
        simulate.reflectPlayerToWorld();
        simulate.leftBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(1, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testLeftBehavioursSpawn() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(4);
        simulate.getPlayer().setYpos(5);
        simulate.reflectPlayerToWorld();
        simulate.leftBehaviours();
        assertEquals(5, simulate.getPlayer().getYpos());
        assertEquals(4, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testLeftBehavioursDeath() throws
            BlockAboveSpawnException, SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(1);
        simulate.getGameWorld().setBlock("Death", 0, 4);
        simulate.reflectPlayerToWorld();
        simulate.leftBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(3, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testLeftBehavioursEnd() throws
            BlockAboveSpawnException, SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(1);
        simulate.getGameWorld().setBlock("End", 0, 4);
        simulate.reflectPlayerToWorld();
        simulate.leftBehaviours();
        assertTrue(simulate.isGameOver());
    }

    @Test
    public void testRightBehavioursEmpty() {
        simulate.initializeSimulation(world);
        simulate.reflectPlayerToWorld();
        simulate.rightBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(4, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testRightBehavioursBoundary() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(5);
        simulate.reflectPlayerToWorld();
        simulate.rightBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(5, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testRightBehavioursTile() throws
            BlockAboveSpawnException, SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(4);
        simulate.getGameWorld().setBlock("Tile", 5, 4);
        simulate.reflectPlayerToWorld();
        simulate.rightBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(4, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testRightBehavioursSpawn() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(2);
        simulate.getPlayer().setYpos(5);
        simulate.reflectPlayerToWorld();
        simulate.rightBehaviours();
        assertEquals(5, simulate.getPlayer().getYpos());
        assertEquals(2, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testRightBehavioursDeath() throws
            BlockAboveSpawnException, SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(4);
        simulate.getGameWorld().setBlock("Death", 5, 4);
        simulate.reflectPlayerToWorld();
        simulate.rightBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(3, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testRightBehavioursEnd() throws
            BlockAboveSpawnException, SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(4);
        simulate.getGameWorld().setBlock("End", 5, 4);
        simulate.reflectPlayerToWorld();
        simulate.rightBehaviours();
        assertTrue(simulate.isGameOver());
    }

    @Test
    public void testFallingBehavioursBoundary() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(5);
        simulate.getPlayer().setFalling(true);
        simulate.reflectPlayerToWorld();
        assertTrue(simulate.getPlayer().getFalling());
        simulate.fallingBehaviours();
        assertEquals(5, simulate.getPlayer().getYpos());
        assertEquals(0, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testFallingBehavioursEmpty() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(4);
        simulate.getPlayer().setFalling(true);
        simulate.reflectPlayerToWorld();
        assertTrue(simulate.getPlayer().getFalling());
        simulate.fallingBehaviours();
        assertEquals(5, simulate.getPlayer().getYpos());
        assertEquals(0, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertTrue(simulate.getPlayer().getFalling());
    }

    @Test
    public void testFallingBehavioursTile() throws BlockAboveSpawnException,
            SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(4);
        simulate.getPlayer().setFalling(true);
        simulate.getGameWorld().setBlock("Tile", 0, 5);
        simulate.reflectPlayerToWorld();
        assertTrue(simulate.getPlayer().getFalling());
        simulate.fallingBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(0, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testFallingBehavioursDeath() throws BlockAboveSpawnException,
            SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(4);
        simulate.getPlayer().setFalling(true);
        simulate.getGameWorld().setBlock("Death", 0, 5);
        simulate.reflectPlayerToWorld();
        assertTrue(simulate.getPlayer().getFalling());
        simulate.fallingBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(3, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testFallingBehavioursEnd() throws BlockAboveSpawnException,
            SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(4);
        simulate.getPlayer().setFalling(true);
        simulate.getGameWorld().setBlock("End", 0, 5);
        simulate.reflectPlayerToWorld();
        assertTrue(simulate.getPlayer().getFalling());
        simulate.fallingBehaviours();
        assertTrue(simulate.isGameOver());
    }

    @Test
    public void testFallingBehavioursSpawn() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(3);
        simulate.getPlayer().setYpos(4);
        simulate.getPlayer().setFalling(true);
        simulate.reflectPlayerToWorld();
        assertTrue(simulate.getPlayer().getFalling());
        simulate.fallingBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(3, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testJumpingBehavioursBoundary() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(0);
        simulate.getPlayer().setJumping(true);
        simulate.reflectPlayerToWorld();
        simulate.jumpingBehaviours();
        assertEquals(0, simulate.getPlayer().getYpos());
        assertEquals(0, simulate.getPlayer().getXpos());
        assertTrue(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testJumpingBehavioursEmpty() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(1);
        simulate.getPlayer().setJumping(true);
        simulate.reflectPlayerToWorld();
        simulate.jumpingBehaviours();
        assertEquals(0, simulate.getPlayer().getYpos());
        assertEquals(0, simulate.getPlayer().getXpos());
        assertTrue(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testJumpingBehavioursTile() throws BlockAboveSpawnException,
            SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(1);
        simulate.getPlayer().setJumping(true);
        simulate.getGameWorld().setBlock("Tile", 0, 0);
        simulate.reflectPlayerToWorld();
        simulate.jumpingBehaviours();
        assertEquals(1, simulate.getPlayer().getYpos());
        assertEquals(0, simulate.getPlayer().getXpos());
        assertTrue(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testJumpingBehavioursDeath() throws BlockAboveSpawnException,
            SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(1);
        simulate.getPlayer().setJumping(true);
        simulate.getGameWorld().setBlock("Death", 0, 0);
        simulate.reflectPlayerToWorld();
        simulate.jumpingBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(3, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testJumpingBehavioursEnd() throws BlockAboveSpawnException,
            SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(1);
        simulate.getPlayer().setJumping(true);
        simulate.getGameWorld().setBlock("End", 0, 0);
        simulate.reflectPlayerToWorld();
        simulate.jumpingBehaviours();
        assertTrue(simulate.isGameOver());
    }

    @Test
    public void testJumpingBehavioursSpawn() throws BlockAboveSpawnException,
            SpawnAlreadyExistsException, IllegalSpawnException {
        World world1 = new World(3,3);
        world1.setBlock("Spawn", 0,1);
        simulate.initializeSimulation(world1);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(2);
        simulate.getPlayer().setJumping(true);
        simulate.reflectPlayerToWorld();
        simulate.jumpingBehaviours();
        assertEquals(2, simulate.getPlayer().getYpos());
        assertEquals(0, simulate.getPlayer().getXpos());
        assertTrue(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testJumpingBehavioursFalling() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setFalling(true);
        simulate.getPlayer().setJumping(true);
        simulate.reflectPlayerToWorld();
        simulate.jumpingBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(3, simulate.getPlayer().getXpos());
        assertTrue(simulate.getPlayer().getJumping());
        assertTrue(simulate.getPlayer().getFalling());
    }

    @Test
    public void testNeutralBehavioursBoundary() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(5);
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
        simulate.reflectPlayerToWorld();
        simulate.neutralBehaviours();
        assertEquals(5, simulate.getPlayer().getYpos());
        assertEquals(0, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testNeutralBehavioursEmpty() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(1);
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
        simulate.reflectPlayerToWorld();
        simulate.neutralBehaviours();
        assertEquals(1, simulate.getPlayer().getYpos());
        assertEquals(0, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertTrue(simulate.getPlayer().getFalling());
    }

    @Test
    public void testNeutralBehavioursDeath() throws BlockAboveSpawnException,
            SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(1);
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
        simulate.getGameWorld().setBlock("Death", 0, 2);
        simulate.reflectPlayerToWorld();
        simulate.neutralBehaviours();
        assertEquals(4, simulate.getPlayer().getYpos());
        assertEquals(3, simulate.getPlayer().getXpos());
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
    }

    @Test
    public void testNeutralBehavioursEnd() throws BlockAboveSpawnException,
            SpawnAlreadyExistsException, IllegalSpawnException {
        simulate.initializeSimulation(world);
        simulate.getPlayer().setXpos(0);
        simulate.getPlayer().setYpos(1);
        assertFalse(simulate.getPlayer().getJumping());
        assertFalse(simulate.getPlayer().getFalling());
        simulate.getGameWorld().setBlock("End", 0, 2);
        simulate.reflectPlayerToWorld();
        simulate.neutralBehaviours();
        assertTrue(simulate.isGameOver());
    }

    @Test
    public void testRespawn() {
        simulate.initializeSimulation(world);
        simulate.getPlayer().moveRight();
        simulate.respawn();
        assertEquals(3, simulate.getPlayer().getXpos());
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

    @Test
    public void testGetGameWorld() {
        simulate.initializeSimulation(world);
        assertEquals(world, simulate.getGameWorld());
    }
}