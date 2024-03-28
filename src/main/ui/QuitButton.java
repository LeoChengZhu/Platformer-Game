package ui;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuitButton extends Buttons {

    public QuitButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }


    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Quit");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }

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
