package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//a class that represents a button used to create a new world

public class NewWorldButton extends TitleButtons {

    // EFFECTS: Constructor
    public NewWorldButton(TitleScreen titleScreen, JComponent parent) {
        super(titleScreen, parent);
    }

    // EFFECTS: creates button, and adds it to parent
    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("New World");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }

    // EFFECTS: adds listener for the button
    @Override
    protected void addListener() {
        button.addActionListener(new NewWorldButton.ClickHandler());
    }

    private class ClickHandler implements ActionListener {

        // EFFECTS: sets active button to this button
        @Override
        public void actionPerformed(ActionEvent e) {
            titleScreen.setActiveButton(NewWorldButton.this);
        }
    }
}
