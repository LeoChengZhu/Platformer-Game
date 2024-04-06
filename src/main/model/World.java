package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Defines the game world and provides methods to operate on the game world
public class World implements Writable {

    private static final int MINHEIGHT = 2;
    private static final int MINWIDTH = 2;
    private static final int MAXHEIGHT = 32;
    private static final int MAXWIDTH = 32;
    private int height;
    private int width;
    private List<Blocks> world;

    // EFFECTS: constructs a array list of empty Blocks the represents the game world
    // according to specified width and height,
    // the height and width is constrained within the MAX and MIN values.
    public World(int width, int height) {

        world = new ArrayList<>();

        if (height > MAXHEIGHT) {
            this.height = MAXHEIGHT;

        } else if (height < MINHEIGHT) {
            this.height = MINHEIGHT;

        } else {
            this.height = height;
        }

        if (width > MAXWIDTH) {
            this.width = MAXWIDTH;

        } else if (width < MINWIDTH) {
            this.width = MINWIDTH;

        } else {
            this.width = width;
        }

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.world.add(new Empty(j, i));
            }
        }
        EventLog.getInstance().logEvent(
                new Event("Created World of " + width + " x " + height));
    }

    // REQUIRES: 0 <= x < width, 0 <= y < height
    // MODIFIES: this
    // EFFECTS: adds block of block type at the designated x, y coordinates,
    // throws exception when a spawn related exception is caught.
    public void setBlock(String blockType, int xpos, int ypos)
            throws SpawnAlreadyExistsException, IllegalSpawnException, BlockAboveSpawnException {
        switch (blockType) {
            case "Tile":
                addBlock(new Tile(xpos, ypos));
                break;
            case "Empty":
                addBlock(new Empty(xpos, ypos));
                break;
            case "Spawn":
                try {
                    placeSpawn(xpos, ypos);
                } catch (Exception e) {
                    throw e;
                }
                break;
            case "End":
                addBlock(new End(xpos, ypos));
                break;
            case "Death":
                addBlock(new Death(xpos, ypos));
                break;
        }
    }

    // REQUIRES: 0 <= block.getXpos() < width, 0 <= block.getYpos() < height
    // MODIFIES: this
    // EFFECTS: sets block at its designated coordinate in the game world
    public void addBlock(Blocks block) throws BlockAboveSpawnException {
        if (!block.getType().equals("Empty")) {
            if (block.getYpos() < height - 1) {
                if (getBlock(block.getXpos(), block.getYpos() + 1).getType().equals("Spawn")) {
                    throw new BlockAboveSpawnException();
                }
            }
        }
        world.set(block.getXpos() + (width * block.getYpos()), block);
        EventLog.getInstance().logEvent(
                new Event("Set block: " + block.getType()
                        + " at " + block.getXpos() + ", " + block.getYpos()));
    }

    // REQUIRES: xposStart > xposEnd, xposStart >= 0, xposEnd < width
    //           yposStart < yposEnd, yposStart >= 0, yposEnd < height
    // MODIFIES: this
    // EFFECTS: fills area in the game world with inputted block type, xposStart, yposStart indicates
    //          coordinate of top left point, xposEnd and yposEnd indicates coordinate at bottom right point
    //          throws FillWithSpawnException if attempting to fill with block type "Spawn"
    public void fill(String blockType, int xposStart, int yposStart, int xposEnd, int yposEnd)
            throws SpawnAlreadyExistsException, IllegalSpawnException,
            FillWithSpawnException, BlockAboveSpawnException {
        if (blockType.equals("Spawn")) {
            throw new FillWithSpawnException();
        }
        for (int i = yposStart; i <= yposEnd; i++) {
            for (int j = xposStart; j <= xposEnd; j++) {
                setBlock(blockType, j, i);
            }
        }
    }

    // REQUIRES: 0 <= block.getXpos() < width, 0 <= block.getYpos() < height
    // MODIFIES: this
    // EFFECTS: places a block of type "Spawn" at x, y, throws IllegalSpawnException if placing
    // spawn at top most row, throws SpawnAlreadyExistsException if a spawn has already been placed.
    public void placeSpawn(int x, int y) throws
            SpawnAlreadyExistsException, IllegalSpawnException, BlockAboveSpawnException {
        if (y == 0) {
            throw new IllegalSpawnException();
        }
        for (Blocks block:world) {
            if (block.getType().equals("Spawn")) {
                throw new SpawnAlreadyExistsException();
            }
        }
        addBlock(new Spawn(x, y));
        setBlock("Empty", x, y - 1);
    }

    // EFFECTS: returns the array list world
    public List<Blocks> getWorld() {
        return world;
    }

    // EFFECTS: return block at coordinate (x, y) from world
    public Blocks getBlock(int x, int y) {
        return world.get(x + (width * y));
    }

    // EFFECTS: returns height
    public int getHeight() {
        return height;
    }

    // EFFECTS returns width
    public int getWidth() {
        return width;
    }

    @Override
    public JSONObject toJson(String name) {
        JSONObject json = new JSONObject();
        json.put(name, world);
        return json;
    }
}
