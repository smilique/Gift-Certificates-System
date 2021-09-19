package com.epam.esm.service;

public class ServiceException extends Exception {
    public ServiceException(Throwable cause) {
        super(cause);
    }
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public ServiceException(String message) {
        super(message);
    }
}