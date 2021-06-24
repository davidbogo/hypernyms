import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.Comparable;

public class HypernymInfo implements Comparable<HypernymInfo> {
    private String      hypernym;
    List<HyponymInfo>   hyponyms;

    public HypernymInfo(String name) {
        hypernym = name.toLowerCase();
        hyponyms = new ArrayList<HyponymInfo>();
    }

    public String name() {
        return hypernym;
    }

    public int getNumberOfHyponyms() {
        return hyponyms.size();
    }

    public List<HyponymInfo> getHyponymList() {
        return hyponyms;
    }

    public int compareTo(HypernymInfo other) {
        return hypernym.compareToIgnoreCase(other.hypernym);
    }

    public void registerHyponym(HyponymInfo newHyponym) {
        int index = Collections.binarySearch(hyponyms, newHyponym);
        if (index >= 0) {
            // A hyponym with this name already exists. We'll just increase its number of occurrences
            HyponymInfo existingHyponym = hyponyms.get(index);
            existingHyponym.increaseOccurrences(newHyponym.getNumOfOccurrences());
        } else {
            index = -index - 1;
            hyponyms.add(index, newHyponym);
        }
    }

    public void writeToFile(BufferedWriter writer) throws Exception {
        String outputStr = hypernym;
        Collections.sort(hyponyms);

        Collections.sort(hyponyms, new Comparator<HyponymInfo>(){
            public int compare(HyponymInfo h1, HyponymInfo h2) {
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
        for (HyponymInfo hyponym: hyponyms) {
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
