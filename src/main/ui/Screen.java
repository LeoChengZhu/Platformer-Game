package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
//Visual of the combination of BlockShapes, used to display the whole World
//Code adapted from SimpleDrawingPlayer

public class Screen extends JPanel {

    private List<BlockShape> shapes;

    public Screen() {
        super();
        shapes = new ArrayList<>();
        setBackground(Color.white);
    }

    public List<BlockShape> getShapes() {
        return this.shapes;
    }

    public boolean containsShape(BlockShape s) {
        return shapes.contains(s);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (BlockShape shape : shapes) {
            shape.draw(g);
        }
    }

    public void setShapes(List<BlockShape> shapes) {
        this.shapes = shapes;
    }

    // EFFECTS: returns the Shape at a given Point in Screen, if any
    public BlockShape getBlockAtPoint(Point point) {
        for (BlockShape shape : shapes) {
            if (shape.contains(point)) {
                return shape;
            }
        }
        return null;
    }
}
