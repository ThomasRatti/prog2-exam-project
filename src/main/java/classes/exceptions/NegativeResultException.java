package classes.exceptions;

/**
 * Exception thrown when a negative result is encountered where it is not allowed.
 */
public class NegativeResultException extends IllegalArgumentException {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for NegativeResultException.
     * 
     * @param message the detail message
     */
    public NegativeResultException(String message) {
        super(message);
    }

}
