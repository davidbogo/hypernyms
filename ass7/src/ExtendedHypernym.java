public class ExtendedHypernym implements Comparable<ExtendedHypernym> {
    Hypernym hypernym;
    int numberOfOccurrences;

    ExtendedHypernym(Hypernym h, int occurrences) {
        hypernym = h;
        numberOfOccurrences = occurrences;
    }

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

    String name() {
        return hypernym.name();
    }

    int getNumberOfOccurrences() {
        return numberOfOccurrences;
    }
}
