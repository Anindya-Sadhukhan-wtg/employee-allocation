package org.example.employeeallocation.exception;

public class BadDataException extends RuntimeException {
    public BadDataException() {
    }
    public BadDataException(String message) {
        super(message);
    }
}
