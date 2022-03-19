package com.josh;

import java.util.Arrays;

/**
 * Uses a heap to ascertain that the brackets match
 */
public class BracketSearch {
    /** contains unmatched opening brackets */
    private String[] unmatchedBrackets = {};
    private String[][] bracketPairs = { { "[", "(", "{" }, { "]", ")", "}" } };

    public static void main(String[] args) {
        String[] brackets = { "[{}]", "(()())", "{]", "[()]))()", "[]{}({})" };

        BracketSearch search = new BracketSearch();

        for (String s : brackets) {
            if (search.bracketsMatch(s))
                System.out.println("'" + s + "' = valid.");
            else
                System.out.println("'" + s + "' = invalid.");
        }
    }

    /**
     * checks whether a given string of brackets has matching pairs
     */
    public boolean bracketsMatch(String brackets) {
        String currentBracket;
        for (int i = 0; i < brackets.length(); ++i) {
            currentBracket = String.valueOf(brackets.charAt(i));
            if (!isBracket(currentBracket))
                throw new IllegalArgumentException("Invalid bracket '" + currentBracket + "'");

            if (isOpeningBracket(currentBracket)) {
                // append opening bracket to stack of unmatched brackets
                unmatchedBrackets = Arrays.copyOf(unmatchedBrackets, unmatchedBrackets.length + 1);
                unmatchedBrackets[unmatchedBrackets.length - 1] = currentBracket;
            } else if (isClosingBracket(currentBracket)) {
                if (matchesLatestOpeningBracket(currentBracket))
                    // pop matching opening bracket from stack of unmatched brackets
                    unmatchedBrackets = Arrays.copyOf(unmatchedBrackets, unmatchedBrackets.length - 1);
                else
                    // there is an opening bracket that is yet to be closed
                    return false;
            }
        }
        return true;
    }

    private boolean matchesLatestOpeningBracket(String closingBracket) {
        String[] opening = bracketPairs[0];
        String[] closing = bracketPairs[1];

        for (int i = 0; i < closing.length; ++i) {
            if (closing[i].equals(closingBracket)) {
                if (opening[i].equals(getLatestOpeningBracket()))
                    return true;
                break;
            }
        }

        return false;
    }

    private String getLatestOpeningBracket() {
        if (unmatchedBrackets.length < 1)
            return null;

        return unmatchedBrackets[unmatchedBrackets.length - 1];
    }

    private boolean isOpeningBracket(String character) {
        String[] opening = bracketPairs[0];

        for (String i : opening) {
            if (i.equals(character))
                return true;
        }
        return false;
    }

    private boolean isClosingBracket(String character) {
        String[] closing = bracketPairs[1];

        for (String i : closing) {
            if (i.equals(character))
                return true;
        }
        return false;
    }

    private boolean isBracket(String character) {
        return isOpeningBracket(character) || isClosingBracket(character);
    }
}
