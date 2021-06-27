public class DiscoverHypernym {
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
