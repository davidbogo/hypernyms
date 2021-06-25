import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class CreateHypernymDatabase {
    private static List<Hypernym>   hypernyms;
    private static SentenceProcessor    sentenceProcessor;

    public static void main(String[] args) {
        if (args.length == 2) {
            hypernyms = new ArrayList<Hypernym>();
            sentenceProcessor = new SentenceProcessor();
            if (readCorpus(args[0])) {

//======================================================================================
                Hypernym curHypernym = new Hypernym("Transport");
                curHypernym.registerHyponym(new Hyponym("Car"));
                curHypernym.registerHyponym(new Hyponym("Car"));
                curHypernym.registerHyponym(new Hyponym("Bicycle"));
                curHypernym.registerHyponym(new Hyponym("Bicycle"));
                curHypernym.registerHyponym(new Hyponym("Bicycle"));
                curHypernym.registerHyponym(new Hyponym("Train"));
                registerHypernym(curHypernym);

                curHypernym = new Hypernym("Animal");
                curHypernym.registerHyponym(new Hyponym("Cat"));
                curHypernym.registerHyponym(new Hyponym("dog"));
                curHypernym.registerHyponym(new Hyponym("mouse"));
                curHypernym.registerHyponym(new Hyponym("Butterfly"));
                registerHypernym(curHypernym);

                curHypernym = new Hypernym("Transport");
                curHypernym.registerHyponym(new Hyponym("Jetfoil"));
                curHypernym.registerHyponym(new Hyponym("Bicycle"));
                curHypernym.registerHyponym(new Hyponym("Bicycle"));
                curHypernym.registerHyponym(new Hyponym("Train"));
                curHypernym.registerHyponym(new Hyponym("Jetfoil"));
                registerHypernym(curHypernym);

                curHypernym = new Hypernym("Animal");
                curHypernym.registerHyponym(new Hyponym("Cat"));
                curHypernym.registerHyponym(new Hyponym("Cat"));
                curHypernym.registerHyponym(new Hyponym("Cat"));
                curHypernym.registerHyponym(new Hyponym("Cat"));
                curHypernym.registerHyponym(new Hyponym("Cat"));
                curHypernym.registerHyponym(new Hyponym("dog"));
                curHypernym.registerHyponym(new Hyponym("mouse"));
                curHypernym.registerHyponym(new Hyponym("Butterfly"));
                curHypernym.registerHyponym(new Hyponym("Dog"));
                curHypernym.registerHyponym(new Hyponym("Fox"));
                registerHypernym(curHypernym);

//======================================================================================

                saveDatabase(args[1], 1);
            }
        } else {
            System.out.println("Exactly 2 parameters are expected: path to corpus directory and output file name");
        }
    }

    private static void registerHypernym(Hypernym newHypernym) {
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

    private static void readSingleCorpusFile(BufferedReader bufReader) throws Exception {
        Hypernym hypernym;
        Pattern pattern = Pattern.compile("\\.");
        String line;
        while ((line = bufReader.readLine()) != null) {
            if (line.length() > 1) {
                if (line.contains(".")) {
                    String[] sentenceArray = pattern.split(line);
                    for (String sentence : sentenceArray) {
                        hypernym = sentenceProcessor.process(sentence);
                        if (hypernym != null) {
                            registerHypernym(hypernym);
                        }
                    }
                } else {
                    hypernym = sentenceProcessor.process(line);
                    if (hypernym != null) {
                        registerHypernym(hypernym);
                    }
                }
            }
        }
    }

    private static boolean readCorpus(String corpusDir)  {
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

    private static boolean saveDatabase(String databasePath, int minHyponyms) {
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
}
