package ui;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//a class that represents a button used to save the game

public class SaveButton extends Buttons {

    // EFFECTS: Constructor
    public SaveButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }

    // EFFECTS: creates button, and adds it to parent
    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Save");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }

    // EFFECTS: adds listener for the button
    @Override
    protected void addListener() {
        button.addActionListener(new SaveButton.ClickHandler());
    }

    private class ClickHandler implements ActionListener {

        // EFFECTS: sets active button to this button
        @Override
        public void actionPerformed(ActionEvent e) {
            gameScreen.setActiveButton(SaveButton.this);
        }
    }
}
