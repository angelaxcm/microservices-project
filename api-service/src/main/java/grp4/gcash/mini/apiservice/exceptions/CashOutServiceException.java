package grp4.gcash.mini.apiservice.exceptions;

public class CashOutServiceException extends Exception {
    public CashOutServiceException() {
    }

    public CashOutServiceException(String message) {
        super(message);
    }

    public CashOutServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CashOutServiceException(Throwable cause) {
        super(cause);
    }

    public CashOutServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
