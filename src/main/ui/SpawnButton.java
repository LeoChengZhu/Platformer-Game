package ui;


import javax.swing.*;
import java.awt.*;
//a class that represents a button used for placing spawn blocks

public class SpawnButton extends BlockButton {

    // EFFECTS: Constructor
    public SpawnButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }

    // MODIFIES: gameScreen.getGame().getWorld()
    // EFFECTS: sets block containing point to "Spawn"
    @Override
    public void alterBlockAt(Point point) {
        BlockShape shape = gameScreen.getBlockOnScreen(point);
        if (shape != null && !gameScreen.getGame().getSimulating()) {
            try {
                gameScreen.getGame().getWorld().setBlock("Spawn",
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
        button = new JButton("Spawn");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }
}
