import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The type Hypernym database manager.
 */
public class HypernymDatabaseManager {
    private List<Hypernym>       hypernyms;
    private SentenceProcessor    sentenceProcessor;

    /**
     * Instantiates a new Hypernym database manager.
     */
    public HypernymDatabaseManager() {
        hypernyms = new ArrayList<Hypernym>();
        sentenceProcessor = new SentenceProcessor();
    }

    /**
     * Register a hypernym.
     *
     * @param newHypernym hypernym to register
     */
    private void registerHypernym(Hypernym newHypernym) {
        int index = Collections.binarySearch(hypernyms, newHypernym);
        if (index >= 0) {
            // A hypernym with this name already exists. We'll just add the new hyponyms to it
            Hypernym existingHypernym = hypernyms.get(index);
            List<Hyponym> newHyponymList = newHypernym.getHyponymList();
            for (Hyponym hyponym : newHyponymList) {
                existingHypernym.registerHyponym(hyponym);
            }
        } else {
            index = -index - 1;
            hypernyms.add(index, newHypernym);
        }
    }

    /**
     * Register multiple hypernyms.
     *
     * @param newHypernyms list of hypernyms to register
     */
    private void registerHypernyms(List<Hypernym> newHypernyms) {
        for (Hypernym hypernym : newHypernyms) {
            registerHypernym(hypernym);
        }
    }

    /**
     * Read a single file from the corpus.
     *
     * @param bufReader the buffered reader
     * @throws Exception if something goes wrong
     */
    private void readSingleCorpusFile(BufferedReader bufReader) throws Exception {
        Pattern pattern = Pattern.compile("\\.");
        String line;
        while ((line = bufReader.readLine()) != null) {
            if (line.length() > 1) {
                List<Hypernym> tempHypernyms;
                String[] sentenceArray = pattern.split(line);
                for (String sentence : sentenceArray) {
                    tempHypernyms = sentenceProcessor.process(sentence);
                    if (tempHypernyms != null) {
                        registerHypernyms(tempHypernyms);
                    }
                }
            }
        }
    }

    /**
     * Read the corpus.
     *
     * @param corpusDir the corpus directory
     * @return true if successful, false otherwise
     */
    public boolean readCorpus(String corpusDir)  {
        boolean success = true;
        try {
            File dir = new File(corpusDir);
            String[] fileNames = dir.list();
            for (String fileName : fileNames) {
                String fullFileName = corpusDir;
                if (fullFileName.charAt(fullFileName.length() - 1) != '\\') {
                    fullFileName += "\\";
                }
                fullFileName += fileName;
                // System.out.println("Processing corpus file: " + fullFileName);
                readSingleCorpusFile(new BufferedReader(new FileReader(fullFileName)));
            }
        } catch (Exception except) {
            success = false;
            System.out.println("An error occurred while reading the corpus");
        }

        return success;
    }

    /**
     * Save the database to a file.
     *
     * @param databasePath the database file path
     * @param minHyponyms min required number of hyponyms. If a hypernym has less hyponyms, it's skipped
     * @return true if successful, false otherwise
     */
    public boolean saveDatabase(String databasePath, int minHyponyms) {
        boolean success = true;
        try {
            BufferedWriter bufWriter = new BufferedWriter(new FileWriter(databasePath));
            for (Hypernym hypernym : hypernyms) {
                if (hypernym.getNumberOfHyponyms() >= minHyponyms) {
                    hypernym.writeToFile(bufWriter);
                }
            }
            bufWriter.close();
        } catch (Exception except) {
            success = false;
            System.out.println("An error occurred while saving the database");
        }

        return success;
    }

    /**
     * Printout matching hypernyms.
     *
     * @param lemma the lemma to look for in hypernyms
     */
    public void printoutMatchingHypernyms(String lemma) {
        List<ExtendedHypernym> hypernymsSortedByOccurrences = new ArrayList<ExtendedHypernym>();
        for (Hypernym hypernym : hypernyms) {
            Hyponym matchingHyponym = hypernym.findHyponym(lemma);
            if (matchingHyponym != null) {
                hypernymsSortedByOccurrences.add(new ExtendedHypernym(hypernym, matchingHyponym.getNumOfOccurrences()));
            }
        }
        if (hypernymsSortedByOccurrences.size() > 0) {
            Collections.sort(hypernymsSortedByOccurrences);
            for (ExtendedHypernym extendedHypernym : hypernymsSortedByOccurrences) {
                System.out.println(extendedHypernym.name()
                        + ": ("
                        + Integer.toString(extendedHypernym.getNumberOfOccurrences())
                        + ")");
            }
        } else {
            System.out.println("The lemma doesn't appear in the corpus");
        }
    }
}
