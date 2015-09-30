package com.sd217.Exceptions;

/**
 * Created by sduquej on 28/09/15.
 */
public class AlreadyDeadException extends RuntimeException {
    public AlreadyDeadException(String message) {
        super(message+" is already dead.");
    }
}
