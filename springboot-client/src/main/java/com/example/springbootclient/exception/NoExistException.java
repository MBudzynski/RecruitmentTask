package com.example.springbootclient.exception;

public class NoExistException extends RuntimeException {

    public NoExistException(String message) {
        super(message);
    }
}
