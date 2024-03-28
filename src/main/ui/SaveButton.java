package ui;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveButton extends Buttons {

    public SaveButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }


    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Save");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }

    @Override
    protected void addListener() {
        button.addActionListener(new SaveButton.ClickHandler());
    }

    private class ClickHandler implements ActionListener {

        // EFFECTS: sets active tool to the PlayShape tool
        //          called by the framework when the tool is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            gameScreen.setActiveButton(SaveButton.this);
        }
    }
}
