package org.example.custom_errors;

public class NoMatchingRecordsException extends Exception {

    public NoMatchingRecordsException() {
        super();
    }

    public NoMatchingRecordsException(String message) {
        super(message);
    }
}
