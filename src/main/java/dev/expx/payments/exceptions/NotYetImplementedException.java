package dev.expx.payments.exceptions;

/**
 * Called when the user attempts
 * to use a not-yet-implemented
 * store type */
public class NotYetImplementedException extends RuntimeException{
    /**
     * Used to call the exception
     * @param msg Message to throw in the console
     */
    public NotYetImplementedException(String msg) {
        super(msg);
    }
}
