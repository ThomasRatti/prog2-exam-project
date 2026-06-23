package classes.exceptions;

/**
 * Exception thrown when attempting to access or operate on an empty slot.
 */
public class EmptySlotException extends IllegalStateException {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;    

    /**
     * Constructor for EmptySlotException.
     * 
     * @param message the detail message
     */
    public EmptySlotException(String message) {
        super(message);
    }

}
