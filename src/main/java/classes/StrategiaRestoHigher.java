package classes;

/**
 * Class extending AbstractStrategia for representing a strategy that gives change preferring larger coins first.
 * <p>
 * This minimizes the number of coins in most cases but may fail when a solution exists with smaller coins.
 * For guaranteed optimal solutions, consider using {@link StrategiaRestoOptimal}.
 * </p>
 */
public final class StrategiaRestoHigher extends AbstractStrategia {
    /**
     * Constructs a new {@link StrategiaRestoHigher} instance.
     */
    public StrategiaRestoHigher() {}

    @Override
    protected Moneta[] getOrdineMonete() {
        return MONETE_DECRESCENTI.clone();
    }
}