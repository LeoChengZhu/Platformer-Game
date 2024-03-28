package ui;


import javax.swing.*;
import java.awt.*;

public class DeathButton extends BlockButton {

    public DeathButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }

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

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Death");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }
}
