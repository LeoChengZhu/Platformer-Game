package ui;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayButton extends Buttons {

    public PlayButton(GameScreen gameScreen, JComponent parent) {
        super(gameScreen, parent);
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Play");
        button = customizeButton(button);
        addToParent(parent);
        button.setFocusable(false);
    }

    @Override
    protected void addListener() {
        button.addActionListener(new PlayButton.ClickHandler());
    }

    private class ClickHandler implements ActionListener {

        // EFFECTS: sets active tool to the PlayShape tool
        //          called by the framework when the tool is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            gameScreen.setActiveButton(PlayButton.this);
        }
    }
}
