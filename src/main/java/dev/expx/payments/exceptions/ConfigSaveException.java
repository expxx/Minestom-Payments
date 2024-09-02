package dev.expx.payments.exceptions;

/**
 * Called when the config cannot be saved
 * due to an error
 */
public class ConfigSaveException extends RuntimeException {
    /**
     * Used to call the exception
     * @param msg Message to throw in the console
     */

    public ConfigSaveException(String msg) {
        super(msg);
    }

}
