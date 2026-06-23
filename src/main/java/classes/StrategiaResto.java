 package classes;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Interface representing a strategy for giving change.
 * <p>
 * Different implementations can define various algorithms for calculating the change to be given
 * from the available cash fund in a vending machine.
 * </p>
 */
public interface StrategiaResto {
    /**
     * Method to calculate the change as an {@link Aggregato} to be given from the available cash fund, {@link Aggregato} object.
     * 
     * @param fondoCassa the current cash reserve of the vending machine as {@link Aggregato}
     * @param importoResto the amount of change to be given as {@link Importo}
     * @return an {@link Aggregato} representing the coins present in cash fund to be given as change,   
     *         an empty {@link Aggregato} if no change is needed 
     *         or {@code null} if exact change cannot be given
     * @throws NullPointerException if fondoCassa or importoResto is {@code null}
     */
    Aggregato calcolaResto(Aggregato fondoCassa, Importo importoResto);

    /**
     * Coins ordered from largest to smallest value.
     */
    Moneta[] MONETE_DECRESCENTI = {
        Moneta.Euro2, Moneta.Euro1, Moneta.Cent50,
        Moneta.Cent20, Moneta.Cent10, Moneta.Cent5,
        Moneta.Cent2, Moneta.Cent1
    };

    /**
     * Coins ordered from smallest to largest value.
     */
    Moneta[] MONETE_CRESCENTI = {
        Moneta.Cent1, Moneta.Cent2, Moneta.Cent5,
        Moneta.Cent10, Moneta.Cent20, Moneta.Cent50,
        Moneta.Euro1, Moneta.Euro2
    };

    /**
     * Build a map of available coins from the cash fund as {@link Aggregato}.
     * 
     * @param fondoCassa the cash fund as {@link Aggregato}
     * @return a map of available coins and their quantities
     */
    static Map<Moneta, Integer> moneteDisponibili(Aggregato fondoCassa) {
        Objects.requireNonNull(fondoCassa, "fondoCassa must not be null");
        Map<Moneta, Integer> disponibili = new EnumMap<>(Moneta.class);
        for (Moneta m : Moneta.values()) {
            int qty = fondoCassa.getQuantita(m);
            if (qty > 0) disponibili.put(m, qty);
        }
        return disponibili;
    }
}
