package dev.expx.payments.exceptions;

/**
 * Called when the plugin hasn't finished setting
 * up, but something tried to use it.
 */
public class NotSetupException extends RuntimeException {
    /**
     * Used to call the exception
     * @param msg Message to throw in the console
     */
    public NotSetupException(String msg) {
        super(msg);
    }
}
