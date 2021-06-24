import java.lang.Comparable;

public class HyponymInfo implements Comparable<HyponymInfo> {
    private String      hyponym;
    private int         numOccurences;

    public HyponymInfo(String name) {
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

    public int compareTo(HyponymInfo other) {
        return hyponym.compareToIgnoreCase(other.hyponym);
    }
}
