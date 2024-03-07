package ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.Game;
import model.World;

import java.io.IOException;

// Creates and displays the game world depending on the input
// This class is partially adapted from code from the Puppy lab
public class WorldMaker {
    private static final int INTERVAL = 40;
    private static final int MAXHEIGHT = 32;
    private static final int MAXWIDTH = 32;
    private Screen screen;
    private Game game;
    private int width;
    private int height;

    // EFFECTS: constructs the screen and game model according to specified width and height,
    //          screen will always be above a certain width and height even if the game itself is a smaller.
    public WorldMaker(int width, int height) throws IOException, InterruptedException {
        this.width = width;
        this.height = height;
        if (width > MAXWIDTH) {
            this.width = MAXWIDTH;
        }
        if (height > MAXHEIGHT) {
            this.height = MAXHEIGHT;
        }
        if (height < 5) {
            this.height = 5;
        }
        if (width < 20) {
            screen = new DefaultTerminalFactory().setPreferTerminalEmulator(false)
                        .setInitialTerminalSize(new TerminalSize(2 * 20, this.height + 3)).createScreen();
        } else {
            screen = new DefaultTerminalFactory().setPreferTerminalEmulator(false)
                        .setInitialTerminalSize(new TerminalSize(2 * this.width, this.height + 3)).createScreen();
        }
        screen.startScreen();
        screen.setCursorPosition(null);

        game = new Game();
        game.createWorld(width, height);
        startTicks();
    }

    // EFFECTS: constructs the screen and game model according to specified world width and height,
    //          screen will always be above a certain width and height even if the world itself is smaller.
    public WorldMaker(World world) throws IOException, InterruptedException {
        this.height = world.getHeight();
        this.width = world.getWidth();
        if (width > MAXWIDTH) {
            this.width = MAXWIDTH;
        }
        if (height > MAXHEIGHT) {
            this.height = MAXHEIGHT;
        }
        if (height < 5) {
            this.height = 5;
        }
        if (width < 20) {
            screen = new DefaultTerminalFactory().setPreferTerminalEmulator(false)
                    .setInitialTerminalSize(new TerminalSize(2 * 20, this.height + 3)).createScreen();
        } else {
            screen = new DefaultTerminalFactory().setPreferTerminalEmulator(false)
                    .setInitialTerminalSize(new TerminalSize(2 * this.width, this.height + 3)).createScreen();
        }
        screen.startScreen();
        screen.setCursorPosition(null);

        game = new Game();
        game.setWorld(world);
        startTicks();
    }

    // MODIFIES: this
    // EFFECTS: updates game with- tick()
    private void startTicks() throws IOException, InterruptedException {
        Boolean isOver = game.isOver();
        while (!isOver) {
            tick();
            isOver = game.isOver();
            Thread.sleep(1000L / INTERVAL);
        }
        System.out.println("Exiting program...");
        System.exit(0);
    }

    // MODIFIES: this
    // EFFECTS: handles input, updates game according to input, and reflects update onto screen
    private void tick() throws IOException {
        handleInput();
        game.update();

        screen.clear();
        render();
        screen.refresh();
    }

    // MODIFIES: this
    // EFFECTS: displays game world and current input onto the screen.
    private void render() {
        String world = game.getGameWorld();
        String input = game.getInput();
        int i = 0;
        int x = 0;
        int y = 0;
        for (char c:world.toCharArray()) {
            screen.setCharacter(2 * x, y, new TextCharacter(c));
            if (x == width - 1) {
                y++;
                x = 0;
            } else {
                x++;
            }
        }
        for (char c: input.toCharArray()) {
            screen.setCharacter(i, height + 1, new TextCharacter(c));
            i++;
        }
    }

    //EFFECTS: passes input into game model, catches any exceptions thrown by the handle input in game.
    private void handleInput() throws IOException {
        KeyStroke stroke = screen.pollInput();

        if (stroke == null || stroke.getCharacter() == null) {
            return;
        }
        char c = stroke.getCharacter();
        try {
            game.handleInput(c);
        } catch (Exception e) {
            System.err.println("Illegal input");
        }
    }



    // EFFECTS: starts program
    public static void main(String[] args) throws Exception {
        new Initiate();
    }

}
