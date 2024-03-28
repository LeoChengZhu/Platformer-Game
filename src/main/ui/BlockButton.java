package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public abstract class BlockButton extends Buttons {

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

        // EFFECTS: sets active tool to the PlayShape tool
        //          called by the framework when the tool is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            gameScreen.setActiveButton(BlockButton.this);
        }
    }

}
