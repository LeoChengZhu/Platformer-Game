package ui;

import jdk.nio.Channels;
import model.World;
import persistence.JsonReader;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static javax.swing.SwingConstants.CENTER;

public class TitleScreen extends JFrame {
    public static final int INITIALWIDTH = 1000;
    public static final int INITIALHEIGHT = 700;
    public static final int INITIALWORLDWIDTH = 17;
    public static final int INITIALWORLDHEIGHT = 17;
    private JsonReader jsonReader;
    private static final String STORE = "./data/save.json";
    private TitleButtons activeButton;
    private JPanel startOptions;
    private JPanel createOptions;
    private JLabel widthLabel;
    private JLabel heightLabel;
    private int width;
    private int height;

    public TitleScreen() {
        super();
        jsonReader = new JsonReader(STORE);
        initializeScreen();
        initializeTitleScreen();
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

    private void initializeWorldCreationPanel() {
        createOptions = new JPanel();
        createOptions.setLayout(new GridLayout(3, 3));
        createOptions.setSize(new Dimension(0, 0));
        add(createOptions, BorderLayout.CENTER);
    }

    private void initializeWorldCreationLabels() {
        widthLabel = new JLabel();
        heightLabel = new JLabel();
        widthLabel.setSize(20,20);
        heightLabel.setSize(20,20);
        widthLabel.setHorizontalAlignment(CENTER);
        heightLabel.setHorizontalAlignment(CENTER);
    }

    private void initializeWorldCreation() {
        width = INITIALWORLDWIDTH;
        height = INITIALWORLDHEIGHT;
        initializeWorldCreationPanel();
        initializeWorldCreationLabels();
        JSlider widthSlider = new JSlider(JSlider.VERTICAL, 2, 32, 17);
        JSlider heightSlider = new JSlider(JSlider.VERTICAL, 2, 32, 17);
        widthLabel.setText("Width: " + width);
        heightLabel.setText("Height: " + height);
        widthSlider.addChangeListener(new WidthSlider());
        heightSlider.addChangeListener(new HeightSlider());
        createOptions.add(widthLabel);
        createOptions.add(heightLabel);
        createOptions.add(widthSlider);
        createOptions.add(heightSlider);
        widthSlider.setMinorTickSpacing(1);
        widthSlider.setPaintTicks(true);
        widthSlider.setPaintLabels(true);
        heightSlider.setMinorTickSpacing(1);
        heightSlider.setPaintTicks(true);
        heightSlider.setPaintLabels(true);
        TitleButtons returnButton = new ReturnButton(this, createOptions);
        TitleButtons createWorldButton = new CreateWorldButton(this, createOptions);
    }

    // MODIFIES: this
    // EFFECTS:  sets the given Button as the activeButton
    public void setActiveButton(TitleButtons button) {
        if (activeButton != null) {
            activeButton.deactivate();
        }
        button.activate();
        activeButton = button;

        if (button instanceof LoadButton) {
            loadGame();
        }

        if (button instanceof NewWorldButton) {
            this.remove(startOptions);
            initializeWorldCreation();
            validate();
        }

        if (button instanceof ReturnButton) {
            this.remove(createOptions);
            initializeTitleScreen();
            validate();
        }

        if (button instanceof CreateWorldButton) {
            newGame();
        }
    }

    public void initializeTitleScreen() {
        startOptions = new JPanel();
        startOptions.setLayout(new GridLayout(0, 1));
        startOptions.setSize(new Dimension(0, 0));
        add(startOptions, BorderLayout.CENTER);
        TitleButtons newWorldButton = new NewWorldButton(this, startOptions);
        TitleButtons loadButton = new LoadButton(this, startOptions);
        validate();
    }

    public void loadGame() {
        try {
            System.out.println("Loaded world from " + STORE);
            dispose();
            JFrame gameScreen = new GameScreen(jsonReader.read("name"));
        } catch (Exception e) {
            System.err.println("Cannot read");
        }
    }

    public void newGame() {
        try {
            dispose();
            JFrame gameScreen = new GameScreen(new World(this.width, this.height));
        } catch (Exception e) {
            //does not occur
        }
    }

    private class WidthSlider implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();
            width = source.getValue();
            widthLabel.setText("Width: " + width);

        }
    }

    private class HeightSlider implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();
            height = source.getValue();
            heightLabel.setText("Height: " + height);
        }
    }
}
