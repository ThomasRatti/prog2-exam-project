package classes.exceptions;

/**
 * Exception thrown when there is a mismatch between expected and actual item types.
 */
public class ItemMismatchException extends IllegalArgumentException {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for ItemMismatchException.
     * 
     * @param message the detail message
     */
    public ItemMismatchException(String message) {
        super(message);
    }

}
