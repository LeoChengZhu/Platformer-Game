package model;

// A block that can be placed, intended to serve as a spawn point for the player
public class Spawn extends Blocks {

    // EFFECTS: constructs block with xpos and ypos according to input and block type of "Spawn"
    public Spawn(int xpos, int ypos) {
        super(xpos, ypos);
        type = "Spawn";
    }
}
