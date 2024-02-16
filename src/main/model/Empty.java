package model;

// A block that can be placed, intended to represent an empty space
public class Empty extends Blocks {

    // EFFECTS: constructs block with xpos and ypos according to input and block type of "Empty"
    public Empty(int xpos, int ypos) {
        super(xpos, ypos);
        type = "Empty";
    }
}
