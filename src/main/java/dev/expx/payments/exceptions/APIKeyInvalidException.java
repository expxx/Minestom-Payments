package dev.expx.payments.exceptions;

public class APIKeyInvalidException extends RuntimeException {
    public APIKeyInvalidException(String msg) {
        super(msg);
    }
}
