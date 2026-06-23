package classes;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Strategy that calculates change using the minimum number of coins possible.
 * <p>
 * Unlike greedy approaches (Higher/Lower), this strategy uses recursive backtracking
 * to find the optimal solution that minimizes the total number of coins given as change.
 * </p>
 */
public final class StrategiaRestoOptimal implements StrategiaResto {

    /**
     * Constructs a new {@link StrategiaRestoOptimal} instance.
     */
    public StrategiaRestoOptimal() {}

    /**
     * Calculates the change using the minimum number of coins.
     * Uses recursive backtracking to explore all possible combinations.
     */
    @Override
    public Aggregato calcolaResto(Aggregato fondoCassa, Importo importoResto) {
        Objects.requireNonNull(fondoCassa, "fondoCassa must not be null");
        Objects.requireNonNull(importoResto, "importoResto must not be null");

        int restoInCents = importoResto.toCents();
        if (restoInCents == 0) return new Aggregato();

        // Map to store available coins and their quantities
        Map<Moneta, Integer> disponibili = StrategiaResto.moneteDisponibili(fondoCassa);

        // Find optimal solution using recursion
        Map<Moneta, Integer> corrente = new EnumMap<>(Moneta.class);
        Map<Moneta, Integer> migliore = new EnumMap<>(Moneta.class);
        int[] minMonete = {Integer.MAX_VALUE}; // Array to allow modification in recursion

        trovaResto(
            0, restoInCents,
            disponibili, corrente, migliore, minMonete, 0
        );

        return minMonete[0] == Integer.MAX_VALUE ? null : new Aggregato(migliore);
    }

    /**
     * Recursive method to find the optimal change combination.
     *
     * @param indice current index in the coins array
     * @param resto remaining amount to give as change (in cents)
     * @param disponibili available coins in cash fund
     * @param corrente current combination being built
     * @param migliore best combination found so far
     * @param minMonete minimum number of coins found (array for mutability)
     * @param numMoneteCorrente current number of coins in this combination
     */
    private void trovaResto(
            int indice, int resto,
            Map<Moneta, Integer> disponibili,
            Map<Moneta, Integer> corrente,
            Map<Moneta, Integer> migliore,
            int[] minMonete, int numMoneteCorrente) {

        // Base case: exact change found
        if (resto == 0) {
            if (numMoneteCorrente < minMonete[0]) {
                minMonete[0] = numMoneteCorrente;
                migliore.clear();
                migliore.putAll(corrente);
            }
            return;
        }

        // Base case: no more coins to try, impossible or exceeded best solution
        if (indice >= MONETE_DECRESCENTI.length ||
            resto < 0 ||
            numMoneteCorrente >= minMonete[0]) return;

        // try all quantities of the current coin denomination
        Moneta moneta = MONETE_DECRESCENTI[indice];
        int maxUsabili = Math.min(resto / moneta.getValueInCents(), disponibili.getOrDefault(moneta, 0));

        for (int qty = maxUsabili; qty >= 0; qty--) {
            //add coins to current combination
            if (qty > 0) corrente.put(moneta, qty);

            // Recur to try next coin denomination
            trovaResto(
                indice + 1, resto - (qty * moneta.getValueInCents()),   //next coin and reduced amount
                disponibili, corrente, migliore, minMonete, 
                numMoneteCorrente + qty                                 //update coin count
            );

            // Backtrack: remove coins from current combination
            if (qty > 0) corrente.remove(moneta);
        }
    }
}
