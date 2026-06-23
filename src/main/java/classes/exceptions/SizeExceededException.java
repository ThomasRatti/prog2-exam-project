package classes.exceptions;

/**
 * Exception thrown when the size limit is exceeded.
 */
public class SizeExceededException extends IllegalArgumentException {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for SizeExceededException.
     * 
     * @param message the detail message
     */
    public SizeExceededException(String message) {
        super(message);
    }

}
