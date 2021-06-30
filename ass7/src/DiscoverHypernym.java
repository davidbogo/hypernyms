/**
 * The type Discover hypernym.
 */
public class DiscoverHypernym {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            HypernymDatabaseManager hypernymDatabaseManager = new HypernymDatabaseManager();
            if (hypernymDatabaseManager.readCorpus(args[0])) {
                hypernymDatabaseManager.printoutMatchingHypernyms(args[1]);
            }
        } else {
            System.out.println("Exactly 2 parameters are expected: path to corpus directory and the lemma");
        }
    }
}
