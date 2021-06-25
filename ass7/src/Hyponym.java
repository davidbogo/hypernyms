import java.lang.Comparable;

public class Hyponym implements Comparable<Hyponym> {
    private String      hyponym;
    private int         numOccurences;

    public Hyponym(String name) {
        hyponym = name.toLowerCase();
        numOccurences = 1;
    }

    public String name() {
        return hyponym;
    }

    public int getNumOfOccurrences() {
        return numOccurences;
    }

    public void increaseOccurrences(int num) {
        numOccurences += num;
    }

    public int compareTo(Hyponym other) {
        return hyponym.compareToIgnoreCase(other.hyponym);
    }
}
