/**
 * The type Text fragment.
 */
public class TextFragment {
    private String text;
    private boolean isNounPhrase;
    private boolean isCommaAndOr;
    private boolean isHyperHypoPatternKeyword;
    private boolean isSplitHyperHypoPatternKeywordPart1;
    private boolean isSplitHyperHypoPatternKeywordPart2;
    private boolean isHypoHyperPatternKeyword;

    /**
     * Instantiates a new Text fragment.
     */
    public TextFragment() {
        text = "";
        isNounPhrase = false;
        isCommaAndOr = false;
        isHyperHypoPatternKeyword = false;
        isSplitHyperHypoPatternKeywordPart1 = false;
        isSplitHyperHypoPatternKeywordPart2 = false;
        isHypoHyperPatternKeyword = false;
    }

    /**
     * Gets the fragment's text.
     *
     * @return the text
     */
    String getText() {
        return text;
    }

    /**
     * Sets the fragment's text.
     *
     * @param newText the new text
     */
    void setText(String newText) {
        text = newText;
    }

    /**
     * Checks whether the fragment is a noun phrase.
     *
     * @return the boolean (noun phrase or not)
     */
    boolean isNounPhrase() {
        return isNounPhrase;
    }

    /**
     * Mark the fragment as a noun phrase.
     *
     * @param nounPhrase the boolean noun phrase flag
     */
    void markAsNounPhrase(boolean nounPhrase) {
        isNounPhrase = nounPhrase;
    }

    /**
     * Checks whether the fragment is a comma, "and" or "or".
     *
     * @return true if the fragment is a comma, "and" or "or", false otherwise
     */
    boolean isCommaAndOr() {
        return isCommaAndOr;
    }

    /**
     * Mark the fragment as a comma, "and" or "or".
     *
     * @param commaAndOr specifies whether the fragment is to be marked as a comma, "and" or "or".
     */
    void markAsCommaAndOr(boolean commaAndOr) {
        isCommaAndOr = commaAndOr;
    }

    /**
     * Checks whether the fragment is a pattern keyword of the type where the hypernym name appears first,
     * then the keyword, then the hyponyms.
     *
     * @return true if the fragment is a pattern keyword of this type, false otherwise
     */
    boolean isHyperHypoPatternKeyword() {
        return isHyperHypoPatternKeyword;
    }

    /**
     * Mark the fragment as a pattern keyword of the type where the hypernym name appears first,
     * then the keyword, then the hyponyms.
     *
     * @param hyperHypoPatternKeyword specifies whether the fragment is a pattern keyword of this type
     */
    void markAsHyperHypoPatternKeyword(boolean hyperHypoPatternKeyword) {
        isHyperHypoPatternKeyword = hyperHypoPatternKeyword;
    }

    /**
     * Checks whether the fragment is a pattern keyword that appears at the beginning of the pattern
     * followed by the hypernym, an addtitional keyword and, finally, the hyponyms.
     *
     * @return true if the fragment is a pattern keyword of this type, false otherwise
     */
    boolean isSplitHyperHypoPatternKeywordPart1() {
        return isSplitHyperHypoPatternKeywordPart1;
    }

    /**
     * Mark the fragment as a pattern keyword that appears at the beginning of the pattern
     * followed by the hypernym, an addtitional keyword and, finally, the hyponyms.
     *
     * @param splitHyperHypoPatternKeywordPart1 specifies whether the fragment is a pattern keyword of this type
     */
    void markAsSplitHyperHypoPatternKeywordPart1(boolean splitHyperHypoPatternKeywordPart1) {
        isSplitHyperHypoPatternKeywordPart1 = splitHyperHypoPatternKeywordPart1;
    }

    /**
     * Checks whether the fragment is the second pattern keyword that appears after the first one
     * and the hypernym, and is followed by the hyponyms.
     *
     * @return true if the fragment is a pattern keyword of this type, false otherwise
     */
    boolean isSplitHyperHypoPatternKeywordPart2() {
        return isSplitHyperHypoPatternKeywordPart2;
    }

    /**
     * Mark the fragment as the second pattern keyword that appears after the first one
     * and the hypernym, and is followed by the hyponyms.
     *
     * @param splitHyperHypoPatternKeywordPart2 specifies whether the fragment is a pattern keyword of this type
     */
    void markAsSplitHyperHypoPatternKeywordPart2(boolean splitHyperHypoPatternKeywordPart2) {
        isSplitHyperHypoPatternKeywordPart2 = splitHyperHypoPatternKeywordPart2;
    }

    /**
     * Checks whether the fragment is a pattern keyword of the type where the hyponym appears first,
     * then the keyword, then the hypernym.
     *
     * @return true if the fragment is a pattern keyword of this type, false otherwise
     */
    boolean isHypoHyperPatternKeyword() {
        return isHypoHyperPatternKeyword;
    }

    /**
     * Mark the fragment as a pattern keyword of the type where the hyponym appears first,
     * then the keyword, then the hypernym.
     *
     * @param hypoHyperPatternKeyword specifies whether the fragment is a pattern keyword of this type
     */
    void markAsHypoHyperPatternKeyword(boolean hypoHyperPatternKeyword) {
        isHypoHyperPatternKeyword = hypoHyperPatternKeyword;
    }
}
