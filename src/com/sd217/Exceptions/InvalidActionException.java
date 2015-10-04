package com.sd217.exceptions;

import com.sd217.utils.MessageBuilder;

import java.util.Map;

/**
 * Created by Sebastián Duque on 28/09/15.
 */
public class InvalidActionException extends Exception {
    public InvalidActionException(Map<Character, String> validActions) {
        super(MessageBuilder.optionsInMap(validActions));
    }
}