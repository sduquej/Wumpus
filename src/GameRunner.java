import dynamicobjects.Adventurer;
import dynamicobjects.DynamicCaveObject;
import dynamicobjects.Wumpus;
import players.*;
import utils.ColorCodes;
import utils.ConsoleReader;
import utils.MessageBuilder;
import world.CaveRoom;
import world.Position;
import world.World;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

/**
 * Main class and entry point.
 * @author  Sebastián Duque on 17/09/15.
 */
public class GameRunner {
    /**
     * Duration of the torch. Once this number of steps are used the player loses
     * as the torch goes off.
     */
    private static final int TORCH_DURATION = 200;
    /**
     * Minimum size acceptable for a dimension (width/length)
     */
    public static final int MIN_DIMENSION = 2;
    /**
     * Maximum size acceptable for a dimension (width/length)
     */
    public static final int MAX_DIMENSION = 20;

    /**
     * Types of players that can play the game. The String value is a representation
     * of the Object that will be used, which must be a subclass of {@link Player}
     */
    public static final Map<Integer, String> PLAYER_TYPES;
    static {
        Map<Integer, String> playerTypes = new Hashtable<>();
        playerTypes.put(1, "Human player");
        playerTypes.put(2, "Random movement AI");
        playerTypes.put(3, "Alternating: Up then Right");
        playerTypes.put(4, "Safe and exploring AI");
        PLAYER_TYPES = Collections.unmodifiableMap(playerTypes);
    }


//    Instance Attributes
    private World world;
    private boolean gameOverWin;
    private int caveWidth;
    private int caveHeight;
    private DynamicCaveObject adventurer;
    private DynamicCaveObject wumpus;
    private Player player;

    public GameRunner() {
        this.world = init();
        this.wumpus = Wumpus.getInstance();
    }

    private World init(){
//        Create the cave
        System.out.println(MessageBuilder.gameIntroduction());
        System.out.print(MessageBuilder.askDimension("width", MIN_DIMENSION, MAX_DIMENSION));
        caveWidth = ConsoleReader.readInt(MIN_DIMENSION, MAX_DIMENSION);
        System.out.print(MessageBuilder.askDimension("height", MIN_DIMENSION, MAX_DIMENSION));
        caveHeight = ConsoleReader.readInt(MIN_DIMENSION, MAX_DIMENSION);
        World w = new World(caveHeight, caveWidth);
//        Print the world that got generated
        System.out.println(w);
//        Place the adventurer
        DynamicCaveObject adv = placeAdventurer();
        w.placeAdventurer(adv);
        this.adventurer = adv;
//        Select the player
        this.player = selectPlayer(adv.getCurrentPosition(), w);

        return w;
    }

    /**
     * Main method. Initialises the Game, runs it and finishes by reporting the outcome.
     * @param args
     */
    public static void main(String[] args) {
        GameRunner gr = new GameRunner();
        System.out.println(gr.world);

        int moves = 0;
        try {
            moves = gr.play();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!gr.gameOverWin) {
            if (!gr.adventurer.isAlive()) {
                System.out.println("Oh sod! You died X_X");
            } else if (moves == TORCH_DURATION) {
                System.out.println("Click! .... Click! ... Your torch goes off as you go completely blind hopeless");
            }
        } else {
            System.out.println("You made it out of Wumpus World in " + moves + " moves. " +
                    ColorCodes.GREEN + "Congratulations!");
        }
    }

    /**
     * Runs the game, one round at a time until it is over or the torch runs off.
     * @return number of steps taken to finish the game.
     * @throws InterruptedException
     */
    private int play() throws InterruptedException {
        int steps = 0;
        while (steps <= TORCH_DURATION) {
//            Display World
//            ANSI escape code for clearing the entire screen
            System.out.print(ColorCodes.CLEAR_SCREEN);
            System.out.println(world);
//            Informs the player of what position its in and what the room has
            Position currentPosition = adventurer.getCurrentPosition();
            player.getPercepts(currentPosition, world.getRoomInPosition(currentPosition));

//            Ask for Action and Direction
            String action = player.askAction();
            String direction = player.askDirection();
            System.out.println(MessageBuilder.decisionMade(action, direction));
            Position roomInChosenDirection= world.getNeighbourInDirection(currentPosition, direction);
//            Perform Action
            slowExecution();
            if(action == "move"){
                world.moveDynamicObject(adventurer, roomInChosenDirection);
            } else if (action == "shoot"){
                player.removeValidAction('S');
                player.inform("arrowFired");
                if(wumpus.atLocation(roomInChosenDirection)){
                    System.out.println(MessageBuilder.wumpusHit());
                    System.out.println(wumpus.killedBy("arrow"));
                    world.wumpusKilled(wumpus);
                    player.inform("wumpusKilled");
                }else{
                    System.out.println(MessageBuilder.arrowLost());
                    world.moveDynamicObject(Wumpus.getInstance(), null);
                }
            }
            steps++;
            slowExecution();
//            Notify Changes (player dead/wumpus killed/treasure collected/exit found)
            if(endOfTurn()){
                break;
            }
        }
        return steps;
    }


    /**
     * Places the adventurer in the specified initial position
     * @return dynamicobjects.Adventurer object
     */
    private DynamicCaveObject placeAdventurer(){
        System.out.println(ColorCodes.GREEN + "Now that you know how the cave looks like it's up to you to decide. " +
                "What will be the adventurer's starting position?");
        System.out.print("Column (1 - " + caveWidth + "): ");
        int advInitCol = ConsoleReader.readInt(1, caveWidth) - 1;
        System.out.print("Row (1 - " + caveHeight + "): ");
        int advInitRow = ConsoleReader.readInt(1, caveHeight) - 1;
        return  Adventurer.getInstance().init(advInitCol, advInitRow);
    }

    /**
     * This method adds a delay between the execution of actions
     * when the player is not human.
     */
    private void slowExecution(){
        if (!(player instanceof HumanPlayer)){
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.err.println("Woke up from the timer");
            }
        }
    }

    /**
     * Prompts for a selection of the type of player that will play the game
     * @return players.Player selected
     * @param currentPosition Position where the adventurer begins
     * @param w World in which the player is
     */
    private Player selectPlayer(Position currentPosition, World w){
        System.out.println(MessageBuilder.choosePlayer(PLAYER_TYPES));
        int chosenPlayerType = ConsoleReader.readInt(1, PLAYER_TYPES.keySet().size());
        Player player = null;
        switch (chosenPlayerType){
            case 1:
                player = new HumanPlayer();
                break;
            case 2:
                player = new RandomAIPlayer();
                break;
            case 3:
                player = new AlternateUpRightPlayer();
                break;
            case 4:
                player = new OnlySafeAI(currentPosition, w);
                break;
        }
        return player;
    }

    /**
     * This method runs at the end of every turn, it returns a boolean that indicates if the game is over or not
     * @return boolean - whether the game is over or not as a result of the last action
     */
    private boolean endOfTurn(){
        boolean gameFinished = false;
        Position adventurerPosition = adventurer.getCurrentPosition();
        if(wumpus.isAlive() && wumpus.atLocation(adventurerPosition)){
            System.out.println(adventurer.killedBy(wumpus.whoAmI()));
            return true;
        }
        CaveRoom currentRoom = world.getRoomInPosition(adventurerPosition);
        if(currentRoom.isPit()){
            System.out.println(adventurer.killedBy("pit"));
            return true;
        }
        if(currentRoom.hasSuperbat()){
            Position randomPosition;
            do{
                randomPosition = world.getRandomPosition();
                currentRoom = world.getRoomInPosition(randomPosition);
            } while (!currentRoom.isRoomSafe());
            world.moveDynamicObject(adventurer, randomPosition);
        }
        if(currentRoom.isExit() && adventurer.hasTreasure()){
            gameOverWin = true;
            return true;
        }
        if(currentRoom.containsTreasure() && world.removeTreasure(adventurerPosition)){
            player.inform("treasureCollected");
            adventurer.setTreasure(true);
        }


        return gameFinished;
    }

}
