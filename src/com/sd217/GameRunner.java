package com.sd217;

import com.sd217.DynamicObjects.Adventurer;
import com.sd217.DynamicObjects.Wumpus;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class GameRunner {
    private static final int TORCH_DURATION = 5;
    public static final int MIN_DIMENSION = 1;
    public static final int MAX_DIMENSION = 100;
    public Map<Character, String> validActions;

    public static final Map<Character, String> VALID_DIRECTIONS;
    static {
        Map<Character, String> validActions = new Hashtable<>();
        validActions.put('W', "up");
        validActions.put('A', "left");
        validActions.put('S', "down");
        validActions.put('D', "right");
        VALID_DIRECTIONS = Collections.unmodifiableMap(validActions);
    }


//    Instance Attributes
    private final World world;
    private boolean gameOverWin;
    private int caveWidth;
    private int caveHeight;
    private DynamicCaveObject adventurer;
    private DynamicCaveObject wumpus;

    public GameRunner() {
        this.validActions = new Hashtable<>();
        this.validActions.put('M', "move");
        this.validActions.put('S', "shoot");
        this.world = init();
        this.wumpus = Wumpus.getInstance();
    }

    public static void main(String[] args) {
        GameRunner gr = new GameRunner();
        System.out.println(gr.world);

        int moves = gr.play();

        if (!gr.gameOverWin) {
            if (!gr.adventurer.isAlive()) {
                System.out.print("Oh no! You're dead X_X");
            } else if (moves == TORCH_DURATION) {
                System.out.print("Click! .... Click! ... Your torch goes off as you go completely blind hopeless");
            }
        } else {
            System.out.print("You made it out of Wumpus World in " + moves + " moves. Congratulations!");
        }
    }

    private int play() {
        int steps = 0;
        while (steps < TORCH_DURATION) {
//            Display World
//            ANSI escape code for clearing the entire screen
            System.out.print(ColorCodes.CLEAR_SCREEN);
            System.out.println(world);

//            Ask for Action and Direction
            String action = askAction();
            String direction = askDirection();
            System.out.println(MessageBuilder.decisionMade(action, direction));
            Position roomInChosenDirection= world.getNeighbourInDirection(adventurer.getCurrentPosition(), direction);
//            Perform Action
            if(action == "move"){
                world.moveDynamicObject(adventurer, roomInChosenDirection);
            } else if (action == "shoot"){
                validActions.remove('S');
                if(wumpus.atLocation(roomInChosenDirection)){
                    System.out.println(MessageBuilder.wumpusHit());
                    System.out.println(wumpus.killedBy("arrow"));
                }else{
                    System.out.println(MessageBuilder.arrowLost());
                    world.moveDynamicObject(Wumpus.getInstance(), null);
                }
            }
            steps++;
            if(endOfTurn()){
                break;
            }
//            Notify Changes (player dead/wumpus killed/treasure collected/exit found)
        }
        return steps;
    }

    private World init(){
//        Create the cave
        System.out.println(MessageBuilder.gameIntroduction());
        System.out.print(MessageBuilder.askDimension("width"));
        caveWidth = ConsoleReader.readInt(MIN_DIMENSION, MAX_DIMENSION);
        System.out.print(MessageBuilder.askDimension("height"));
        caveHeight = ConsoleReader.readInt(MIN_DIMENSION, MAX_DIMENSION);
        World w = new World(caveHeight, caveWidth);
//        Print the world that got generated
        System.out.println(w);
//        Place the adventurer
        System.out.print("Where should we place the Adventurer? (Column)(1 - " + caveWidth + ")");
        int advInitCol = ConsoleReader.readInt(1, caveWidth) - 1;
        System.out.print("Where should we place the Adventurer? (Row)(1 - " + caveHeight + ")");
        int advInitRow = ConsoleReader.readInt(1, caveHeight) - 1;
        DynamicCaveObject adv = Adventurer.getInstance().init(advInitCol, advInitRow, w);
        w.placeAdventurer(adv);
        this.adventurer = adv;

        return w;
    }

    /**
     * This method is run at the end of every turn, it returns a boolean that indicates if the game is over or not
     * @return
     */
    private boolean endOfTurn(){
        boolean gameFinished = false;
        Position adventurerPosition = adventurer.getCurrentPosition();
        if(wumpus.atLocation(adventurerPosition)){
            System.out.println(adventurer.killedBy(wumpus.whoAmI()));
            return true;
        }
        CaveRoom currentRoom = world.getRoomInPosition(adventurerPosition);
        if(currentRoom.isPit()){
            System.out.println(adventurer.killedBy("pit"));
            return true;
        }
        if(currentRoom.isExit() && adventurer.hasTreasure()){
            gameOverWin = true;
            return true;
        }
        if(currentRoom.containsTreasure() && world.removeTreasure(adventurerPosition)){
            adventurer.setTreasure(true);
        }
        if(currentRoom.hasSuperbat()){
            Position randomPosition;
            CaveRoom caveRoom;
            do{
                randomPosition = world.getRandomPosition();
                caveRoom = world.getRoomInPosition(randomPosition);
            } while (!caveRoom.isRoomSafe());
            world.moveDynamicObject(adventurer, randomPosition);
        }

        return gameFinished;
    }

    private String askAction(){
        System.out.println(MessageBuilder.optionsInCharStringMap(validActions, "What do you want to do?"));
        return ConsoleReader.readAction(validActions);
    }

    private static String askDirection(){
        System.out.println(MessageBuilder.optionsInCharStringMap(GameRunner.VALID_DIRECTIONS, "Which direction?"));
        return ConsoleReader.readDirection(VALID_DIRECTIONS);
    }
}
