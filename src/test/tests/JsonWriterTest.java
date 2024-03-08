package tests;

import model.World;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            World world = new World(3,3);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterWorld() {
        try {
            World world = new World(3,3);
            world.setBlock("Tile",0,0);
            world.setBlock("Death",1,0);
            world.setBlock("End",2,0);
            world.setBlock("Spawn", 2, 2);
            JsonWriter writer = new JsonWriter("./data/testWriter.json");
            writer.open();
            writer.write(world, "name");
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriter.json");
            world = reader.read("name");

            assertEquals("Tile", world.getBlock(0,0).getType());
            assertEquals("Death", world.getBlock(1,0).getType());
            assertEquals("End", world.getBlock(2,0).getType());

            assertEquals("Empty", world.getBlock(0,1).getType());
            assertEquals("Empty", world.getBlock(1,1).getType());
            assertEquals("Empty", world.getBlock(2,1).getType());

            assertEquals("Empty", world.getBlock(0,2).getType());
            assertEquals("Empty", world.getBlock(1,2).getType());
            assertEquals("Spawn", world.getBlock(2,2).getType());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (Exception e) {
            fail();
        }
    }

}
