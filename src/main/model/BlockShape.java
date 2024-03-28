package model;

import java.awt.*;
import java.util.Objects;

//Visual component of each block in world
//Code partially adapted from SimpleDrawingPlayer

public class BlockShape {
    private int xpos;
    private int ypos;
    private static final int SIDE = 20;
    private Color color;

    public BlockShape(int x, int y, Color color) {
        this.xpos = x;
        this.ypos = y;
        this.color = color;
    }

    public void draw(Graphics g) {
        g.fillRect(xpos, ypos, SIDE, SIDE);
        g.setColor(color);
        g.fillRect(xpos, ypos, SIDE, SIDE);
    }

    // EFFECTS: return true iff the given x value is within the bounds of the Shape
    public boolean containsX(int x) {
        return (this.xpos <= x) && (x <= this.xpos + SIDE);
    }

    // EFFECTS: return true iff the given y value is within the bounds of the Shape
    public boolean containsY(int y) {
        return (this.ypos <= y) && (y <= this.ypos + SIDE);
    }

    // EFFECTS: return true if the given Point (x,y) is contained within the bounds of this Shape
    public boolean contains(Point point) {
        return containsX(point.x) && containsY(point.y);
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public Color getColor() {
        return color;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockShape)) {
            return false;
        }
        BlockShape that = (BlockShape) o;
        return xpos == that.xpos && ypos == that.ypos
                && that.color.getRed() == this.color.getRed()
                && that.color.getGreen() == this.color.getGreen()
                && that.color.getBlue() == this.color.getBlue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(xpos, ypos);
    }
}
