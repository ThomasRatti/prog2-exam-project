package classes.exceptions;

/**
 * Exception thrown when the capacity of a component is exceeded.
 */
public class CapacityExceededException extends IllegalArgumentException  {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for CapacityExceededException.
     * 
     * @param message the detail message
     */
    public CapacityExceededException(String message) {
        super(message);
    }
}
