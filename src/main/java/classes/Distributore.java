package classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import classes.exceptions.CapacityExceededException;
import classes.exceptions.EmptySlotException;
import classes.exceptions.ItemMismatchException;
import classes.exceptions.NotEnoughChangeException;
import classes.exceptions.NotEnoughCoinsException;
import classes.exceptions.SizeExceededException;

/**
 * Class representing a vending machine quipped with multiple slots for products as {@link List}&lt;{@link Binario}&gt;,
 * a {@link Aggregato} cash reserve and a strategy for giving change {@link StrategiaResto}.
 * <p>
 * The vending machine can load and remove coins from its cash reserve, load products into its slots,
 * and dispense products to users while providing change according to the selected strategy.
 * </p>
 */
public final class Distributore implements Iterable<String> {

    /** List of vending machine slots */
    private final List<Binario> slots;
    /** Cash available in the vending machine */
    private Aggregato cashFund;
    /** Strategy for giving change */
    private final StrategiaResto strategy;

    /*-
     * RI:
     *  - slots ≠ null && ∀ b ∈ slots: b ≠ null
     *  - cashFund ≠ null
     *  - strategy ≠ null
     * 
     * AF:
     *  - represent a vending machine with its slots(slots), cash reserve (cashFund)
     *    and change strategy (strategy); slots are numbered from 0
     */

    // Constructor

    /**
     * Constructor for empty {@link Distributore}.
     */
    public Distributore() {
        this.slots = new ArrayList<>();
        this.cashFund = new Aggregato();
        this.strategy = new StrategiaRestoHigher();
    }

    /**
     * Constructor for {@link Distributore} with specified slots, cash reserve, and change strategy.
     * 
     * @param slots the list of vending machine slots as {@link List}&lt;{@link Binario}&gt;
     * @param cashFund the initial cash reserve of the vending machine as {@link Aggregato}     
     * @param strategy the strategy for giving change as {@link StrategiaResto}
     * @throws NullPointerException if slots, cashFund, or strategy is {@code null}
     * @throws IllegalArgumentException if slots contains null elements
     */
    public Distributore(List<Binario> slots, Aggregato cashFund, StrategiaResto strategy) {
        Objects.requireNonNull(slots, "slots must not be null");
        for (Binario b : slots) {
            if (b == null) throw new IllegalArgumentException("slots must not contain null elements");
        }
        this.slots = new ArrayList<>(slots);
        this.cashFund = Objects.requireNonNull(cashFund, "cashFund must not be null");
        this.strategy = Objects.requireNonNull(strategy, "strategy must not be null");
    }
    
    // Methods

    /**
     * Getter for {@link Binario} at the specified index in {@link Distributore}.
     * 
     * @param index the index of the vending machine slot as {@link Integer}
     * @return the {@link Binario} at the specified index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public Binario getBinario(int index) {
        if (index < 0 || index >= slots.size())
            throw new IndexOutOfBoundsException("index out of bounds");
        return slots.get(index);
    }

    /**
     * Gets the current cash reserve of {@link Distributore}.
     * 
     * @return the {@link Importo} representing the current cash reserve
     */
    public Importo getFondoCassa() {
        return cashFund.toImporto();
    }

    /**
     * Completely empties {@link Distributore}'s cash reserve.
     * 
     * @return the {@link Aggregato} representing the total cash removed
     */ 
    public Aggregato svuotaCassa() {
        Aggregato rimosso = cashFund;
        cashFund = new Aggregato();
        return rimosso;
    }

    /**
     * Add cash to the {@link Distributore}'s cash reserve.
     * 
     * @param monete the {@link Aggregato} representing the amount of cash to add
     * @throws NullPointerException if monete is {@code null}
     */
    public void aggiungiCassa(Aggregato monete) {
        Objects.requireNonNull(monete, "monete must not be null");
        cashFund = cashFund.aggiungi(monete);
    }

    /**
     * Load a given quantity of a {@link Prodotto} into the {@link Distributore}.
     * <p>
     * Loading start from the first slot (in order of number) that can contain the product,
     * proceeding to subsequent slots if necessary until the quantity is exhausted or there are no more suitable slots.
     * Slots that cannot accept the product (due to size or item mismatch) are silently skipped.
     * </p>
     * 
     * @param p the {@link Prodotto} to load
     * @param quantity the quantity of the product to load as {@link Integer}
     * @return the quantity of product that could not be loaded due to lack of space (0 if all were loaded)
     * @throws NullPointerException if p is {@code null}
     * @throws IllegalArgumentException if quantity is not positive
     */
    public int carica(Prodotto p, int quantity){
        Objects.requireNonNull(p, "p must not be null");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");

        for (Binario b : slots) {
            if (quantity == 0) break;
            
            int spazio = b.getCapacitaRimanente();
            if (spazio > 0) {
                int toLoad = Math.min(spazio, quantity);
                try {
                    b.carica(p, toLoad);
                    quantity -= toLoad;
                } catch (ItemMismatchException | SizeExceededException | CapacityExceededException e) {
                    // Slot not suitable (mismatched item or size) or capacity exceeded, silently skip to next slot  
                }
            }   
        }
        return quantity; // Return remaining quantity that couldn't be loaded
    }


    /**
     * Dispense a product from a specified vending machine {@link Binario}.
     * <p>Dispensing is possible if:
     * <ul>
     *  <li>the slot number is valid and not empty</li>
     *  <li>the inserted amount is sufficient to cover the product price</li>
     *  <li>the cash fund (after adding the inserted amount) can provide the necessary change</li>
     * </ul>
     * 
     * @param nBinario the number of the vending machine {@link Binario} (from 0)
     * @param importoInserito the {@link Aggregato} representing the amount inserted by the user
     * @return the change as an {@link Aggregato}, empty if no change needed
     * @throws NullPointerException if importoInserito is {@code null}
     * @throws IndexOutOfBoundsException if nBinario is invalid (less than 0 or greater than available slots)
     * @throws EmptySlotException if the specified slot is empty
     * @throws NotEnoughCoinsException if importoInserito amount non-positive or insufficient
     * @throws NotEnoughChangeException if the cash fund cannot provide the necessary change
     */
    public Aggregato eroga(int nBinario, Aggregato importoInserito) {
        Objects.requireNonNull(importoInserito, "importoInserito must not be null");
        if (nBinario < 0 || nBinario >= slots.size()) throw new IndexOutOfBoundsException("slot");
        Binario b = slots.get(nBinario);
        if (b.isEmpty()) throw new EmptySlotException("empty");
        Importo prezzoP = b.getProdotto().getPrice();
        Importo totaleInserito = importoInserito.toImporto();
        if (totaleInserito.toCents() <= 0 || totaleInserito.compareTo(prezzoP) < 0)
            throw new NotEnoughCoinsException("value");
        Importo restoNecessario = totaleInserito.subtract(prezzoP);
        Aggregato fondoConImporto = cashFund.aggiungi(importoInserito);
        if (restoNecessario.toCents() == 0 ) {
            cashFund = fondoConImporto;
            b.dispensa();
            return new Aggregato(); // empty change
        }

        Aggregato resto = strategy.calcolaResto(fondoConImporto, restoNecessario);
        if (resto == null) throw new NotEnoughChangeException("change");
        cashFund = fondoConImporto.rimuovi(resto);
        b.dispensa();
        return resto;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String prodotto : this) {
            sb.append("? ").append(prodotto).append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns an iterator over the products in the vending machine.
     * Each element is a string in the format "index | name | price".
     * Only non-empty slots are included.
     * 
     * @return an iterator over the product descriptions
     */
    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int currentIndex = 0;
            private int nextValidIndex = -1;

            private int findNextValid() {
                for (int i = currentIndex; i < slots.size(); i++) {
                    if (!slots.get(i).isEmpty()) {
                        return i;
                    }
                }
                return -1;
            }

            @Override
            public boolean hasNext() {
                if (nextValidIndex == -1) {
                    nextValidIndex = findNextValid();
                }
                return nextValidIndex != -1;
            }

            @Override
            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Binario b = slots.get(nextValidIndex);
                Prodotto p = b.getProdotto();
                String result = nextValidIndex + " | " + p.getName() + " | " + p.getPrice();
                currentIndex = nextValidIndex + 1;
                nextValidIndex = -1;
                return result;
            }
        };
    }
}
