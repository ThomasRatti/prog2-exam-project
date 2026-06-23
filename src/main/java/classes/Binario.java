package classes;

import java.util.Objects;

import classes.exceptions.CapacityExceededException;
import classes.exceptions.EmptySlotException;
import classes.exceptions.ItemMismatchException;
import classes.exceptions.SizeExceededException;

/**
 * 
 * Class representing a vending machine slot as a {@link Binario} object.
 * 
 * <p>
 * A slot is characterized by a size as a {@link Taglia} object and a maximum capacity as an {@code Integer}.
 * It can contain products of the same type, with size not exceeding
 * the slot's one, up to the maximum capacity.
 * </p>
 */
public final class Binario {
    // Fields

    /** Size of the slot */
    private final Taglia dim;
    /** Maximum capacity of the slot */
    private final int maxCapacity;
    /** Current number of products in the slot */
    private int nProdotti;
    /** The type of product currently in the slot, or null if empty */
    private Prodotto p;

    /*-
     * RI:
     *  - dim ≠ null && dim ∈ Taglia = {Small, Medium, Large}
     *  - maxCapacity > 0
     *  - nProdotti ≥ 0 && nProdotti ≤ maxCapacity
     *  - p = null <=> nProdotti = 0
     *  - p ≠ null => p.dim.size ≤ dim.size
     *  - p ≠ null => all products in the slot are of type p
     *  
     * 
     * AF:
     *  - represent a vending machine slot with its size and maximum capacity; 
     *  - if nProdotti > 0, the slot contains 'nProdotti' of 'p' products,
     *    otherwise it is empty.
     */

    // Constructor

    /**
     * Constructor for {@link Binario} class.
     * 
     * @param dim the size of the slot as a {@link Taglia} object
     * @param maxCapacity the maximum capacity of the slot as an {@code Integer}
     * @throws NullPointerException if dim is {@code null}
     * @throws IllegalArgumentException if maxCapacity is not positive
     */
    private Binario(Taglia dim, int maxCapacity) {
        this.dim =  Objects.requireNonNull(dim, "dim must not be null");
        if (maxCapacity <= 0)
            throw new IllegalArgumentException("maxCapacity must be positive");
        this.maxCapacity = maxCapacity;
        this.nProdotti = 0;
        this.p = null;
    }

    // Methods

    /**
     * Factory method to create a {@link Binario} from a {@link String} representation.
     * 
     * @param input the {@link String} representation of the slot in the format "maxCapacity|size"
     * @return a new {@link Binario} object representing the slot
     * @throws NullPointerException if input is {@code null}
     * @throws IllegalArgumentException if input is improperly formatted or contains invalid values
     */
    public static Binario fromString(String input) {
        Objects.requireNonNull(input, "input must not be null");
        String[] parts = input.split("\\|");
        if (parts.length != 2) throw new IllegalArgumentException("invalid input format");
        final int maxCapacity = Integer.parseInt(parts[0].trim());
        final Taglia dim = Taglia.fromString(parts[1].trim());
        return new Binario(dim, maxCapacity);
    }

    /** 
     * Checks if the {@link Binario} is empty.
     * 
     * @return {@code true} if the slot is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return nProdotti == 0;
    }

    /**
     * Getter for the {@link Prodotto} in the slot.
     * 
     * @return the product in the slot as a {@link Prodotto} object
     * @throws IllegalStateException if the slot is empty
     */
    public Prodotto getProdotto() {
        if (isEmpty()) throw new EmptySlotException("empty");
        return p;
    }

    /**
     * Getter for the remaining capacity of the {@link Binario}.
     * Obtained by subtracting the current number of products from the maximum capacity.
     * 
     * @return the remaining capacity of the slot as an {@code Integer}
     */
    public int getCapacitaRimanente() {
        return maxCapacity - nProdotti;
    }

    /**
     * Adds a number of {@link Prodotto} to the {@link Binario}.
     * 
     * @param p the product to add
     * @param quantity the number of products to add
     * @throws NullPointerException if p is {@code null}
     * @throws IllegalArgumentException if quantity is not positive
     * @throws ItemMismatchException if p is different from the current {@link Prodotto} in the slot
     * @throws CapacityExceededException if quantity > maxCapacity - nProdotti
     * @throws SizeExceededException if p.dim.size > dim.size
     */
    public void carica(Prodotto p, int quantity) {
        Objects.requireNonNull(p, "p must not be null");
        if (quantity <= 0)
            throw new IllegalArgumentException("quantity must be positive");
        if (isEmpty()){
            if (p.getDim().compareTo(dim) > 0)
                throw new SizeExceededException("size");
            if (quantity > maxCapacity)
                throw new CapacityExceededException("capacity");
            this.p = p;
        } else {
            if (!p.equals(this.p))
                throw new ItemMismatchException("item");
            if (quantity + nProdotti > maxCapacity)
                throw new CapacityExceededException("capacity");
        }
        nProdotti += quantity;
    }

    /**
     * Dispense a {@link Prodotto} from the {@link Binario} (if available).
     * 
     * @return the dropped product as a {@link Prodotto} object
     * @throws EmptySlotException if the slot is empty
     */
    public Prodotto dispensa() {
        if (isEmpty()) throw new EmptySlotException("empty");
        Prodotto erogato = p;
        nProdotti--;
        if (nProdotti == 0) {
            p = null;
        }
        return erogato;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return String.format("<-, %s, 0, %d>", dim.toString(), maxCapacity);
        } else {
            return String.format("<%s, %s, %d, %d>", p, dim.toString(), nProdotti, maxCapacity);
        }
    }
}

