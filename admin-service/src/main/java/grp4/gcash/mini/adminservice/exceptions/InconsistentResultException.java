package grp4.gcash.mini.adminservice.exceptions;

public class InconsistentResultException extends Exception {
    public InconsistentResultException() {
    }

    public InconsistentResultException(String message) {
        super(message);
    }

    public InconsistentResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public InconsistentResultException(Throwable cause) {
        super(cause);
    }

    public InconsistentResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
