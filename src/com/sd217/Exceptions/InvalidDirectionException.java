package com.sd217.exceptions;

import com.sd217.Player;
import com.sd217.utils.MessageBuilder;

/**
 * Created by Sebastián Duque on 28/09/15.
 */
public class InvalidDirectionException extends Exception {
    public InvalidDirectionException() {
        super(MessageBuilder.optionsInMap(Player.VALID_DIRECTIONS, "Remember!"));
    }
}
