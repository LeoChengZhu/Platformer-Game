package model;

import persistence.JsonWriter;
import ui.BlockShape;
import ui.PlayerShape;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isISOControl;

// Initializes and changes game world according to input from ui, then returns the updated
// game world back to ui to be displayed.
public class Game {

    private World world;
    private Boolean simulating;
    private Simulate simulate;
    private String input;
    private static final String STORE = "./data/save.json";
    private JsonWriter jsonWriter;
    private Boolean over;

    // EFFECTS: constructs game in create mode with no input
    public Game() {
        simulating = false;
        input = "";
        jsonWriter = new JsonWriter(STORE);
        over = false;
        simulate = new Simulate();
    }

    // MODIFIES: this
    // EFFECTS: initializes game world
    public void createWorld(int width, int height) {
        world = new World(width, height);
    }

    // MODIFIES: this
    // EFFECTS: sets world
    public void setWorld(World world) {
        this.world = world;
    }

    // MODIFIES: this
    // EFFECTS: updates game when playing the game (not in creation mode), switch back
    //          to create mode when game is over.
    public void update() {
        if (simulating) {
            simulate.update();
            if (simulate.isGameOver()) {
                stop();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: handles key event when playing the game and stores input to be handled
    //          Simulating:
    //              passes input character into simulate.keyEvent()
    //          Not simulating:
    //              backspace && input length > 0 -> remove last character from inout
    //              return && input length > 0 -> handles current input and clears it
    //              not control character -> store input
    // throws exception when handleCreateCommand throws exception
    public void handleInput(char c) throws
            SpawnAlreadyExistsException, IllegalSpawnException,
            FillWithSpawnException, BlockAboveSpawnException {
        if (simulating) {
            simulate.keyEvent(c);
        } else {
            if (c == '\b' && input.length() > 0) {
                input = input.substring(0, input.length() - 1);
            } else if (c == '\n' && input.length() > 0) {
                try {
                    handleCreateCommand(input);
                } catch (Exception e) {
                    throw e;
                } finally {
                    this.input = "";
                }
            } else if (!isISOControl(c)) {
                input = input + c;
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: apply different effects to game world depending on input:
    //          input length 1:
    //                  play -> sets mode to simulating(playing)
    //                  save -> saves current world to JSON
    //                  quit -> saves current world to JSON and exits game
    //          input length 4:
    //                  place [Blocks] [x] [y] -> places block at x y starting at (0, 0)
    //                                            [Blocks] -> "Tile", "Empty", "Death", "End", "Spawn"
    //          input length 6:
    //                  fill [Blocks] [start x] [start y] [end x] [end y]
    //                         -> fills area contained by the two x, y coordinates, start x, y is top left of the area,
    //                            end x, y is bottom right of the area.
    //                  [Blocks] -> "Tile", "Empty", "Death", "End"
    // throw exception if an exception is caught from methods from World
    @SuppressWarnings("methodlength")
    public void handleCreateCommand(String command)
            throws NumberFormatException, SpawnAlreadyExistsException, IllegalSpawnException,
            FillWithSpawnException, BlockAboveSpawnException {
        String[] commandStrings = command.split(" ");
        try {
            switch (commandStrings.length) {
                case 1:
                    if (commandStrings[0].equals("play")) {
                        play();
                    }
                    if (commandStrings[0].equals("save")) {
                        save("name");
                    }
                    if (commandStrings[0].equals("quit")) {
                        save("name");
                        makeGameOver();
                    }
                    break;
                case 4:
                    if (commandStrings[0].equals("set")) {
                        world.setBlock(commandStrings[1], Integer.parseInt(commandStrings[2]),
                                Integer.parseInt(commandStrings[3]));
                    }
                    break;
                case 6:
                    if (commandStrings[0].equals("fill")) {
                        world.fill(commandStrings[1], Integer.parseInt(commandStrings[2]),
                                Integer.parseInt(commandStrings[3]),
                                Integer.parseInt(commandStrings[4]), Integer.parseInt(commandStrings[5]));
                    }
                    break;
            }
        } catch (Exception e) {
            throw e;
        }
    }


    // EFFECTS: returns game world as a list of BlockShapes with different colors representing different blocks:
    @SuppressWarnings("methodlength")
    public List<BlockShape> getGameWorld() {
        List<Blocks> gameWorld = world.getWorld();
        List<BlockShape> worldShapes = new ArrayList<>();
        for (Blocks object:gameWorld) {
            switch (object.getType()) {
                case "Tile":
                    worldShapes.add(new BlockShape(
                            object.getXpos() * 20,
                            object.getYpos()  * 20,
                            new Color(135, 62, 35)));
                    break;
                case "Empty":
                    worldShapes.add(emptyVariations(object));
                    break;
                case "Spawn":
                    worldShapes.add(new BlockShape(
                            object.getXpos() * 20,
                            object.getYpos()  * 20,
                            new Color(0, 255, 0)));
                    break;
                case "End":
                    worldShapes.add(new BlockShape(
                            object.getXpos() * 20,
                            object.getYpos()  * 20,
                            new Color(255, 255, 0)));
                    break;
                case "Death":
                    worldShapes.add(new BlockShape(
                            object.getXpos() * 20,
                            object.getYpos()  * 20,
                            new Color(255, 0, 0)));
                    break;
                case "Player":
                    worldShapes.add(new PlayerShape(
                            object.getXpos() * 20,
                            object.getYpos()  * 20,
                            new Color(0, 0, 0), (Player) object));
                    break;
            }
        }
        return worldShapes;
    }



    // EFFECTS: returns BlockShape representing "Empty" depending on the game state
    public BlockShape emptyVariations(Blocks block) {
        if (simulating) {
            return new BlockShape(
                    block.getXpos() * 20,
                    block.getYpos()  * 20,
                    new Color(255, 255, 255));
        }
        return new BlockShape(
                block.getXpos() * 20,
                block.getYpos()  * 20,
                new Color(190,223,228));
    }

    // MODIFIES: this
    // EFFECTS: initializes simulate, turns game state into simulating (playing)
    public void play() {
        if (checkForSpawn()) {
            simulating = true;
            simulate.initializeSimulation(world);
        }
    }

    // EFFECTS: returns whether a "Spawn" block has been placed
    public Boolean checkForSpawn() {
        for (Blocks block:world.getWorld()) {
            if (block.getType().equals("Spawn")) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: saves the world to file
    public void save(String name) {
        try {
            jsonWriter.open();
            jsonWriter.write(world, name);
            jsonWriter.close();
            System.out.println("Saved world " + name + " to " + STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets game state out of simulating (playing)
    public void stop() {
        simulating = false;
    }

    // EFFECTS: returns current input
    public String getInput() {
        return input;
    }


    // EFFECTS: returns game state
    public Boolean getSimulating() {
        return simulating;
    }

    // EFFECTS: returns world
    public World getWorld() {
        return world;
    }

    // EFFECTS: returns simulate
    public Simulate getSimulate() {
        return simulate;
    }

    // MODIFIES: this
    // EFFECTS: turn isOver true
    public void makeGameOver() {
        over = true;
    }

    // EFFECTS: return over
    public Boolean isOver() {
        return over;
    }
    

}
