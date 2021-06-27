import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.Comparable;

public class Hypernym implements Comparable<Hypernym> {
    private String hypernym;
    private List<Hyponym> hyponyms;

    public Hypernym(String name) {
        hypernym = name.toLowerCase();
        hyponyms = new ArrayList<Hyponym>();
    }

    public String name() {
        return hypernym;
    }

    public int getNumberOfHyponyms() {
        return hyponyms.size();
    }

    public List<Hyponym> getHyponymList() {
        return hyponyms;
    }

    public int compareTo(Hypernym other) {
        return hypernym.compareToIgnoreCase(other.hypernym);
    }

    public void registerHyponym(Hyponym newHyponym) {
        int index = Collections.binarySearch(hyponyms, newHyponym);
        if (index >= 0) {
            // A hyponym with this name already exists. We'll just increase its number of occurrences
            Hyponym existingHyponym = hyponyms.get(index);
            existingHyponym.increaseOccurrences(newHyponym.getNumOfOccurrences());
        } else {
            index = -index - 1;
            hyponyms.add(index, newHyponym);
        }
    }

    public Hyponym findHyponym(String hyponymName) {
        int index = Collections.binarySearch(hyponyms, new Hyponym(hyponymName));
        if (index >= 0) {
            return hyponyms.get(index);
        }
        return null;
    }

    public void writeToFile(BufferedWriter writer) throws Exception {
        String outputStr = hypernym;
        Collections.sort(hyponyms, new Comparator<Hyponym>(){
            public int compare(Hyponym h1, Hyponym h2) {
                // We need the hyponyms to be sorted in reverse order, from the highest
                // number of occurrences down to the lowest
                if (h1.getNumOfOccurrences() > h2.getNumOfOccurrences()) {
                    return -1;
                }
                if (h1.getNumOfOccurrences() < h2.getNumOfOccurrences()) {
                    return 1;
                }
                return h1.name().compareToIgnoreCase(h2.name());
            }});

        boolean isFirst = true;
        for (Hyponym hyponym: hyponyms) {
            if (isFirst) {
                isFirst = false;
                outputStr += ": ";
            } else {
                outputStr += ", ";
            }
            outputStr += hyponym.name();
            outputStr += " (";
            outputStr += Integer.toString(hyponym.getNumOfOccurrences());
            outputStr += ")";
        }
        writer.append(outputStr);
        writer.newLine();
    }
}
