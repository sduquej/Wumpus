package exceptions;

import utils.MessageBuilder;

import java.util.Map;

/**
 * @author Sebastián Duque on 28/09/15.
 */
public class InvalidActionException extends Exception {
    public InvalidActionException(Map<Character, String> validActions) {
        super(MessageBuilder.optionsInMap(validActions));
    }
}