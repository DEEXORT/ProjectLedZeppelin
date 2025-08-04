package com.quest.exception;

public class UserEmptyException extends RuntimeException {
    public UserEmptyException(String message) {
        super(message);
    }
}
