package services.customExeptions;

import org.springframework.dao.DataIntegrityViolationException;

public class ViolationUniquenessException extends Exception {

    public ViolationUniquenessException(String msg) {
        super(msg);
    }

    public ViolationUniquenessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
