package model;

import java.util.List;

import static java.lang.Character.isISOControl;

// Initializes and changes game world according to input from ui, then returns the updated
// game world back to ui to be displayed.
public class Game {

    private World world;
    private Boolean simulating;
    private Simulate simulate;
    private String input;

    // EFFECTS: constructs game in create mode with no input
    public Game() {
        simulating = false;
        input = "";
    }

    // MODIFIES: this
    // EFFECTS: initializes game world
    public void createWorld(int width, int height) {
        world = new World(height, width);
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
    //          input length 4:
    //                  place [Blocks] [x] [y] -> places block at x y starting at (0, 0)
    //                                            [Blocks] -> "Tile", "Empty", "Death", "End", "Spawn"
    //          input length 6:
    //                  fill [Blocks] [start x] [start y] [end x] [end y]
    //                         -> fills area contained by the two x, y coordinates, start x, y is top left of the area,
    //                            end x, y is bottom right of the area.
    //                  [Blocks] -> "Tile", "Empty", "Death", "End", "Spawn"
    // throw exception is an exception is caught from methods from World
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
                    break;
                case 4:
                    if (commandStrings[0].equals("place")) {
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


    // EFFECTS: returns game world as a string with different character representing different blocks:
    //          "Tile"   -> "a"
    //          "Empty"  -> " " (simulating), "b" (not simulating)
    //          "Spawn"  -> "c"
    //          "End"    -> "d"
    //          "Death"  -> "e"
    //          "Player" -> "f"
    @SuppressWarnings("methodlength")
    public String getGameWorld() {
        List<Blocks> gameWorld = world.getWorld();
        StringBuilder worldString = new StringBuilder();
        for (Blocks object:gameWorld) {
            switch (object.getType()) {
                case "Tile":
                    worldString.append("a");
                    break;
                case "Empty":
                    worldString.append(emptyVariations());
                    break;
                case "Spawn":
                    worldString.append("c");
                    break;
                case "End":
                    worldString.append("d");
                    break;
                case "Death":
                    worldString.append("e");
                    break;
                case "Player":
                    worldString.append("f");
                    break;
            }
        }
        return worldString.toString();
    }

    // EFFECTS: returns character representing "Empty" depending on the game state
    public String emptyVariations() {
        if (simulating) {
            return " ";
        }
        return "b";
    }

    // MODIFIES: this
    // EFFECTS: initializes simulate, turns game state into simulating (playing)
    public void play() {
        if (checkForSpawn()) {
            simulating = true;
            simulate = new Simulate();
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

    // MODIFIES: this
    // EFFECTS: sets game state out of simulating (playing)
    public void stop() {
        simulating = false;
    }

    // EFFECTS: returns current input
    public String getInput() {
        return input;
    }
}
