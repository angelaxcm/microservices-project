package grp4.gcash.mini.moneytransferservice.exceptions;

public class MoneyTransferException extends Exception {
    public MoneyTransferException() {
    }

    public MoneyTransferException(String message) {
        super(message);
    }

    public MoneyTransferException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoneyTransferException(Throwable cause) {
        super(cause);
    }

    public MoneyTransferException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
