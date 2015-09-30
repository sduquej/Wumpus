package com.sd217.Exceptions;

import com.sd217.MessageBuilder;
import com.sd217.GameRunner;

import java.util.Map;

/**
 * Created by sduquej on 28/09/15.
 */
public class InvalidActionException extends Exception {
    public InvalidActionException(Map<Character, String> validActions) {
        super(MessageBuilder.optionsInCharStringMap(validActions));
    }
}