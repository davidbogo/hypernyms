/**
 * The type Extended hypernym.
 */
public class ExtendedHypernym implements Comparable<ExtendedHypernym> {
    /**
     * The Hypernym.
     */
    private Hypernym hypernym;
    /**
     * The Number of occurrences.
     */
    private int numberOfOccurrences;

    /**
     * Instantiates a new Extended hypernym.
     *
     * @param h           the hypernym
     * @param occurrences the occurrences
     */
    ExtendedHypernym(Hypernym h, int occurrences) {
        hypernym = h;
        numberOfOccurrences = occurrences;
    }

    /**
     * Compares this extended hypernym to another one.
     *
     * @param other the hypernym
     * @return the comparison result
     */
    public int compareTo(ExtendedHypernym other) {
        if (numberOfOccurrences > other.numberOfOccurrences) {
            // We need the hypernyms to be sorted in reverse order, from the highest
            // number of the relevant lemma's occurrences down to the lowest
            return -1;
        }
        if (numberOfOccurrences < other.numberOfOccurrences) {
            return 1;
        }
        return hypernym.name().compareToIgnoreCase(other.hypernym.name());
    }

    /**
     * Returns the name string.
     *
     * @return the name string
     */
    String name() {
        return hypernym.name();
    }

    /**
     * Gets number of occurrences.
     *
     * @return the number of occurrences
     */
    int getNumberOfOccurrences() {
        return numberOfOccurrences;
    }
}
