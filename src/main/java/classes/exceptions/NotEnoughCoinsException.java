package classes.exceptions;

/**
 * Exception thrown when there are not enough coins to complete an operation.
 */
public class NotEnoughCoinsException extends IllegalArgumentException {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for NotEnoughCoinsException.
     * 
     * @param message the detail message
     */
    public NotEnoughCoinsException(String message) {
        super(message);
    }

}
