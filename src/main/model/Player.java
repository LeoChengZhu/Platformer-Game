package model;

// class representing a player which extends off of Blocks,
// methods changes the player state and player coordinates.
// according to input and blocks around the player
public class Player extends Blocks {

    private boolean falling;
    private boolean jumping;
    private int jumpTick;
    private int airTick;

    // EFFECTS: constructs player with xpos, and ypos,
    //          sets falling and jumping to false
    //          initializes jumpTick and airTick,
    //          sets type to "Player"
    public Player(int xpos, int ypos) {
        super(xpos, ypos);
        falling = false;
        jumping = false;
        jumpTick = 2;
        airTick = 10;
        type = "Player";
    }

    // MODIFIES: this
    // EFFECTS: player xpos - 1 (player moves left)
    public void moveLeft() {
        xpos--;
    }

    // MODIFIES: this
    // EFFECTS: player xpos + 1 (player moves right)
    public void moveRight() {
        xpos++;
    }

    // MODIFIES: this
    // EFFECTS: sets jumping to inputted state
    public void setJumping(Boolean state) {
        jumping = state;
    }

    // MODIFIES: this
    // EFFECTS: when jumpTick > 0, player moves up, jump tick is reduced,
    //          if jumpTick is not > 0, air tick is reduced
    //          if airTick is not > 0 && jumpTick is not > 0, the player is no longer jumping and is now falling
    public void jump() {
        if (jumpTick > 0) {
            ypos--;
            jumpTick--;
        } else {
            airTick--;
            if (airTick <= 0) {
                jumping = false;
                falling = true;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: when jumpTick > 0, jump tick is reduced,
    //          if jumpTick is not > 0, air tick is reduced
    //          if airTick is not > 0 && jumpTick is not > 0, the player is no longer jumping and is now falling
    public void hitHead() {
        if (jumpTick > 0) {
            jumpTick--;
        } else {
            airTick--;
            if (airTick <= 0) {
                jumping = false;
                falling = true;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: player ypos + 1 (player moves down)
    public void fall() {
        this.ypos++;
    }

    // MODIFIES: this
    // EFFECTS: sets jumping to inputted state,
    //          if set to false, reset airTick and jumpTick
    public void setFalling(boolean state) {
        falling = state;
        if (!state) {
            airTick = 10;
            jumpTick = 2;
        }
    }

    // EFFECTS: returns whether the player is falling
    public Boolean getFalling() {
        return falling;
    }

    // EFFECTS: returns whether the player is jumping
    public Boolean getJumping() {
        return jumping;
    }

    // MODIFIES: this
    // EFFECTS: sets player xpos to input
    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    // MODIFIES: this
    // EFFECTS: sets player ypos to input
    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    // EFFECTS: returns the jumpTick
    public int getJumpTick() {
        return jumpTick;
    }

    // EFFECTS: returns the airTick
    public int getAirTick() {
        return airTick;
    }
}
