package classes;

import java.util.Objects;

/**
 * Immutable class representing a product in a vending machine with name, price and size.
 * <p>
 * Products are comparable based on their size as {@link Taglia} objects, name as {@link String} 
 * and price as {@link Importo} objects.
 * </p>
 */
public final class Prodotto implements Comparable<Prodotto>{
    // Fields

    /** Name of the product */
    private final String name;
    /** Price of the product */
    private final Importo price;
    /** Size of the product */
    private final Taglia dim;

    /*-
     * RI:
     *  - nome ≠ null && nome ≠ ""
     *  - prezzo ≠ null
     *  - dim ≠ null && dim ∈ Taglia = {Small, Medium, Large}
     * 
     * AF:
     *  - represent a product with its name, price and size
     */

    // Constructor

    /**
     * Constructor for {@link Prodotto} class.
     * 
     * @param name the name of the product as {@link String}
     * @param price the price of the product as {@link Importo}
     * @param dim the size of the product as {@link Taglia}
     * @throws NullPointerException if name, price or dim is {@code null}
     * @throws IllegalArgumentException if name is empty/blank
     */
    private Prodotto(String name, Importo price, Taglia dim) {
        if (Objects.requireNonNull(name, "name must not be null").trim().isBlank())
            throw new IllegalArgumentException("name must not be empty or blank");
        this.name = name;
        this.price = Objects.requireNonNull(price, "price must not be null");
        this.dim = Objects.requireNonNull(dim, "dim must not be null");
    }

    // Methods

    /**
     * Factory method to create a {@link Prodotto} from {@link String} representations.
     * 
     * @param input the {@link String} representation of the product in the format "name|price|dim"
     * @return a new {@link Prodotto} object
     * @throws NullPointerException if input is {@code null}
     * @throws IllegalArgumentException if input is blank, improperly formatted, or if values/size are invalid
     */
    public static Prodotto fromString(String input) {
        if (Objects.requireNonNull(input, "input must not be null").isBlank())
            throw new IllegalArgumentException("input must not be empty or blank");
        String[] parts = input.split("\\|");
        if (parts.length != 3) {
            throw new IllegalArgumentException("invalid input format");
        }
        String name = parts[0].trim();
        Importo price = Importo.fromString(parts[1].trim());
        Taglia dim = Taglia.fromString(parts[2].trim());
        return new Prodotto(name, price, dim);
    }


    /**
     * Getter for the name of the product.
     * 
     * @return the name of the product as {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the price of the product.
     * 
     * @return the price of the product as {@link Importo}
     */
    public Importo getPrice() {
        return price;
    }

    /**
     * Getter for the size of the product.
     * 
     * @return the size of the product as {@link Taglia}
     */
    public Taglia getDim() {
        return dim;
    } 

    /**
     * Compares this {@link Prodotto} with another {@link Prodotto} for ordering.
     * The comparison is based first on dim, then on name, and finally on price.
     * 
     * @param other the product to be compared
     * @return a negative, zero, or a positive {@link Integer} as this {@link Prodotto}
     *         is less than, equal to, or greater than the specified {@link Prodotto}
     * @throws NullPointerException if other is {@code null}
     */
    @Override
    public int compareTo(Prodotto other) {
        Objects.requireNonNull(other, "other must not be null");
        int sizeComp = Integer.compare(this.dim.ordinal(), other.dim.ordinal());
        if (sizeComp != 0) return sizeComp;
        int nameComp = this.name.compareTo(other.name);
        if (nameComp != 0) return nameComp;
        return this.price.compareTo(other.price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Prodotto prodotto = (Prodotto) obj;
        return name.equals(prodotto.name) && price.equals(prodotto.price) && dim == prodotto.dim;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, dim);
    }

    @Override
    public String toString() {
        return String.format("<%s, %s, %c>", name, price, dim.getSize());
    }
}
