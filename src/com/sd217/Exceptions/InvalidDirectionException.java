package com.sd217.Exceptions;

import com.sd217.MessageBuilder;
import com.sd217.GameRunner;

/**
 * Created by sduquej on 28/09/15.
 */
public class InvalidDirectionException extends Exception {
    public InvalidDirectionException() {
        super(MessageBuilder.optionsInCharStringMap(GameRunner.VALID_DIRECTIONS, "Remember!"));
    }
}
