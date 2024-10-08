package ui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

// a class that represents the screen when the game is running

public class GameScreen extends JFrame {
    public static final int INITIALWIDTH = 1000;
    public static final int INITIALHEIGHT = 700;
    private static final int INTERVAL = 25;
    private Game game;
    private Screen currentScreen;
    private Buttons activeButton;
    private JPanel buttonArea;

    // EFFECTS: Constructor
    public GameScreen(World world) throws IOException, InterruptedException {
        super();
        game = new Game();
        game.setWorld(world);
        initializeScreen();
        initialize();
        startTicks();
    }

    // MODIFIES: this
    // EFFECTS: initializes graphics components and adds listeners
    public void initialize() {
        initializeGraphics();
        initializeInteraction();
    }

    // MODIFIES: this
    // EFFECTS: updates game with tick()
    private void startTicks() throws IOException, InterruptedException {
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    tick();
                    if (!game.getSimulating()) {
                        add(buttonArea, BorderLayout.SOUTH);
                        resizeWindow(1000, 700 - 28);
                        validate();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        };
        new Timer(INTERVAL, taskPerformer).start();
    }


    // MODIFIES: this
    // EFFECTS: updates game, and reflects update onto screen
    private void tick() throws IOException {
        game.update();
        updateScreen();
    }

    // EFFECTS: returns game
    public Game getGame() {
        return game;
    }


    // MODIFIES: this
    // EFFECTS:  draws the JFrame window where the game will operate
    private void initializeScreen() {
        setLayout(new BorderLayout());
        setSize(new Dimension(INITIALWIDTH, INITIALHEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        validate();
    }

    // MODIFIES: this
    // EFFECTS: initializes graphic components
    private void initializeGraphics() {
        addNewScreen();
        buttonArea = new JPanel();
        buttonArea.setLayout(new GridLayout(0,2));
        buttonArea.setSize(new Dimension(0, 0));
        add(buttonArea, BorderLayout.SOUTH);
        createButtons();
        validate();
    }

    // MODIFIES: this
    // EFFECTS:  initializes a KeyHandler and a MouseListener to be used in the JFrame
    private void initializeInteraction() {
        KeyHandler kh = new KeyHandler();
        WindowHandler wh = new WindowHandler();
        addKeyListener(kh);
        MouseListener ml = new MouseListener();
        addMouseListener(ml);
        addMouseMotionListener(ml);
        addWindowListener(wh);
    }

    // MODIFIES: this
    // EFFECTS:  a helper method which declares and instantiates all buttons
    private void createButtons() {
        Buttons emptyButton = new EmptyButton(this, buttonArea);
        Buttons tileButton = new TileButton(this, buttonArea);
        Buttons spawnButton = new SpawnButton(this, buttonArea);
        Buttons endButton = new EndButton(this, buttonArea);
        Buttons deathButton = new DeathButton(this, buttonArea);
        Buttons playButton = new PlayButton(this, buttonArea);
        Buttons saveButton = new SaveButton(this, buttonArea);
        Buttons quitButton = new QuitButton(this, buttonArea);

    }

    // MODIFIES: this
    // EFFECTS: creates new Screen
    private void addNewScreen() {
        Screen newScreen = new Screen();
        currentScreen = newScreen;
        currentScreen.setShapes(game.getGameWorld());
        add(newScreen, BorderLayout.CENTER);
        validate();
    }

    // MODIFIES: this
    // EFFECTS: updates current screen with updates in world
    private void updateScreen() {
        currentScreen.setShapes(game.getGameWorld());
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: resizes window according to input
    private void resizeWindow(int width, int height) {
        setSize(new Dimension(width, height + 28));
    }

    // EFFECTS: if activeButton != null, then mousePressed is invoked on activeButton, depends on the
    //          type of the button which is currently active
    private void handleMousePressed(MouseEvent e)  {
        if (activeButton != null) {
            activeButton.mousePressed(e);
        }
    }

    // EFFECTS: if activeButton != null, then mouseReleased is invoked on activeButton, depends on the
    //          type of the button which is currently active
    private void handleMouseReleased(MouseEvent e) {
        if (activeButton != null) {
            activeButton.mouseReleased(e);
        }
    }

    // EFFECTS: if activeButton != null, then mouseClicked is invoked on activeButton, depends on the
    //          type of the button which is currently active
    private void handleMouseClicked(MouseEvent e) {
        if (activeButton != null) {
            activeButton.mouseClicked(e);
        }
    }

    // EFFECTS: if activeButton != null, then mouseDragged is invoked on activeButton, depends on the
    //          type of the button which is currently active
    private void handleMouseDragged(MouseEvent e) {
        if (activeButton != null) {
            activeButton.mouseDragged(e);
        }
    }

    // EFFECTS: return BlockShaoe at given point at the currentScreen
    public BlockShape getBlockOnScreen(Point point) {
        return currentScreen.getBlockAtPoint(point);
    }

    // MODIFIES: this
    // EFFECTS: sets the given Button as the activeButton, and perform different actions,
    //          depending on which button is clicked(set active)
    public void setActiveButton(Buttons button) {
        if (activeButton != null) {
            activeButton.deactivate();
        }
        button.activate();
        activeButton = button;
        if (button instanceof PlayButton) {
            game.play();
            if (getGame().getSimulating()) {
                playScreen();
            }
        }
        if (button instanceof SaveButton) {
            game.save("name");
        }
        if (button instanceof QuitButton) {
            game.save("name");
            game.makeGameOver();
            dispose();
            new TitleScreen();
        }

    }

    // MODIFIES: this
    // EFFECTS: removes the buttons from the screen
    public void playScreen() {
        this.remove(buttonArea);
        resizeWindow(game.getWorld().getWidth() * 20,
                game.getWorld().getHeight() * 20);
        validate();
    }


    private class KeyHandler implements KeyListener {
        // EFFECTS: forward key event into game
        @Override
        public void keyTyped(KeyEvent e) {
            try {
                game.handleInput(e.getKeyChar());
            } catch (Exception ex) {
                //not needed
            }
        }

        // EFFECTS: do nothing
        @Override
        public void keyPressed(KeyEvent e) {
        }

        // EFFECTS: do nothing
        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private class MouseListener extends MouseAdapter {

        // EFFECTS: Forward mouse pressed event to the active button
        public void mousePressed(MouseEvent e) {
            handleMousePressed(translateEvent(e));
        }

        // EFFECTS: Forward mouse released event to the active button
        public void mouseReleased(MouseEvent e) {
            handleMouseReleased(translateEvent(e));
        }

        // EFFECTS:Forward mouse clicked event to the active button
        public void mouseClicked(MouseEvent e) {
            handleMouseClicked(translateEvent(e));
        }

        // EFFECTS:Forward mouse dragged event to the active button
        public void mouseDragged(MouseEvent e) {
            handleMouseDragged(translateEvent(e));
        }

        // EFFECTS: translates the mouse event to current drawing's coordinate system
        private MouseEvent translateEvent(MouseEvent e) {
            return SwingUtilities.convertMouseEvent(e.getComponent(), e, currentScreen);
        }
    }

    private class WindowHandler implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            EventLog.getInstance().printLog();
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}
