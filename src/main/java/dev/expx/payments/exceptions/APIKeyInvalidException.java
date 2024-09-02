package dev.expx.payments.exceptions;

/**
 * Called when the API key for a store
 * is invalid, or cannot be used.
 */
public class APIKeyInvalidException extends RuntimeException {
    /**
     * Used to call the exception
     * @param msg Message to throw in the console
     */
    public APIKeyInvalidException(String msg) {
        super(msg);
    }
}
