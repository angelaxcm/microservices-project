package grp4.gcash.mini.walletservice.exceptions;

public class CreateWalletException extends Exception {
    public CreateWalletException() {
    }

    public CreateWalletException(String message) {
        super(message);
    }

    public CreateWalletException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateWalletException(Throwable cause) {
        super(cause);
    }

    public CreateWalletException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
