package ui;


import javax.swing.*;
import java.awt.*;
//a class that represents a button used for placing empty blocks

public class EmptyButton extends BlockButton {

    // EFFECTS: Constructor
    public EmptyButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }

    // MODIFIES: gameScreen.getGame().getWorld()
    // EFFECTS: sets block containing point to "Empty"
    @Override
    public void alterBlockAt(Point point) {
        BlockShape shape = gameScreen.getBlockOnScreen(point);
        if (shape != null && !gameScreen.getGame().getSimulating()) {
            try {
                gameScreen.getGame().getWorld().setBlock("Empty",
                        shape.getXpos() / 20,
                        shape.getYpos() / 20);
            } catch (Exception e) {
                //do nothing
            }
        }
    }

    // EFFECTS: creates button, and adds it to parent
    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Empty");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }
}
