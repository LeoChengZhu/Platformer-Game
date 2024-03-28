package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReturnButton extends TitleButtons {


    public ReturnButton(TitleScreen titleScreen, JComponent parent) {
        super(titleScreen, parent);
    }


    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Return");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }

    @Override
    protected void addListener() {
        button.addActionListener(new ReturnButton.ClickHandler());
    }

    private class ClickHandler implements ActionListener {

        // EFFECTS: sets active button to this button
        @Override
        public void actionPerformed(ActionEvent e) {
            titleScreen.setActiveButton(ReturnButton.this);
        }
    }
}
