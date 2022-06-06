package grp4.gcash.mini.apiservice.exceptions;

public class CashInServiceException extends Exception {
    public CashInServiceException() {
    }

    public CashInServiceException(String message) {
        super(message);
    }

    public CashInServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CashInServiceException(Throwable cause) {
        super(cause);
    }

    public CashInServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
