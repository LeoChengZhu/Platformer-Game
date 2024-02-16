package tests;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    public Player player;

    @BeforeEach
    public void setup() {
        player = new Player(1, 5);
    }

    @Test
    public void testPlayer() {
        assertEquals(1, player.getXpos());
        assertEquals(5, player.getYpos());
        assertEquals("Player", player.getType());
        assertEquals(2, player.getJumpTick());
        assertEquals(10, player.getAirTick());
        assertFalse(player.getJumping());
        assertFalse(player.getFalling());
    }

    @Test
    public void testMoveLeft() {
        player.moveLeft();
        assertEquals(0, player.getXpos());
        assertEquals(5, player.getYpos());
    }

    @Test
    public void testMoveRight() {
        player.moveRight();
        assertEquals(2, player.getXpos());
        assertEquals(5, player.getYpos());
    }

    @Test
    public void testFall() {
        player.fall();
        assertEquals(1, player.getXpos());
        assertEquals(6, player.getYpos());
    }


    @Test
    public void testSetJumping() {
        player.setJumping(true);
        assertTrue(player.getJumping());
        assertFalse(player.getFalling());

        player.setJumping(false);
        assertFalse(player.getJumping());
        assertFalse(player.getFalling());
    }

    @Test
    public void testSetFalling() {
        player.setFalling(true);
        assertFalse(player.getJumping());
        assertTrue(player.getFalling());

        player.jump();
        player.setFalling(false);
        assertFalse(player.getJumping());
        assertFalse(player.getFalling());
        assertEquals(2, player.getJumpTick());
        assertEquals(10, player.getAirTick());
    }

    @Test
    public void testJump() {
        player.jump();
        assertEquals(1, player.getXpos());
        assertEquals(4, player.getYpos());
        assertFalse(player.getJumping());
        assertFalse(player.getFalling());
        assertEquals(1, player.getJumpTick());
        assertEquals(10, player.getAirTick());
    }

    @Test
    public void testJumpNoJumpTick() {
        player.jump();
        player.jump();
        player.jump();
        assertEquals(1, player.getXpos());
        assertEquals(3, player.getYpos());
        assertFalse(player.getJumping());
        assertFalse(player.getFalling());
        assertEquals(0, player.getJumpTick());
        assertEquals(9, player.getAirTick());
    }

    @Test
    public void testJumpNoJumpTickNoAirTick() {
        player.jump();
        player.jump();
        player.jump();
        player.jump();
        player.jump();
        player.jump();
        player.jump();
        player.jump();
        player.jump();
        player.jump();
        player.jump();
        player.jump();
        assertEquals(1, player.getXpos());
        assertEquals(3, player.getYpos());
        assertFalse(player.getJumping());
        assertTrue(player.getFalling());
        assertEquals(0, player.getJumpTick());
        assertEquals(0, player.getAirTick());
    }

    @Test
    public void testHitHead() {
        player.hitHead();
        assertEquals(1, player.getXpos());
        assertEquals(5, player.getYpos());
        assertFalse(player.getJumping());
        assertFalse(player.getFalling());
        assertEquals(1, player.getJumpTick());
        assertEquals(10, player.getAirTick());
    }

    @Test
    public void testHitHeadNoJumpTick() {
        player.hitHead();
        player.hitHead();
        player.hitHead();
        assertEquals(1, player.getXpos());
        assertEquals(5, player.getYpos());
        assertFalse(player.getJumping());
        assertFalse(player.getFalling());
        assertEquals(0, player.getJumpTick());
        assertEquals(9, player.getAirTick());
    }

    @Test
    public void testHitHeadNoJumpTickNoAirTick() {
        player.hitHead();
        player.hitHead();
        player.hitHead();
        player.hitHead();
        player.hitHead();
        player.hitHead();
        player.hitHead();
        player.hitHead();
        player.hitHead();
        player.hitHead();
        player.hitHead();
        player.hitHead();
        assertEquals(1, player.getXpos());
        assertEquals(5, player.getYpos());
        assertFalse(player.getJumping());
        assertTrue(player.getFalling());
        assertEquals(0, player.getJumpTick());
        assertEquals(0, player.getAirTick());
    }

    @Test
    public void testSetXpos() {
        player.setXpos(6);
        assertEquals(6, player.getXpos());
        assertEquals(5, player.getYpos());
    }

    @Test
    public void testSetYpos() {
        player.setYpos(6);
        assertEquals(1, player.getXpos());
        assertEquals(6, player.getYpos());
    }




}