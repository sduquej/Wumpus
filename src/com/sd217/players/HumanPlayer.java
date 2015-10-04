package com.sd217.players;

import com.sd217.Player;
import com.sd217.utils.ConsoleReader;
import com.sd217.utils.MessageBuilder;
import com.sd217.world.CaveRoom;
import com.sd217.world.Position;

/**
 * Created by Sebastián Duque on 01/10/15.
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
