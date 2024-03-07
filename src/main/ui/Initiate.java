package ui;

import model.World;
import persistence.JsonReader;

import java.io.IOException;
import java.util.Scanner;

//a class that handles input to create screen to play game
public class Initiate {

    private static final String STORE = "./data/save.json";
    private JsonReader jsonReader;
    private int width;
    private int height;
    private Scanner input;

    //EFFECTS: Constructs input system and runs application
    public Initiate() throws InterruptedException {
        jsonReader = new JsonReader(STORE);
        run();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void run() throws InterruptedException {
        boolean keepGoing = true;
        String command = null;
        input = new Scanner(System.in);

        while (keepGoing) {
            menu();
            command = input.nextLine();
            if (command.equals("quit")) {
                keepGoing = false;
            } else {
                keepGoing = handleCommand(command);
            }
        }
    }

    // EFFECTS: displays menu of options to user
    private void menu() {
        System.out.println("\nSelect from:");
        System.out.println("\tnew -> create new world");
        System.out.println("\tload -> load world from file");
        System.out.println("\tquit -> quit program");
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private Boolean handleCommand(String command) throws InterruptedException {
        if (command.equals("new")) {
            try {
                System.out.println("Enter world width(max 32):");
                width = input.nextInt();
                System.out.println("Enter world height(max 32):");
                height = input.nextInt();
                new WorldMaker(width, height);
                return false;
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        } else if (command.equals("load")) {
            try {
                World world = jsonReader.read("name");
                System.out.println("Loaded world from " + STORE);
                new WorldMaker(world);
                return false;
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + STORE);
            }
        }
        return true;
    }
}

