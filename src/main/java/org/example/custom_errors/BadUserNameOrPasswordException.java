package org.example.custom_errors;

public class BadUserNameOrPasswordException extends Exception{

    public BadUserNameOrPasswordException() {
        super();
    }

    public BadUserNameOrPasswordException(String message) {
        super(message);
    }
}
