package classes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/* 
 * I decide to use an enum here because the set of possible coin values is fixed and known in advance.
 * Using an enum allows for a clear and type-safe representation of these fixed values.
 * Each enum constant corresponds to a specific coin denomination, making the code more readable and maintainable. 
 */

/**
 * Enum class representing different coin denominations.
 * Each enum constant corresponds to a specific coin denomination.
 */
public enum Moneta {
    // Fields

    /** 1 cent coin */
    Cent1(1),
    /** 2 cents coin */
    Cent2(2),
    /** 5 cents coin */
    Cent5(5),
    /** 10 cents coin */
    Cent10(10),
    /** 20 cents coin */
    Cent20(20),
    /** 50 cents coin */
    Cent50(50),
    /** 1 euro coin */
    Euro1(100),
    /** 2 euros coin */
    Euro2(200);

    /**
     * Immutable set of all valid coin representations in {@link Importo} objects.
     * Initialized statically to ensure it is created only once.
     */
    private static final Set<Importo> VALUES = Collections.unmodifiableSet(
        Arrays.stream(Moneta.values())  //create a stream of all enum constants
              .map(Moneta::getValue)   //map each constant to its monetary value (Importo)
              .collect(Collectors.toSet())  //collect the results into a set (without duplicates)
    );

    /*
     * I decided to represent the value of the coin in cents to avoid performance issues (as no superfluous Importo objects are created) 
     * and to avoid dependency on the internal structure of the Importo class.
     * Furthermore, we delegate to the Importo class the management of arithmetic operations and conversions.
     */
    /** Value of the coin in cents, always positive and valid. */
    private final int value;

    //RI and AF not necessary for enum, but for clarity:
    /*-
     * RI:
     *  - value must be one of the predefined amounts, value ∈ {1, 2, 5, 10, 20, 50, 100, 200}
     * 
     * AF:
     *  - associate each enum constant with the monetary value expressed by the attribute value
     *    (Moneta.Cent1 -> value = 1, Moneta.Cent2 -> value = 2, ..., Moneta.Euro2 -> value = 200)
     */

    // Constructor

    /**
     * Constructor for {@link Moneta} enum.
     * 
     * @param value the monetary value of the coin
     */
    //private constructor by default for enum
    Moneta(int value) {
        this.value = value;
    }

    /** 
     * Constructor for {@link Moneta} enum from {@link Importo}.
     * 
     * @param importo the monetary value of the coin as an {@link Importo} object
     * @return the corresponding {@link Moneta} enum constant
     * @throws NullPointerException if importo is {@code null}
     * @throws IllegalArgumentException if importo does not correspond to a valid coin value
     */
    public static Moneta fromImporto(Importo importo) {
        Objects.requireNonNull(importo, "importo ≠ null");
        int cent = importo.toCents();

        for (Moneta moneta : Moneta.values()) {
            if (moneta.value == cent) {
                return moneta;
            }
        }
        throw new IllegalArgumentException("Valore della moneta non valido");
    }

    // Methods

    /**
     * Getter for the monetary value of the coin.
     * 
     * @return the monetary value of the coin as an {@link Importo} object.
     */
    public Importo getValue() {
        return Importo.fromCents(this.value);
    }

    /**
     * Getter for the monetary value of the coin in cents.
     * 
     * @return the monetary value of the coin as an {@link Integer} representation of the value in cents. 
     */
    public int getValueInCents() {
        return this.value;
    }

    /**
     * Static method to get the set of all valid coin values.
     * 
     * @return the set of all valid coin values as an unmodifiable {@link Set}&lt;{@link Importo}&gt;
     */
    public static Set<Importo> getValues() {
        return VALUES;
    }
}
