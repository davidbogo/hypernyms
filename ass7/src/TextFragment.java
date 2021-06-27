public class TextFragment {
    private String text;
    private boolean isNounPhrase;
    private boolean isCommaAndOr;
    private boolean isHyperHypoPatternKeyword;
    private boolean isSplitHyperHypoPatternKeywordPart1;
    private boolean isSplitHyperHypoPatternKeywordPart2;
    private boolean isHypoHyperPatternKeyword;

    public TextFragment() {
        text = "";
        isNounPhrase = false;
        isCommaAndOr = false;
        isHyperHypoPatternKeyword = false;
        isSplitHyperHypoPatternKeywordPart1 = false;
        isSplitHyperHypoPatternKeywordPart2 = false;
        isHypoHyperPatternKeyword = false;
    }

    String getText() {
        return text;
    }

    void setText(String newText) {
        text = newText;
    }

    boolean isNounPhrase() {
        return isNounPhrase;
    }

    void markAsNounPhrase(boolean nounPhrase) {
        isNounPhrase = nounPhrase;
    }

    boolean isCommaAndOr() {
        return isCommaAndOr;
    }

    void markAsCommaAndOr(boolean commaAndOr) {
        isCommaAndOr = commaAndOr;
    }

    boolean isHyperHypoPatternKeyword() {
        return isHyperHypoPatternKeyword;
    }

    void markAsHyperHypoPatternKeyword(boolean hyperHypoPatternKeyword) {
        isHyperHypoPatternKeyword = hyperHypoPatternKeyword;
    }

    boolean isSplitHyperHypoPatternKeywordPart1() {
        return isSplitHyperHypoPatternKeywordPart1;
    }

    void markAsSplitHyperHypoPatternKeywordPart1(boolean splitHyperHypoPatternKeywordPart1) {
        isSplitHyperHypoPatternKeywordPart1 = splitHyperHypoPatternKeywordPart1;
    }

    boolean isSplitHyperHypoPatternKeywordPart2() {
        return isSplitHyperHypoPatternKeywordPart2;
    }

    void markAsSplitHyperHypoPatternKeywordPart2(boolean splitHyperHypoPatternKeywordPart2) {
        isSplitHyperHypoPatternKeywordPart2 = splitHyperHypoPatternKeywordPart2;
    }

    boolean isHypoHyperPatternKeyword() {
        return isHypoHyperPatternKeyword;
    }

    void markAsHypoHyperPatternKeyword(boolean hypoHyperPatternKeyword) {
        isHypoHyperPatternKeyword = hypoHyperPatternKeyword;
    }
}
