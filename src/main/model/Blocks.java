package model;

// Abstract class defined to make all objects that can be placed to be homogenous
public abstract class Blocks {
    protected int xpos;
    protected int ypos;
    protected String type;

    // EFFECTS: constructs xpos and ypos according to input
    public Blocks(int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    // EFFECTS: returns xpos
    public int getXpos() {
        return xpos;
    }

    // EFFECTS: returns ypos
    public int getYpos() {
        return ypos;
    }

    // EFFECTS: returns block type
    public String getType() {
        return type;
    }



}
