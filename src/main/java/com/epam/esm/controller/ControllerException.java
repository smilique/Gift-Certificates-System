package com.epam.esm.controller;

public class ControllerException extends Exception {
    public ControllerException(Throwable cause) {
        super(cause);
    }
    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }
    public ControllerException(String message) {
        super(message);
    }
}