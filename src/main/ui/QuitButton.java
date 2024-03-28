package ui;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//a class that represents a button used for quitting the game

public class QuitButton extends Buttons {

    // EFFECTS: Constructor
    public QuitButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }


    // EFFECTS: creates button, and adds it to parent
    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Quit");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }

    // EFFECTS: adds listener for the button
    @Override
    protected void addListener() {
        button.addActionListener(new QuitButton.ClickHandler());
    }

    private class ClickHandler implements ActionListener {

        // EFFECTS: sets active button to this button
        @Override
        public void actionPerformed(ActionEvent e) {
            gameScreen.setActiveButton(QuitButton.this);
        }
    }
}
