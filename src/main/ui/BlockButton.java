package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

// a class that represents buttons used to add blocks into world
public abstract class BlockButton extends Buttons {

    // EFFECTS: Constructor
    public BlockButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }

    // EFFECTS: selects the figure containing point of mouse press
    @Override
    public void mousePressed(MouseEvent e)  {
        alterBlockAt(e.getPoint());
    }

    // EFFECTS: selects the figure containing point of mouse drag
    @Override
    public void mouseDragged(MouseEvent e)  {
        alterBlockAt(e.getPoint());
    }

    // MODIFIES: this
    // EFFECTS:  sets the activeTool in button to this when clicked
    @Override
    protected void addListener() {
        button.addActionListener(new ClickHandler());
    }

    public abstract void alterBlockAt(Point point);

    private class ClickHandler implements ActionListener {

        // EFFECTS: sets active button to this button
        @Override
        public void actionPerformed(ActionEvent e) {
            gameScreen.setActiveButton(BlockButton.this);
        }
    }

}
