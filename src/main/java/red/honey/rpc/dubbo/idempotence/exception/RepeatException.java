package red.honey.rpc.dubbo.idempotence.exception;

/**
 * @author yangzhijie
 */
public class RepeatException extends RuntimeException {

    public RepeatException() {
    }

    public RepeatException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatException(String message) {
        super(message);
    }

    public RepeatException(Throwable cause) {
        super(cause);
    }
}
