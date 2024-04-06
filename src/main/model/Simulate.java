package model;

import java.util.List;

//Manages the updating of the game world when the game state is set to simulating (playing)
public class Simulate {
    private Player player;
    private World gameWorld;
    private char input;
    private Boolean gameOver;

    // EFFECTS: constructs with game over being false
    public Simulate() {
        input = '0';
        gameOver = false;
    }

    // MODIFIES: this
    // EFFECTS: initializes gameWorld and player, and places player on top of Spawn in the game world
    public void initializeSimulation(World world) {
        this.gameWorld = world;
        List<Blocks> gameWorld = world.getWorld();
        for (Blocks block:gameWorld) {
            if (block.getType().equals("Spawn")) {
                this.player = new Player(block.getXpos(), block.getYpos() - 1);
            }
        }
        reflectPlayerToWorld();
    }

    // MODIFIES: this
    // EFFECTS: updates player positions according to block behaviours,
    //          reflect changes to the player into the world,
    //          remove player from world if game is over
    public void update() {
        updatePlayerPositions();
        reflectPlayerToWorld();
        if (gameOver) {
            removePlayer();
        }
    }

    // MODIFIES: this
    // EFFECTS: stores input
    public void keyEvent(char c) {
        input = c;
    }

    // MODIFIES: this
    // EFFECTS: updates player location in the world, and sets the blocks the player was
    //          previously travelling through with empty
    public void reflectPlayerToWorld() {
        gameWorld.getWorld().set(player.getXpos() + (player.getYpos() * gameWorld.getWidth()),player);
        EventLog.getInstance().logEvent(
                new Event("Set Player at " + player.getXpos() + ", " + player.getYpos()));
        for (int i = 0; i < gameWorld.getWorld().size(); i++) {
            if (gameWorld.getWorld().get(i).getType().equals("Player")
                    && i != (player.getXpos() + (player.getYpos() * gameWorld.getWidth()))) {
                try {
                    gameWorld.setBlock("Empty", i % gameWorld.getWidth(),
                            (i - (i % gameWorld.getWidth())) / gameWorld.getWidth());
                } catch (Exception e) {
                    //Can't cause exception
                }
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: updates player positions according to input, player state, and blocks, and resets stored input
    //          w -> sets jumping state of player to true
    //          a -> move left
    //          d -> move right
    //          o -> end game
    @SuppressWarnings("checkstyle:MethodLength")
    public void updatePlayerPositions() {
        switch (input) {
            case 'w':
                EventLog.getInstance().logEvent(new Event("w key pressed"));
                player.setJumping(true);
                break;

            case 'a':
                EventLog.getInstance().logEvent(new Event("a key pressed"));
                leftBehaviours();
                break;

            case 'd':
                EventLog.getInstance().logEvent(new Event("d key pressed"));
                rightBehaviours();
                break;

            case 'o':
                setGameOver();
        }
        if (player.getFalling()) {
            fallingBehaviours();
        }
        if (player.getJumping()) {
            jumpingBehaviours();
        }
        if (!player.getJumping() && !player.getFalling()) {
            neutralBehaviours();
        }

        this.input = '0';
    }

    // MODIFIES: this
    // EFFECTS: check for what is left of the player and performs one of the following:
    //          left is boundary -> do nothing
    //          left is "Empty" -> move left
    //          left is "Death" -> respawn player
    //          left is "End" -> game is over
    public void leftBehaviours() {
        player.setIsLeft(true);
        if (!(player.getXpos() - 1 < 0)) {
            if (gameWorld.getBlock(player.getXpos() - 1, player.getYpos()).getType().equals("Empty")) {
                player.moveLeft();
            } else if (gameWorld.getBlock(player.getXpos() - 1,
                    player.getYpos()).getType().equals("Death")) {
                respawn();
            } else if (gameWorld.getBlock(player.getXpos() - 1,
                    player.getYpos()).getType().equals("End")) {
                setGameOver();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: check for what is right of the player and performs one of the following:
    //          right is boundary -> do nothing
    //          right is "Empty" -> move right
    //          right is "Death" -> respawn player
    //          right is "End" -> game is over
    public void rightBehaviours() {
        player.setIsLeft(false);
        if (!(player.getXpos() + 1 >= gameWorld.getWidth())) {
            if (gameWorld.getBlock(player.getXpos() + 1, player.getYpos()).getType().equals("Empty")) {
                player.moveRight();
            } else if (gameWorld.getBlock(player.getXpos() + 1,
                    player.getYpos()).getType().equals("Death")) {
                respawn();
            } else if (gameWorld.getBlock(player.getXpos() + 1,
                    player.getYpos()).getType().equals("End")) {
                setGameOver();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: check for what is below the player is falling and performs one of the following:
    //              below is boundary -> no movement, player is now no longer falling
    //              below is "Tile" -> no movement, player is now no longer falling
    //              below is "Spawn" -> no movement, player is now no longer falling
    //              below is "Empty" -> player falls down
    //              below is "Death" -> respawn player, player is now no longer falling
    //              below is "End" -> game is over
    public void fallingBehaviours() {
        player.setIdleFrame(1);
        player.resetIdleChangeDelayTickTick();
        if (player.getYpos() + 1 >= gameWorld.getHeight()) {
            player.setFalling(false);
        } else if ((gameWorld.getBlock(player.getXpos(), player.getYpos() + 1).getType().equals("Tile"))) {
            player.setFalling(false);
        } else if ((gameWorld.getBlock(player.getXpos(), player.getYpos() + 1).getType().equals("Spawn"))) {
            player.setFalling(false);
        } else if ((gameWorld.getBlock(player.getXpos(), player.getYpos() + 1).getType().equals("Death"))) {
            respawn();
        } else if ((gameWorld.getBlock(player.getXpos(), player.getYpos() + 1).getType().equals("End"))) {
            setGameOver();
        } else {
            player.fall();
        }
    }

    // MODIFIES: this
    // EFFECTS: check for what is above the player is jumping and falling state and performs one of the following:
    //              above is boundary -> no movement, player hits head (air time decreases)
    //              above is "Tile" -> no movement, player hits head (air time decreases)
    //              above is "Spawn" -> no movement, player hits head (air time decreases)
    //              above is "Empty" and player is not falling -> player moves up
    //              above is "Death" -> respawn player, player is now no longer jumping
    //              above is "End" -> game is over
    public void jumpingBehaviours() {
        player.setIdleFrame(1);
        player.resetIdleChangeDelayTickTick();
        if (player.getYpos() - 1 < 0) {
            player.hitHead();
        } else if (gameWorld.getBlock(player.getXpos(), player.getYpos() - 1).getType().equals("Tile")) {
            player.hitHead();
        } else if (gameWorld.getBlock(player.getXpos(), player.getYpos() - 1).getType().equals("Death")) {
            respawn();
        } else if (gameWorld.getBlock(player.getXpos(), player.getYpos() - 1).getType().equals("Spawn")) {
            player.hitHead();
        } else if (gameWorld.getBlock(player.getXpos(), player.getYpos() - 1).getType().equals("End")) {
            setGameOver();
        } else if (!player.getFalling()) {
            player.jump();
        }
    }

    // MODIFIES: this
    // EFFECTS: check for what is below the player when player is
    //          not jumping or falling and performs one of the following:
    //              below is boundary -> do nothing
    //              below is "Empty" -> player is now falling
    //              below is "Death" -> respawn player
    //              below is "End" -> game is over
    public void neutralBehaviours() {
        if (!(player.getYpos() + 1 >= gameWorld.getHeight())) {
            if (gameWorld.getBlock(player.getXpos(), player.getYpos() + 1).getType().equals("Empty")) {
                player.setFalling(true);
            } else if (gameWorld.getBlock(player.getXpos(), player.getYpos() + 1).getType().equals("Death")) {
                respawn();
            } else if (gameWorld.getBlock(player.getXpos(), player.getYpos() + 1).getType().equals("End")) {
                setGameOver();
            }
        }
        if (player.getIdleChangeDelayTick() > 0) {
            player.idleChangeDelayTickTick();
        } else {
            if (player.getIdleFrame() == 1) {
                player.setIdleFrame(2);
            } else {
                player.setIdleFrame(1);
            }
            player.resetIdleChangeDelayTickTick();
        }
    }

    // MODIFIES: this
    // EFFECTS: move player back to spawn, and resets their jumping/falling state
    public void respawn() {
        for (Blocks block:gameWorld.getWorld()) {
            if (block.getType().equals("Spawn")) {
                player.setFalling(false);
                player.setJumping(false);
                player.setXpos(block.getXpos());
                player.setYpos(block.getYpos() - 1);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: set gameOver to true
    public void setGameOver() {
        gameOver = true;
        EventLog.getInstance().logEvent(
                new Event("No longer simulating..."));
    }

    // MODIFIES: this
    // EFFECTS: set where the player is to "Empty"
    public void removePlayer() {
        try {
            gameWorld.setBlock("Empty", player.getXpos(), player.getYpos());
            EventLog.getInstance().logEvent(
                    new Event("Player is removed"));
        } catch (Exception e) {
            //Can't cause exception
        }
    }

    // EFFECTS: returns whether the game is over
    public Boolean isGameOver() {
        return gameOver;
    }

    // EFFECTS: returns input
    public char getInput() {
        return input;
    }

    // EFFECTS: returns player
    public Player getPlayer() {
        return player;
    }

    // EFFECTS: returns gameWorld
    public World getGameWorld() {
        return gameWorld;
    }


}
