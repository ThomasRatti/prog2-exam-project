package classes;

/**
 * Class extending AbstractStrategia for representing a strategy that gives change preferring smaller coins first.
 * <p>
 * This approach maximises the number of coins given as change, which may be useful to reduce 
 * the stock of smaller denominations.
 * For guaranteed optimal solutions, consider using {@link StrategiaRestoOptimal}.
 * </p>
 */
public final class StrategiaRestoLower extends AbstractStrategia {
    /**
     * Constructs a new {@link StrategiaRestoLower} instance.
     */
    public StrategiaRestoLower() {}

    @Override
    protected Moneta[] getOrdineMonete() {
        return MONETE_CRESCENTI.clone();
    }
}
