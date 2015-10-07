package exceptions;

import players.Player;
import utils.MessageBuilder;

/**
 * @author Sebastián Duque on 28/09/15.
 */
public class InvalidDirectionException extends Exception {
    public InvalidDirectionException() {
        super(MessageBuilder.optionsInMap(Player.VALID_DIRECTIONS, "Remember!"));
    }
}
