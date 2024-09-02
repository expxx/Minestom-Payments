package dev.expx.payments.exceptions;

public class NotSetupException extends RuntimeException {
    public NotSetupException(String msg) {
        super(msg);
    }
}
