package grp4.gcash.mini.apiservice.exceptions;

public class MoneyTransferFailedException extends Exception {
    public MoneyTransferFailedException() {
    }

    public MoneyTransferFailedException(String message) {
        super(message);
    }

    public MoneyTransferFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoneyTransferFailedException(Throwable cause) {
        super(cause);
    }

    public MoneyTransferFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
