package classes;

import java.math.BigDecimal;
import java.util.Objects;

import classes.exceptions.NegativeResultException;

/**
 * Immutable class representing a monetary amount in units and cents as {@link Integer}s.
 * <p>
 * It provides methods for arithmetic operations (addition, subtraction, multiplication and division)
 * and conversions between different representations (cents and string).
 * </p>
 */
public final class Importo implements Comparable<Importo> {
    // Fields

    /*
     * I decide to represent the amount using two separate integer fields (whole units and the cents) instead of a single floating-point number.
     * This approach avoids potential rounding problems of float or double types. 
    */
    /** Units of the amount */
    private final int unit;
    /** Cents of the amount */
    private final int cent;

    /*-
     * RI: 
     *  - unit ≥ 0
     *  - 0 ≤ cent < 100
     * 
     * AF: 
     *  - unit represent the whole part of the amount, cent represent the decimal part of the amount
     *    and the total amount is given by unit + cent/100
     */

    // Constructor

    /**
     * Constructor for {@link Importo} class with given units and cents.
     * 
     * @param  unit the whole part of the amount
     * @param  cent the decimal part of the amount
     * @throws IllegalArgumentException if unit and cent do not satisfy the RI
     */
    //private constructor that enforces the RI
    private Importo(int unit, int cent) {
        if (unit < 0) throw new IllegalArgumentException("unit must not be negative");
        if (cent < 0 || cent >= 100) throw new IllegalArgumentException("cent must be between 0 and 99");
        this.unit = unit;
        this.cent = cent;
    }

     // Methods

    /**
     * Factory method to create an {@link Importo} from total cents representation.
     * 
     * @param totalCents the total amount in cents
     * @return a new {@link Importo} object representing the amount
     * @throws IllegalArgumentException if totalCents is negative
     */
    public static Importo fromCents(int totalCents) {
        if (totalCents < 0) throw new IllegalArgumentException("totalCents must not be negative");
        return new Importo(totalCents / 100, totalCents % 100);
    }

    /**
     * Factory method to create an {@link Importo} from a String representation.
     * 
     * @param importoStr the String representation of the amount (e.g., "12,34" or "12.34")
     * @return a new {@link Importo} object representing the amount
     * @throws NullPointerException if importoStr is {@code null}
     * @throws IllegalArgumentException if importoStr is empty, not a valid number, 
     *                                  or improperly formatted (negative or has more than two decimal places)
     */
    public static Importo fromString(final String importoStr){
        Objects.requireNonNull(importoStr, "importoStr must not be null");
        try {
            if (importoStr.trim().isEmpty())
                throw new IllegalArgumentException("importoStr must not be empty");

            //more readable version of: int totalCents = new BigDecimal(importoStr.replace(',', '.').trim()).multiply(BigDecimal.valueOf(100)).intValueExact();
            String cleaned = importoStr.replace(',', '.').trim();
            BigDecimal value = new BigDecimal(cleaned);
            BigDecimal multiplier = BigDecimal.valueOf(100);
            int totalCents = value.multiply(multiplier).intValueExact();
            
            if (totalCents < 0) throw new IllegalArgumentException("importoStr must not be negative");
            return fromCents(totalCents);
        } catch (NumberFormatException | ArithmeticException e) {
            throw new IllegalArgumentException("importoStr not valid: " + importoStr, e);
        }
    }

    /**
     * Get the total amount in cents.
     * 
     * @return the total amount in cents as an {@link Integer}
     */
    public int toCents() {
        //overlow-safe conversion to cents
        return Math.addExact(Math.multiplyExact(unit, 100), cent);
    }

    //SOF: operations

    /**
     * Sum two {@link Importo} objects.
     * 
     * @param other the {@link Importo} object to be added
     * @return a new {@link Importo} object representing the sum of the two amounts
     * @throws NullPointerException if other is {@code null}
     */
    public Importo add(Importo other) {
        Objects.requireNonNull(other, "other must not be null");
        //overflow check 
        int totalCents = Math.addExact(this.toCents(), other.toCents());
        return fromCents(totalCents);
    }

    /**
     * Subtract two {@link Importo} objects.
     * 
     * @param other the {@link Importo} object to be subtracted
     * @return a new {@link Importo} object representing the difference of the two amounts
     * @throws NullPointerException if other is {@code null}
     * @throws NegativeResultException if the resulting amount is invalid
     */
    public Importo subtract(Importo other) {
        if (this.compareTo(Objects.requireNonNull(other, "other must not be null")) < 0) 
            throw new NegativeResultException("the result of the subtraction must not be negative");
        int totalCents = Math.subtractExact(this.toCents(), other.toCents());
        return fromCents(totalCents);
    }

    /**
     * Multiplies the {@link Importo} by n, a non-negative integer.
     * 
     * @param n the integer to multiply the amount by
     * @return a new {@link Importo} object representing the product of the amount and n
     * @throws NegativeResultException if n is negative
     */
    public Importo multiply(int n) {
        if (n < 0) throw new NegativeResultException("multiplier must be non-negative");
        int totalCents = Math.multiplyExact(this.toCents(), n);
        return fromCents(totalCents);
    }

    /**
     * Integer division of two {@link Importo} objects.
     * 
     * @param other the {@link Importo} object to divide by
     * @return the largest integer n such that n * other ≤ this
     * @throws NullPointerException if other is {@code null}
     * @throws ArithmeticException if other is zero
     */
    public int divide(Importo other) {
        Objects.requireNonNull(other, "other must not be null");
        if (other.toCents() == 0) throw new ArithmeticException("division by zero");
        return this.toCents() / other.toCents();
    }

    //EOF: operations

    @Override
    public int compareTo(Importo other) {
        Objects.requireNonNull(other, "other must not be null");
        return Integer.compare(this.toCents(), other.toCents());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Importo importo = (Importo) obj;
        return unit == importo.unit && cent == importo.cent;
    } 
    
    @Override
    public int hashCode() {
        return Objects.hash(unit, cent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (unit == 0 && cent == 0) return "0";
        if (unit > 0){
            sb.append(unit).append(" unit");
            if (unit > 1) sb.append("s");
        } 
        if (cent > 0) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(cent).append(" cent");
            if (cent > 1) sb.append("s");
        }
        return sb.toString();
    }
}
