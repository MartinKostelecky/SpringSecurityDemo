package cz.martinkostelecky.springsecurity.exception;

public class EmailAlreadyTakenException extends Exception {

    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
