package ui;


import javax.swing.*;
import java.awt.*;
//a class that represents a button used for placing death blocks

public class DeathButton extends BlockButton {

    // EFFECTS: Constructor
    public DeathButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }

    // MODIFIES: gameScreen.getGame().getWorld()
    // EFFECTS: sets block containing point to "Death"
    @Override
    public void alterBlockAt(Point point) {
        BlockShape shape = gameScreen.getBlockOnScreen(point);
        if (shape != null && !gameScreen.getGame().getSimulating()) {
            try {
                gameScreen.getGame().getWorld().setBlock("Death",
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
        button = new JButton("Death");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }
}
