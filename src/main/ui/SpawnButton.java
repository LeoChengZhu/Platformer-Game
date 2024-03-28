package ui;


import javax.swing.*;
import java.awt.*;

public class SpawnButton extends BlockButton {

    public SpawnButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }

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

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Spawn");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }
}
