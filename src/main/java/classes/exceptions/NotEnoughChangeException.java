package classes.exceptions;

/**
 * Exception thrown when there is not enough change available in the cash fund to fulfill a request.
 */
public class NotEnoughChangeException extends IllegalStateException {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for NotEnoughChangeException.
     * 
     * @param message the detail message
     */
    public NotEnoughChangeException(String message) {
        super(message);
    }

}
