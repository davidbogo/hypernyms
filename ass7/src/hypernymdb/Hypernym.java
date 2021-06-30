package hypernymdb;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The type hypernymdb.Hypernym.
 */
public class Hypernym implements Comparable<Hypernym> {
    private String hypernym;
    private List<Hyponym> hyponyms;

    /**
     * Instantiates a new hypernymdb.Hypernym.
     *
     * @param name the name
     */
    public Hypernym(String name) {
        hypernym = name.toLowerCase();
        hyponyms = new ArrayList<Hyponym>();
    }

    /**
     * Returns the name string.
     *
     * @return the name string
     */
    public String name() {
        return hypernym;
    }

    /**
     * Gets number of hyponyms.
     *
     * @return the number of hyponyms
     */
    public int getNumberOfHyponyms() {
        return hyponyms.size();
    }

    /**
     * Gets hyponym list.
     *
     * @return the hyponym list
     */
    public List<Hyponym> getHyponymList() {
        return hyponyms;
    }

    /**
     * Compares this hypernym to another one.
     *
     * @param other hypernym
     * @return the result of the comparison
     */
    public int compareTo(Hypernym other) {
        return hypernym.compareToIgnoreCase(other.hypernym);
    }

    /**
     * Register a hyponym.
     *
     * @param newHyponym the new hyponym
     */
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

    /**
     * Find hyponym.
     *
     * @param hyponymName the hyponym name
     * @return the hyponym if found or null otherwise
     */
    public Hyponym findHyponym(String hyponymName) {
        int index = Collections.binarySearch(hyponyms, new Hyponym(hyponymName));
        if (index >= 0) {
            return hyponyms.get(index);
        }
        return null;
    }

    /**
     * Write the hypernym to file.
     *
     * @param writer the buffered writer
     * @throws Exception the exception
     */
    public void writeToFile(BufferedWriter writer) throws Exception {
        String outputStr = hypernym;
        Collections.sort(hyponyms, new Comparator<Hyponym>() {
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
            }
        });

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
