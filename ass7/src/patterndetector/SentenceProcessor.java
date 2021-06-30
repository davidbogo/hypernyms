package patterndetector;

import hypernymdb.Hypernym;
import hypernymdb.Hyponym;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Sentence processor.
 */
public class SentenceProcessor {
    /**
     * Process the sentence.
     *
     * @param sentence the sentence to process
     * @return the list of hypernyms extracted from the sentence
     */
    public static List<Hypernym> process(String sentence) {
        List<Hypernym> hypernyms = new ArrayList<Hypernym>();
        List<TextFragment> fragments = splitIntoFragments(sentence);
        analyzeFragments(fragments);
        while (fragments.size() > 1) {
            int processedFragments = findAndProcessPattern(fragments, hypernyms);
            for (int i = 0; i < processedFragments; i++) {
                fragments.remove(0);
            }
        }

        return hypernyms;
    }

    /**
     * Split the sentence into fragments. Each fragment is either a noun phrase or a text
     * between the noun phrases.
     *
     * @param sentence the sentence to split
     * @return the list of fragments
     */
    private static List<TextFragment> splitIntoFragments(String sentence) {
        List<TextFragment> fragments = new ArrayList<TextFragment>();
        String remainingText = sentence;
        boolean lookingForNounPhraseStart = true;
        while (remainingText.length() > 0) {
            TextFragment fragment = new TextFragment();
            String fragmentText = "";
            if (lookingForNounPhraseStart) {
                int nounPhraseStartIndex = remainingText.indexOf("<np>");
                if (nounPhraseStartIndex < 0) {
                    fragmentText = remainingText;
                    remainingText = ""; // This is the last fragment
                } else {
                    fragmentText = remainingText.substring(0, nounPhraseStartIndex);
                    remainingText = remainingText.substring(nounPhraseStartIndex + 4);
                    // We're skipping the 4 characters of the noun phrase start marker (<np>)
                    lookingForNounPhraseStart = false;
                }
                fragment.markAsNounPhrase(false);
            } else { // Already found a noun phrase start, now looking for the end
                int nounPhraseEndIndex = remainingText.indexOf("</np>");
                if (nounPhraseEndIndex < 0) {
                    // Generally, this shouldn't happen as there should be no unmatched <np> markers in a sentence,
                    // but if this happens anyway, we'll treat the rest of the sentence as a noun phrase
                    fragmentText = remainingText;
                    remainingText = ""; // This is the last fragment
                } else {
                    fragmentText = remainingText.substring(0, nounPhraseEndIndex);
                    remainingText = remainingText.substring(nounPhraseEndIndex + 5);
                    // We're skipping the 5 characters of the noun phrase end marker (</np>)
                    lookingForNounPhraseStart = true;
                }
                fragment.markAsNounPhrase(true);
            }
            fragmentText = fragmentText.trim();
            if (fragmentText.length() > 0) {
                // Some fragments might have ended up empty due to trimming
                fragment.setText(fragmentText);
                fragments.add(fragment);
            }
        }
        return fragments;
    }

    /**
     * Copmares the strings while ignoring any leading spaces or commas.
     *
     * @param mainString the main string
     * @param searchString a search string
     * @return true if the strings are equal, false otherwise
     */
    private static boolean equalsIgnoreLeadingCommasAndSpaces(String mainString, String searchString) {
        boolean equals = false;
        if (mainString.toLowerCase().endsWith(searchString)) {
            equals = true;
            int searchStringIndex = mainString.length() - searchString.length();
            for (int i = 0; i < searchStringIndex; i++) {
                if ((mainString.charAt(i) != ' ') && (mainString.charAt(i) != ',')) {
                    equals = false;
                    break;
                }
            }
        }
        return equals;
    }

    /**
     * Analyzes the fragments and records the type of each of them.
     *
     * @param fragments the list of fragments to analyze
     */
    private static void analyzeFragments(List<TextFragment> fragments) {
        for (TextFragment fragment : fragments) {
            if (!fragment.isNounPhrase()) {
                String fragmentText = fragment.getText();
                if (fragmentText.equals(",")
                        || equalsIgnoreLeadingCommasAndSpaces(fragmentText, "and")
                        || equalsIgnoreLeadingCommasAndSpaces(fragmentText, "or")) {
                    fragment.markAsCommaAndOr(true);
                } else if (equalsIgnoreLeadingCommasAndSpaces(fragmentText, "such as")
                        || equalsIgnoreLeadingCommasAndSpaces(fragmentText, "including")
                        || equalsIgnoreLeadingCommasAndSpaces(fragmentText, "especially")) {
                    fragment.markAsHyperHypoPatternKeyword(true);
                } else if (equalsIgnoreLeadingCommasAndSpaces(fragmentText, "which is")
                        || equalsIgnoreLeadingCommasAndSpaces(fragmentText, "which is an example of")
                        || equalsIgnoreLeadingCommasAndSpaces(fragmentText, "which is a kind of")
                        || equalsIgnoreLeadingCommasAndSpaces(fragmentText, "which is a class of")) {
                    fragment.markAsHypoHyperPatternKeyword(true);
                } else if (fragmentText.toLowerCase().endsWith("such")) {
                    // Unlike with other pattern markers that always appear between noun phrases,
                    // "such" is a special case where we don't test for exact match. Rather, we look
                    // for any text fragments ending with "such"
                    fragment.markAsSplitHyperHypoPatternKeywordPart1(true);
                } else if (fragmentText.equalsIgnoreCase("as")) {
                    fragment.markAsSplitHyperHypoPatternKeywordPart2(true);
                }
            }
        }
    }

    /**
     * Find the first pattern marker among the segments.
     *
     * @param fragments the list of fragments
     * @return the first pattern marker index or -1 if none was found
     */
    private static int findFirstPatternMarker(List<TextFragment> fragments) {
        int firstPatternMarkerIndex = -1;
        int curIndex = 0;
        for (TextFragment fragment : fragments) {
            if (fragment.isHyperHypoPatternKeyword() || fragment.isHypoHyperPatternKeyword()) {
                firstPatternMarkerIndex = curIndex;
                break;
            }
            if (fragment.isSplitHyperHypoPatternKeywordPart1()) {
                // Finding the first part of a split pattern marker isn't enough,
                // we'll only treat it as a start of pattern if we find the second
                // part at the expected index (curIndex + 2)
                if (curIndex + 2 < fragments.size()) {
                    TextFragment expectedSecondPartFragment = fragments.get(curIndex + 2);
                    if (expectedSecondPartFragment.isSplitHyperHypoPatternKeywordPart2()) {
                        firstPatternMarkerIndex = curIndex;
                        break;
                    }
                }
            }
            curIndex++;
        }
        return firstPatternMarkerIndex;
    }

    /**
     * Finds and processes a pattern in the text represented as a list of fragments.
     *
     * @param fragments the list of fragments
     * @param hypernyms the list to add the hypernym to, if found
     * @return number of processed fragments
     */
    private static int findAndProcessPattern(List<TextFragment> fragments, List<Hypernym> hypernyms) {
        int processedFragments = fragments.size();
        int firstPatternMarkerIndex = findFirstPatternMarker(fragments);
        if (firstPatternMarkerIndex >= 0) {
            TextFragment patternMarker = fragments.get(firstPatternMarkerIndex);
            if (patternMarker.isHyperHypoPatternKeyword()) {
                processedFragments = processHyperHypoPattern(fragments, firstPatternMarkerIndex, hypernyms);
            } else if (patternMarker.isHypoHyperPatternKeyword()) {
                processedFragments = processHypoHyperPattern(fragments, firstPatternMarkerIndex, hypernyms);
            } else if (patternMarker.isSplitHyperHypoPatternKeywordPart1()) {
                processedFragments = processSplitHyperHypoPattern(fragments, firstPatternMarkerIndex, hypernyms);
            }
        }
        return processedFragments;
    }

    /**
     * Processes a pattern of the type where the hypernym appears first, then the keyword, then the hyponyms.
     *
     * @param fragments the list of fragments making up the pattern
     * @param patternMarkerIndex the index of the fragment containing the pattern keyword
     * @param hypernyms the list to add the hypernym to, after it's created
     * @return number of processed fragments
     */
    private static int processHyperHypoPattern(List<TextFragment> fragments, int patternMarkerIndex,
                                               List<Hypernym> hypernyms) {
        int processedFragments = fragments.size();

        if (patternMarkerIndex == 0) {
            // Such marker shouldn't appear at the beginning of a sentence.
            // We'll just skip it
            processedFragments = 1;
        } else {
            TextFragment hypernymFragment = fragments.get(patternMarkerIndex - 1);
            if (hypernymFragment.isNounPhrase()) {
                Hypernym hypernym = new Hypernym(hypernymFragment.getText());
                int curIndex = patternMarkerIndex + 1;
                while (curIndex < fragments.size()) {
                    TextFragment curFragment = fragments.get(curIndex);
                    if (curFragment.isNounPhrase()) {
                        hypernym.registerHyponym(new Hyponym(curFragment.getText()));
                    } else {
                        if (!curFragment.isCommaAndOr()) {
                            // We've reached the end of the pattern. This fragment doesn't belong to either
                            // this or next pattern and can be just skipped
                            processedFragments = curIndex + 1;
                            break;
                        }
                    }
                    curIndex++;
                }
                hypernyms.add(hypernym);
            } else {
                // This should never happen. Skipping...
                processedFragments = 1;
            }
        }
        return processedFragments;
    }

    /**
     * Processes a pattern of the type where the hyponym appears first, then the keyword, then the hypernym.
     *
     * @param fragments the list of fragments making up the pattern
     * @param patternMarkerIndex the index of the fragment containing the pattern keyword
     * @param hypernyms the list to add the hypernym to, after it's created
     * @return number of processed fragments
     */
    private static int processHypoHyperPattern(List<TextFragment> fragments, int patternMarkerIndex,
                                               List<Hypernym> hypernyms) {
        int processedFragments = fragments.size();
        if (patternMarkerIndex == 0) {
            // Such marker shouldn't appear at the beginning of a sentence.
            // We'll just skip it
            processedFragments = 1;
        } else {
            if (patternMarkerIndex < fragments.size() - 1) {
                TextFragment hyponymFragment = fragments.get(patternMarkerIndex - 1);
                TextFragment hypernymFragment = fragments.get(patternMarkerIndex + 1);
                if (hyponymFragment.isNounPhrase() && hypernymFragment.isNounPhrase()) {
                    Hypernym hypernym = new Hypernym(hypernymFragment.getText());
                    hypernym.registerHyponym(new Hyponym(hyponymFragment.getText()));
                    hypernyms.add(hypernym);
                    processedFragments = patternMarkerIndex + 2;
                } else {
                    // This should never happen. Skipping...
                    processedFragments = 1;
                }
            }
        }
        return processedFragments;
    }

    /**
     * Processes a pattern of the type where the first keyword appears at the beginning of
     * the pattern followed by the hypernym, a second keyword and, finally, the hyponyms.
     *
     * @param fragments the list of fragments making up the pattern
     * @param patternMarkerIndex the index of the fragment containing the first pattern keyword
     * @param hypernyms the list to add the hypernym to, after it's created
     * @return number of processed fragments
     */
    private static int processSplitHyperHypoPattern(List<TextFragment> fragments, int patternMarkerIndex,
                                                    List<Hypernym> hypernyms) {
        int processedFragments = fragments.size();
        if (patternMarkerIndex == 0) {
            // Such marker shouldn't appear at the beginning of a sentence.
            // We'll just skip it
            processedFragments = 1;
        } else {
            TextFragment hypernymFragment = fragments.get(patternMarkerIndex + 1);
            if (hypernymFragment.isNounPhrase()) {
                Hypernym hypernym = new Hypernym(hypernymFragment.getText());
                int curIndex = patternMarkerIndex + 3;
                // Skipping over the hypernym and the second part of the pattern
                while (curIndex < fragments.size()) {
                    TextFragment curFragment = fragments.get(curIndex);
                    if (curFragment.isNounPhrase()) {
                        hypernym.registerHyponym(new Hyponym(curFragment.getText()));
                    } else {
                        if (!curFragment.isCommaAndOr()) {
                            // We've reached the end of the pattern. This fragment doesn't belong to the pattern,
                            // it will be processed later
                            processedFragments = curIndex;
                            break;
                        }
                    }
                    curIndex++;
                }
                hypernyms.add(hypernym);
            } else {
                // This should never happen. Skipping...
                processedFragments = 1;
            }
        }
        return processedFragments;
    }
}
