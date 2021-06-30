
/**
 * The type Hyponym.
 */
public class Hyponym implements Comparable<Hyponym> {
    private String hyponym;
    private int numOccurences;

    /**
     * Instantiates a new Hyponym.
     *
     * @param name the hypnym name
     */
    public Hyponym(String name) {
        hyponym = name.toLowerCase();
        numOccurences = 1;
    }

    /**
     * Returns the name string.
     *
     * @return the name string
     */
    public String name() {
        return hyponym;
    }

    /**
     * Gets number of occurrences.
     *
     * @return the number of occurrences
     */
    public int getNumOfOccurrences() {
        return numOccurences;
    }

    /**
     * Increase the number of occurrences.
     *
     * @param num the number to increase by
     */
    public void increaseOccurrences(int num) {
        numOccurences += num;
    }

    /**
     * Compares this hyponym to another one.
     *
     * @param other hyponym to compare to
     * @return the result of the comparison
     */
    public int compareTo(Hyponym other) {
        return hyponym.compareToIgnoreCase(other.hyponym);
    }
}
