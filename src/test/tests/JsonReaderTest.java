package tests;

import model.World;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            World world = reader.read("name");
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderWorld() {
        JsonReader reader = new JsonReader("./data/test.json");
        try {
            World world = reader.read("name");
            assertEquals("Tile", world.getBlock(0,0).getType());
            assertEquals("Death", world.getBlock(1,0).getType());
            assertEquals("End", world.getBlock(2,0).getType());
            assertEquals("Empty", world.getBlock(3,0).getType());
            assertEquals("Empty", world.getBlock(4,0).getType());
            for (int i = 0; i <= 4; i++) {
                for (int j = 1; j < 4; j++) {
                    assertEquals("Empty", world.getBlock(i,j).getType());
                }
            }
            assertEquals("Empty", world.getBlock(4,0).getType());
            assertEquals("Empty", world.getBlock(4,1).getType());
            assertEquals("Empty", world.getBlock(4,2).getType());
            assertEquals("Empty", world.getBlock(4,3).getType());
            assertEquals("Spawn", world.getBlock(4,4).getType());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }



}
