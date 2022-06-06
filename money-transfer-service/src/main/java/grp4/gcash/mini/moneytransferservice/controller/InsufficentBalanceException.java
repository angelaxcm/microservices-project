package grp4.gcash.mini.moneytransferservice.controller;

public class InsufficentBalanceException extends Exception {
    public InsufficentBalanceException() {
    }

    public InsufficentBalanceException(String message) {
        super(message);
    }

    public InsufficentBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficentBalanceException(Throwable cause) {
        super(cause);
    }

    public InsufficentBalanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
