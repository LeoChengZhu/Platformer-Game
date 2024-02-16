package model;

// A block that can be placed, intended to represent an obstacle in the world
public class Tile extends Blocks {

    // EFFECTS: constructs block with xpos and ypos according to input and block type of "Tile"
    public Tile(int xpos, int ypos) {
        super(xpos, ypos);
        type = "Tile";
    }
}
