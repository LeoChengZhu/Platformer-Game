package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//a class that represents a button used to create the world

public class CreateWorldButton extends TitleButtons {

    // EFFECTS: Constructor
    public CreateWorldButton(TitleScreen titleScreen, JComponent parent) {
        super(titleScreen, parent);
    }

    // EFFECTS: creates button, and adds it to parent
    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Create World");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }

    // EFFECTS: adds listener for the button
    @Override
    protected void addListener() {
        button.addActionListener(new CreateWorldButton.ClickHandler());
    }

    private class ClickHandler implements ActionListener {

        // EFFECTS: sets active button to this button
        @Override
        public void actionPerformed(ActionEvent e) {
            titleScreen.setActiveButton(CreateWorldButton.this);
        }
    }
}
