package classes;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract base class for change calculation strategies using a greedy approach.
 * Subclasses define the order in which coins are considered.
 * <p>
 * Greedy strategies are fast and efficient but may fail to find a solution even if one exists.
 * Subclasses can override the coin order in which coins are considered for change.
 * </p>
 */
public abstract class AbstractStrategia implements StrategiaResto {

    /**
     * Defoult constructor for {@link AbstractStrategia}.
     */
    protected AbstractStrategia() {}

    /**
     * Returns the array of {@link Moneta} in the order defined by the strategy.
     * <p> To ensure immutability, it returns a copy </p>
     * 
     * @return the ordered array of coins as {@link Moneta} objects
     */
    protected Moneta[] getOrdineMonete(){
        return Arrays.copyOf(MONETE_DECRESCENTI, MONETE_DECRESCENTI.length);
    };

    /**
     * Calculates the change to be given from the available cash fund using a greedy algorithm.
     * <p>
     * Greedy algorithms are not optimal and may fail to find a solution even if one exists.
     * For guaranteed optimal solutions, consider using {@link StrategiaRestoOptimal}
     */
    @Override
    public Aggregato calcolaResto(Aggregato fondoCassa, Importo importoResto) {
        Objects.requireNonNull(fondoCassa, "Il fondo cassa non può essere null");
        Objects.requireNonNull(importoResto, "L'importo del resto non può essere null");
    
        int restoInCents = importoResto.toCents();
        if (restoInCents == 0) return new Aggregato();

        Map<Moneta, Integer> resto = new EnumMap<>(Moneta.class);  
                                
        for (Moneta moneta : getOrdineMonete()) {
            int valoreMoneta = moneta.getValueInCents();
            int quantitaDisponibile = fondoCassa.getQuantita(moneta);
            int quantitaDaUsare = Math.min(restoInCents / valoreMoneta, quantitaDisponibile);
            
            if (quantitaDaUsare > 0) {
                resto.put(moneta, quantitaDaUsare);
                restoInCents -= quantitaDaUsare * valoreMoneta;
            }
        }

        return restoInCents == 0 ? new Aggregato(resto) : null;
    }
}
