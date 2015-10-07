package players;

import utils.ConsoleReader;
import utils.MessageBuilder;
import world.CaveRoom;
import world.Position;

/**
 * @author Sebastián Duque on 01/10/15.
 */
public class HumanPlayer extends Player {
    @Override
    public String askAction() {
        System.out.println(MessageBuilder.optionsInMap(validActions, "What do you want to do?"));
        return ConsoleReader.readAction(validActions);
    }

    @Override
    public String askDirection() {
        System.out.println(MessageBuilder.optionsInMap(VALID_DIRECTIONS, "Which direction?"));
        return ConsoleReader.readDirection(VALID_DIRECTIONS);
    }

    @Override
    public void inform(String condition) {
//        Doesn't care
    }

    @Override
    protected void tellKnowledgeBase(Position position, CaveRoom roomInPosition) {
//        Doesn't care
    }

}
