package grp4.gcash.mini.adminservice.exceptions;

public class GetAllException extends Exception {
    public GetAllException() {
    }

    public GetAllException(String message) {
        super(message);
    }

    public GetAllException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetAllException(Throwable cause) {
        super(cause);
    }

    public GetAllException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
