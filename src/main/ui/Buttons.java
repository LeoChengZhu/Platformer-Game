package ui;

import javax.swing.*;
import java.awt.event.MouseEvent;

// a class that represents buttons created for the game
public abstract class Buttons {
    protected JButton button;
    protected GameScreen gameScreen;
    private boolean active;

    // EFFECTS: Constructor
    public Buttons(GameScreen gameScreen, JComponent parent) {
        this.gameScreen = gameScreen;
        createButton(parent);
        addToParent(parent);
        active = false;
        addListener();
    }

    // MODIFIES: this
    // EFFECTS:  customizes the button
    protected JButton customizeButton(JButton button) {
        button.setBorderPainted(true);
        button.setFocusPainted(true);
        button.setContentAreaFilled(true);
        return button;
    }

    // EFFECTS: creates button
    protected abstract void createButton(JComponent parent);

    // MODIFIES: parent
    // EFFECTS:  adds the given button to the parent component
    public void addToParent(JComponent parent) {
        parent.add(button);
    }

    // EFFECTS: adds a listener for this button
    protected abstract void addListener();

    // EFFECTS: returns active
    public boolean isActive() {
        return active;
    }

    // EFFECTS: sets this button's active field to true
    public void activate() {
        active = true;
    }

    // EFFECTS: sets this button's active field to false
    public void deactivate() {
        active = false;
    }

    // EFFECTS: default behaviour does nothing
    public void mousePressed(MouseEvent e) {

    }

    // EFFECTS: default behaviour does nothing
    public void mouseReleased(MouseEvent e) {

    }

    // EFFECTS: default behaviour does nothing
    public void mouseClicked(MouseEvent e) {

    }

    // EFFECTS: default behaviour does nothing
    public void mouseDragged(MouseEvent e) {

    }
}
