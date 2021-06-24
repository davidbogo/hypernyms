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
    private static List<HypernymInfo>   hypernyms;
    private static SentenceProcessor    sentenceProcessor;

    public static void main(String[] args) {
        if (args.length == 2) {
            hypernyms = new ArrayList<HypernymInfo>();
            sentenceProcessor = new SentenceProcessor();
            if (readCorpus(args[0])) {

//======================================================================================
                HypernymInfo curHypernym = new HypernymInfo("Transport");
                curHypernym.registerHyponym(new HyponymInfo("Car"));
                curHypernym.registerHyponym(new HyponymInfo("Car"));
                curHypernym.registerHyponym(new HyponymInfo("Bicycle"));
                curHypernym.registerHyponym(new HyponymInfo("Bicycle"));
                curHypernym.registerHyponym(new HyponymInfo("Bicycle"));
                curHypernym.registerHyponym(new HyponymInfo("Train"));
                registerHypernym(curHypernym);

                curHypernym = new HypernymInfo("Animal");
                curHypernym.registerHyponym(new HyponymInfo("Cat"));
                curHypernym.registerHyponym(new HyponymInfo("dog"));
                curHypernym.registerHyponym(new HyponymInfo("mouse"));
                curHypernym.registerHyponym(new HyponymInfo("Butterfly"));
                registerHypernym(curHypernym);

                curHypernym = new HypernymInfo("Transport");
                curHypernym.registerHyponym(new HyponymInfo("Jetfoil"));
                curHypernym.registerHyponym(new HyponymInfo("Bicycle"));
                curHypernym.registerHyponym(new HyponymInfo("Bicycle"));
                curHypernym.registerHyponym(new HyponymInfo("Train"));
                curHypernym.registerHyponym(new HyponymInfo("Jetfoil"));
                registerHypernym(curHypernym);

                curHypernym = new HypernymInfo("Animal");
                curHypernym.registerHyponym(new HyponymInfo("Cat"));
                curHypernym.registerHyponym(new HyponymInfo("Cat"));
                curHypernym.registerHyponym(new HyponymInfo("Cat"));
                curHypernym.registerHyponym(new HyponymInfo("Cat"));
                curHypernym.registerHyponym(new HyponymInfo("Cat"));
                curHypernym.registerHyponym(new HyponymInfo("dog"));
                curHypernym.registerHyponym(new HyponymInfo("mouse"));
                curHypernym.registerHyponym(new HyponymInfo("Butterfly"));
                curHypernym.registerHyponym(new HyponymInfo("Dog"));
                curHypernym.registerHyponym(new HyponymInfo("Fox"));
                registerHypernym(curHypernym);

//======================================================================================

                saveDatabase(args[1], 1);
            }
        } else {
            System.out.println("Exactly 2 parameters are expected: path to corpus directory and output file name");
        }
    }

    private static void registerHypernym(HypernymInfo newHypernym) {
        int index = Collections.binarySearch(hypernyms, newHypernym);
        if (index >= 0) {
            // A hypernym with this name already exists. We'll just add the new hyponyms to it
            HypernymInfo existingHypernym = hypernyms.get(index);
            List<HyponymInfo> newHyponymList = newHypernym.getHyponymList();
            for (HyponymInfo hyponym : newHyponymList) {
                existingHypernym.registerHyponym(hyponym);
            }
        } else {
            index = -index - 1;
            hypernyms.add(index, newHypernym);
        }
    }

    private static void readSingleCorpusFile(BufferedReader bufReader) throws Exception {
        HypernymInfo hypernym;
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
            for (HypernymInfo hypernym: hypernyms) {
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
