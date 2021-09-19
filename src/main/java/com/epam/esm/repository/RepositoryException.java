package com.epam.esm.repository;

public class RepositoryException extends Exception {
    public RepositoryException(Throwable cause) {
        super(cause);
    }
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
    public RepositoryException(String message) {
        super(message);
    }
}