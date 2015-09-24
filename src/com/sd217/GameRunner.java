package com.sd217;

public class GameRunner {
    private static final int TORCH_DURATION = 100;
    private static boolean dead = false;
    private static boolean gameOverWin = false;

    private static int caveWidth, caveHeight;

    public static void main(String[] args) {
        int steps = 0;
        boolean validInput;
        System.out.println(ColorCodes.GREEN + "Hello and welcome to Wumpus World\n\n" +
                "Let us begin... But first, you will be asked to define a size for the cave");

        System.out.println("The cave has the shape of a rectangle.");
        System.out.print("What should be its width? (1 - 100) ");
        caveWidth = ConsoleReader.readInt(1, 100);
        System.out.print("What should be its height? (1 - 100) ");
        caveHeight = ConsoleReader.readInt(1, 100);

        World world = new World(caveHeight, caveWidth);

        while (steps < TORCH_DURATION) {
//        ANSI escape code for clearing the entire screen
            System.out.print("\033[H\033[2J");
            System.out.println(world.toString());
            steps++;
        }
        if (!gameOverWin) {
            if (dead) {
                System.out.print("Oh no! You're dead X_X");
            } else if (steps == TORCH_DURATION) {
                System.out.print("Click! .... Click! ... Your torch goes off as you go completely blind hopeless");
            }
        } else {
            System.out.print("You made it out of Wumpus World. Congratulations!");
        }
    }
}
