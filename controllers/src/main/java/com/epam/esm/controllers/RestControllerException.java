package com.epam.esm.controllers;

public class RestControllerException {

    private final String error;
    private final String errorMessage;

    public RestControllerException(String errorCode, String errorMessage) {
        this.error = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
