package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//Visual of the combination of BlockShapes, used to display the whole World
//Code adapted from SimpleDrawingPlayer

public class Screen extends JPanel {

    private List<BlockShape> shapes;

    // EFFECTS: Constructor
    public Screen() {
        super();
        shapes = new ArrayList<>();
        setBackground(Color.white);
    }

    // EFFECTS: returns shapes
    public List<BlockShape> getShapes() {
        return this.shapes;
    }

    // EFFECTS: returns whether shapes contains input
    public boolean containsShape(BlockShape s) {
        return shapes.contains(s);
    }

    // EFFECTS: draws each shape in shape onto graphics component
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (BlockShape shape : shapes) {
            shape.draw(g);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets shapes to input
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
