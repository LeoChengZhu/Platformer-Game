package model;

// A block that can be placed, intended to respawn player when touched
public class Death extends Blocks {

    // EFFECTS: constructs block with xpos and ypos according to input and block type of "Death"
    public Death(int xpos, int ypos) {
        super(xpos, ypos);
        type = "Death";
    }
}
