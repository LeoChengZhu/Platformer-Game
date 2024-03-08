package persistence;

import model.World;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


// Represents a reader that reads World from JSON data stored in file
// Code adapted from persistence example
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads world from file and returns it;
    // throws IOException if an error occurs reading data from file
    public World read(String name) throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorld(jsonObject, name);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses world from JSON object and returns it
    private World parseWorld(JSONObject jsonObject, String name) {
        JSONArray jsonArray = jsonObject.getJSONArray(name);
        Object json = jsonArray.get(jsonArray.length() - 1);
        JSONObject block = (JSONObject) json;
        int x = block.getInt("xpos");
        int y = block.getInt("ypos");
        World world = new World(x + 1, y + 1);
        setBlocks(world, jsonArray);
        return world;
    }

    // MODIFIES: world
    // EFFECTS: parses blocks from JSON array and adds them to world
    private void setBlocks(World world, JSONArray jsonArray) {
        for (Object json : jsonArray) {
            JSONObject block = (JSONObject) json;
            String type = block.getString("type");
            int x = block.getInt("xpos");
            int y = block.getInt("ypos");
            try {
                world.setBlock(type, x, y);
            } catch (Exception e) {
                //not supposed to occur
            }
        }
    }



}
