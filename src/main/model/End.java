package model;

// A block that can be placed, intended to end game when touched
public class End extends Blocks {

    // EFFECTS: constructs block with xpos and ypos according to input and block type of "End"
    public End(int xpos, int ypos) {
        super(xpos, ypos);
        type = "End";
    }
}
