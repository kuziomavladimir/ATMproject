package services.customExeptions;

public class NegativeBalanceException extends Exception{
    public NegativeBalanceException() {
    }

    public NegativeBalanceException(String message) {
        super(message);
    }

    public NegativeBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NegativeBalanceException(Throwable cause) {
        super(cause);
    }

    public NegativeBalanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
