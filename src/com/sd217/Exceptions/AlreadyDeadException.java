package com.sd217.exceptions;

/**
 * Created by Sebastián Duque on 28/09/15.
 */
public class AlreadyDeadException extends RuntimeException {
    public AlreadyDeadException(String message) {
        super(message+" is already dead.");
    }
}
