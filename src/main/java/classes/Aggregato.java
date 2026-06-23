package classes;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable class representing a multi-collection of coins as {@link Moneta} with their respective quantities as {@link Integer}s.
 * <p>
 * An {@link Aggregato} is a collection that can hold multiple instances of the same type of coin.
 * Used for representing the payment, change and coin funds in a vending machine system, it can be 
 * created from a list of coins or by adding/removing coins from another {@link Aggregato}.
 * </p>
 */
public final class Aggregato {
    // Fields

    /** Map of coins and their respective quantities */
    private final Map<Moneta, Integer> coins;

    /*-
     * RI:
     *  - coins ≠ null
     *  - coin ≠ null ∀ coin ∈ coins.keySet()
     *  - quantity ≠ null ∀ quantity ∈ coins.values()
     *  - quantity ≥ 0 ∀ quantity ∈ coins.values()
     * 
     * AF:
     *  - represent the collection of coins and their respective quantities 
     *    as Map<Moneta, Integer> = { (coin_1, quantity_1), (coin_2, quantity_2), ..., (coin_n, quantity_n) }
     */

    // Constructor

    /**
     * Constructor for an empty {@link Aggregato}.
     */ 
    Aggregato() {
        this.coins = Collections.unmodifiableMap(new EnumMap<>(Moneta.class));
    }
    
    /**
     * Private constructor for {@link Aggregato} with given map of coins and their quantities.
     * 
     * @param coins the map of coins and their quantities
     * @throws NullPointerException if coins is {@code null}
     * @throws IllegalArgumentException if any quantity in coins is negative
     */
    Aggregato(Map<Moneta, Integer> coins) {
        Objects.requireNonNull(coins, "coins must be non-null");
        coins.forEach((coin, quantity) -> {
            Objects.requireNonNull(coin, "coin must be non-null");
            Objects.requireNonNull(quantity, "quantity must be non-null");
            if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");
        });
        this.coins = Collections.unmodifiableMap(new EnumMap<>(coins));
    }

    // Methods

    /**
     * Factory method for create an {@link Aggregato} with given {@link String}.
     * 
     * @param input the {@link String} representation of the {@link Aggregato} object
     * @return the corresponding {@link Aggregato}
     * @throws NullPointerException if input is {@code null}
     * @throws IllegalArgumentException if input is improperly formatted
     */
    public static Aggregato fromString(String input) {
        Objects.requireNonNull(input, "input must not be null");
        if (input.trim().isEmpty()) return new Aggregato();
        
        Map<Moneta, Integer> tempMap = new EnumMap<>(Moneta.class);
        String[] parts = input.split(",");
        for (String part : parts) {
            String[] subParts = part.trim().split("x");
            if (subParts.length != 2) throw new IllegalArgumentException("invalid input format");
            int quantity = Integer.parseInt(subParts[0].trim());
            Moneta coin = Moneta.fromImporto(Importo.fromString(subParts[1].trim()));
            tempMap.put(coin, Math.addExact(tempMap.getOrDefault(coin, 0), quantity));
        }
        return new Aggregato(tempMap);
    }

    /**
     * Getter for the quantity of a specific coin in the {@link Aggregato}.
     * 
     * @param coin the coin denomination as {@link Moneta} object
     * @return the quantity of the specified coin in the {@link Aggregato}
     * @throws NullPointerException if coin is {@code null}
     */
    public int getQuantita(Moneta coin) {
        Objects.requireNonNull(coin, "coin must not be null");
        return coins.getOrDefault(coin, 0);
    }

    /**
     * Getter for the total amount of {@link Aggregato}.
     * 
     * @return the total amount of {@link Aggregato} as {@link Importo} object
     */
    public Importo toImporto() {
        int totalCents = 0;
        for (Map.Entry<Moneta, Integer> entry : coins.entrySet()) {
            int value= Math.multiplyExact(entry.getKey().getValueInCents(), entry.getValue());
            totalCents = Math.addExact(totalCents, value);
        }
        return Importo.fromCents(totalCents);
    }

    /**
     * Creates a new {@link Aggregato} by adding coins from another {@link Aggregato}.
     * 
     * @param other the other {@link Aggregato} to add coins from
     * @return a new {@link Aggregato} with the combined coins
     * @throws NullPointerException if other is {@code null}
     */
    public Aggregato aggiungi(Aggregato other) {
        Objects.requireNonNull(other, "other must not be null");
        Map<Moneta, Integer> newAgg = new EnumMap<>(Moneta.class);
        newAgg.putAll(coins);

        /*
         *  Caso A: la chiave è non presente in newMonete
         *      si comporta come put: newMonete.put(entry.getKey(), entry.getValue())
         *  Caso B: la chiave è già presente in newMonete
         *      non sovrescrive il valore, ma applica la somma tra il valore esistente e quello nuovo
         */
        other.coins.forEach((coin, quantity) -> 
            newAgg.merge(coin, quantity, (a, b) -> Math.addExact(a, b))
        );
        return new Aggregato(newAgg);
    }

    /**
     * Creates a new {@link Aggregato} by removing coins from another {@link Aggregato}.
     * 
     * @param other the other {@link Aggregato} to remove coins from
     * @return a new {@link Aggregato} with the remaining coins
     * @throws NullPointerException if other is {@code null}
     * @throws IllegalArgumentException with message "value" if the total value to remove exceeds the current {@link Aggregato}'s value
     * @throws IllegalArgumentException with message "coins" if trying to remove more coins than available for any denomination
     */
    public Aggregato rimuovi(Aggregato other) {
        Objects.requireNonNull(other, "other must not be null");
        if (other.toImporto().toCents() > this.toImporto().toCents()) {
            throw new IllegalArgumentException("value");
        }
        Map<Moneta, Integer> newAgg = new EnumMap<>(Moneta.class);
        newAgg.putAll(coins);

        other.coins.forEach((coin, quantityToRemove) -> {
            int currentQuantity = newAgg.getOrDefault(coin, 0);
            if (quantityToRemove > currentQuantity) throw new IllegalArgumentException("coins");

            if (quantityToRemove == currentQuantity) newAgg.remove(coin);
            else newAgg.put(coin, Math.subtractExact(currentQuantity, quantityToRemove));
        });
        return new Aggregato(newAgg);
    }

    /**
     * Checks if the {@link Aggregato} is empty.
     * 
     * @return {@code true} if the {@link Aggregato} is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return coins.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Aggregato aggregato = (Aggregato) obj;
        return coins.equals(aggregato.coins);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coins);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "<>";
        StringBuilder sb = new StringBuilder("<");
        coins.forEach((coin, quantity) -> 
            sb.append(quantity).append(" x ").append(coin.getValue()).append(", ")
        );
        sb.setLength(sb.length() - 2); // Remove the trailing ", "
        sb.append(">");
        return sb.toString();
    }

}
