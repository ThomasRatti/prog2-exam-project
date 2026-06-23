package classes;

import java.util.Objects;

/*
 * I decided to use an enum here because the set of possible sizes is fixed and known in advance.
 * Using an enum allows for a clear and type-safe representation of these fixed values.
 * Each enum constant corresponds to a specific size, making the code more readable and maintainable.
 */

/**
 * Enum class representing the size of a {@link Prodotto}.
 * Each enum constant corresponds to a specific size designation.
 */
public enum Taglia {
    // Fields 

    /** Small product */
    Small('S'),
    /** Medium product */
    Medium('M'),
    /** Large product */
    Large('L');

    /** Size character representation */
    private final char size;

    /*-
     * RI:
     *  - size must be one of the predefined characters, size ∈ {'S', 'M', 'L'}
     * 
     * AF:
     *  - associate each enum constant with the character representing the size of the product.
     *    (Taglia.Small  -> 'S', Taglia.Medium -> 'M', Taglia.Large  -> 'L')
     */

    // Constructor

    /**
     * Constructor for {@link Taglia} enum.
     * 
     * @param size the {@code char} representing the size of the {@link Prodotto}
     */
    //private constructor by default for enum
    Taglia(char size) {
        this.size = size;
    }

    // Methods
    
    /**
     * Returns the {@link Taglia} enum constant corresponding to the given character as {@link String}.
     * 
     * @param input the {@link String} representing the size of the {@link Prodotto}
     * @return the corresponding {@link Taglia} enum constant
     * @throws NullPointerException if {@code input} is {@code null}
     * @throws IllegalArgumentException if {@code input}, after trimming, is empty/blank or is not a single character
     * @throws IllegalArgumentException if the character does not correspond to any supported size
     */
    public static Taglia fromString(String input) {
        String s = Objects.requireNonNull(input, "input cannot be null").trim();
        if (s.isEmpty())
            throw new IllegalArgumentException("input cannot be empty/blank");
        if (s.length() != 1)
            throw new IllegalArgumentException("input improperly formatted");
        for (Taglia t : Taglia.values()) {
            if (t.size == Character.toUpperCase(s.charAt(0))) return t;
        }
        throw new IllegalArgumentException("invalid size character");
    }

    /**
     * Getter for the {@code char} representation of the size.
     * 
     * @return the character representing the size of the {@link Prodotto} as {@code char} 
     */
    public char getSize() {
        return size;
    }

    @Override
    public String toString() {
        return String.valueOf(size);
    }
}
