package clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import classes.Aggregato;
import classes.Binario;
import classes.Distributore;
import classes.Importo;
import classes.Prodotto;
import classes.exceptions.EmptySlotException;
import classes.exceptions.NegativeResultException;
import classes.exceptions.NotEnoughChangeException;
import classes.exceptions.NotEnoughCoinsException;


/*
 * I decide to use records here to group related data together in a clear, immutable and concise way.
 * Compilator automatically provides constructor, getters, equals, hashCode, and toString methods.
 * Otherwise, I would have used a String[] array or a Object[] array to hold multiple values, 
 * but that would be less type-safe, less readable (e.g. part[0]...part[1]), mutable and not documented.
 */
/**
 * Utility class for parsing a complex input representation of various objects.
 * <p>
 * Used by the main program to interpret input commands and data related to 
 * the vending machine system in {@link UsaDistributore}.
 * </p>
 */
public class ParseUtils {

    /** 
     * Record for holding quantity and product pair 
     * 
     * @param quantita the quantity of the product as {@link Integer}
     * @param prodotto the product as {@link Prodotto}   
     */
    public record QuantitaProdotto(int quantita, Prodotto prodotto) {}

    /** 
     * Record for holding operator and operand pair 
     *
     * @param operatore the operator as {@link Character}
     * @param operando the operand as {@link String}
     */
    public record Operazione(char operatore, String operando) {}

    /** 
     * Record for holding index and aggregato pair
     * 
     * @param indice the index as {@link Integer}
     * @param aggregato the aggregato as {@link Aggregato}
     */
    public record IndiceAggregato(int indice, Aggregato aggregato) {}

    private ParseUtils() {}

    //SOF: General parse methods

    /**
     * Parses a "quantity, product" format string.
     * 
     * @param line the string in format "quantity, product" (e.g., "3, Coca Cola | 1,50 | M")
     * @return a {@link QuantitaProdotto} record containing the parsed values
     * @throws NullPointerException if line is {@code null}
     * @throws IllegalArgumentException if format is empty or invalid
     */
    public static QuantitaProdotto parseQuantitaProdotto(final String line) {
        String[] parts = line.split(",", 2);
        int quantita = Integer.parseInt(parts[0].trim());
        Prodotto prodotto = Prodotto.fromString(parts[1].trim());
        return new QuantitaProdotto(quantita, prodotto);
    }

    /**
     * Parses an "operator operand" format string.
     * 
     * @param line the string in format "operator operand" (e.g., "+ 2x0,50")
     * @return an {@link Operazione} record containing operator char and operand string
     * @throws NullPointerException if line is {@code null}
     * @throws IllegalArgumentException if format is empty or invalid
     */
    public static Operazione parseOperazione(final String line) {
        String[] parts = line.split(" ", 2);
        char operatore = parts[0].charAt(0);
        String operando = parts.length > 1 ? parts[1] : "";
        return new Operazione(operatore, operando);
    }

    /**
     * Parses an "index, aggregato" format string.
     * 
     * @param line the string in format "index, aggregato" (e.g., "0, 1x1,00")
     * @return an {@link IndiceAggregato} record containing the parsed values
     * @throws NullPointerException if line is {@code null}
     * @throws IllegalArgumentException if format is empty or invalid
     */
    public static IndiceAggregato parseIndiceAggregato(final String line) {
        Objects.requireNonNull(line, "line cannot be null");
        String[] parts = line.split(", ", 2);
        int indice = Integer.parseInt(parts[0].trim());
        Aggregato aggregato = Aggregato.fromString(parts[1].trim());
        return new IndiceAggregato(indice, aggregato);
    }

    /**
     * Parses a string and returns the corresponding list of {@link Binario}.
     * 
     * @param line the string representation of the list of {@link Binario}
     * @return the list of {@link Binario} parsed from the input line
     * @throws NullPointerException if line is {@code null}
     * @throws IllegalArgumentException if format is empty or invalid
     */
    public static List<Binario> parseBinari(final String line) {
        Objects.requireNonNull(line, "line cannot be null");
        List<Binario> binari = new ArrayList<>();
        String[] binariStrings = line.split(",");
        for (String str : binariStrings) {
            binari.add(Binario.fromString(str.trim()));
        }
        return binari;
    }    

    //EOF: General parse methods

    //SOF: OperazioniImporti.java parse methods

    /**
     * Parses a string and performs an operation between two amounts of {@link Importo} defined by an input string.
     * 
     * <p> The input string should contain two amounts separated by space, 
     * and an operator (+, -, *, /) and have the format: "Importo" "operator" "Importo|int" 
     * </p>
     * 
     * @param input the input string containing two amounts separated by space
     * @return a string representing the results of the operations (sum, difference, product, integer division),
     *         or "invalid" if the input or operation is improperly formatted, or "negative" if operation violates RI
     * @throws NullPointerException if input is {@code null}
     * @throws IllegalArgumentException if input is empty or invalid 
     */
    public static String eseguiOperazione(final String input) {
        Objects.requireNonNull(input, "input must not be null");
        try {
            String[] parts = input.trim().split("\\s+");
            if (parts.length != 3) return "invalid";
            Importo left = Importo.fromString(parts[0]);
            switch(parts[1]) {
                case "+" -> { return left.add(Importo.fromString(parts[2])).toString(); }
                case "-" -> { return left.subtract(Importo.fromString(parts[2])).toString(); }
                case "*" -> { return left.multiply(Integer.parseInt(parts[2])).toString(); }
                case "/" -> { return String.valueOf(left.divide(Importo.fromString(parts[2]))); }
                default -> { return "invalid"; }
            }           
        } catch (NegativeResultException e) {
            return "negative";
        } catch (IllegalArgumentException | ArithmeticException e) {
            return "invalid";
        }
    }

    //EOF: OperazioniImporti.java parse methods

    //SOF: usaDistributore.java parse methods
    
    /**
     * Parses a string and add products to the corresponding {@link Binario}.
     * 
     * @param line the string representation of the command to add products
     * @param distributore the {@link Distributore} to which to add the products
     * @return the quantity of product that could not be loaded due to lack of space (0 if all were loaded)
     * @throws NullPointerException if line or distributore is {@code null}
     * @throws IllegalArgumentException if format is empty or invalid
     */
    public static int parseCarica(final String line, final Distributore distributore) {
        QuantitaProdotto qp = parseQuantitaProdotto(line);
        return distributore.carica(qp.prodotto(), qp.quantita());
    }

    /**
     * Parses a string and remove a product from the corresponding {@link Binario}.
     * 
     * @param line the string representation of the command to remove a product
     * @param distributore the {@link Distributore} from which to remove the product
     * @return the string representation of the change
     * @throws NullPointerException if line or distributore is {@code null}
     * @throws IllegalArgumentException if format is empty or invalid
     */
    public static String parseEroga(final String line, final Distributore distributore) {
        try{
            IndiceAggregato ia = parseIndiceAggregato(line);
            Aggregato resto = distributore.eroga(ia.indice(), ia.aggregato());
            return resto.toImporto().toCents() == 0 ? " " : resto.toString();
        } catch (IndexOutOfBoundsException | EmptySlotException | 
                 NotEnoughCoinsException | NotEnoughChangeException e) {
            return e.getMessage();
        }
    }

    //EOF: usaDistributore.java parse methods
}