import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class HypernymDatabaseManager {
    private List<Hypernym>       hypernyms;
    private SentenceProcessor    sentenceProcessor;

    public HypernymDatabaseManager() {
        hypernyms = new ArrayList<Hypernym>();
        sentenceProcessor = new SentenceProcessor();
    }

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

    private void registerHypernyms(List<Hypernym> newHypernyms) {
        for (Hypernym hypernym : newHypernyms) {
            registerHypernym(hypernym);
        }
    }

    private void readSingleCorpusFile(BufferedReader bufReader) throws Exception {
        Pattern pattern = Pattern.compile("\\.");
        String line;
        while ((line = bufReader.readLine()) != null) {
            if (line.length() > 1) {
                List<Hypernym> hypernyms;
                if (line.contains(".")) {
                    String[] sentenceArray = pattern.split(line);
                    for (String sentence : sentenceArray) {
                        hypernyms = sentenceProcessor.process(sentence);
                        if (hypernyms != null) {
                            registerHypernyms(hypernyms);
                        }
                    }
                } else {
                    hypernyms = sentenceProcessor.process(line);
                    if (hypernyms != null) {
                        registerHypernyms(hypernyms);
                    }
                }
            }
        }
    }

    public boolean readCorpus(String corpusDir)  {
        boolean success = true;
        try {
            File dir = new File(corpusDir);
            String[] fileNames = dir.list();
            for (String fileName : fileNames) {
                String fullFileName = corpusDir;
                if (fullFileName.charAt(fullFileName.length() - 1) != '\\'){
                    fullFileName += "\\";
                }
                fullFileName += fileName;
                System.out.println("Processing corpus file: " + fullFileName);
                readSingleCorpusFile(new BufferedReader(new FileReader(fullFileName)));
            }
        } catch (Exception except) {
            success = false;
            System.out.println("An error occurred while reading the corpus");
        }

        return success;
    }

    public boolean saveDatabase(String databasePath, int minHyponyms) {
        boolean success = true;
        try {
            BufferedWriter bufWriter = new BufferedWriter(new FileWriter(databasePath));
            for (Hypernym hypernym: hypernyms) {
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

    public void printoutMatchingHypernyms(String lemma) {
    }
}
