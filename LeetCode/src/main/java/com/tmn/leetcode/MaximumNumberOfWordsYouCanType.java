package com.tmn.leetcode;

public class MaximumNumberOfWordsYouCanType {

    public int canBeTypedWords(String text, String brokenLetters) {
        char[] brokens = brokenLetters.toCharArray();
        boolean[] isBroken = new boolean[26];
        for (int i = 0; i < brokens.length; i++) {
            isBroken[brokens[i] - 'a'] = true;
        }
        boolean broken = false;
        char[] texts = text.toCharArray();
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = texts[i];
            if (c == ' ') {
                if (!broken) {
                    count++;
                }
                broken = false;
                continue;
            }
            if (!broken) {
                broken = isBroken[c - 'a'];
            }
        }
        // Count the last word
        if (!broken) {
            count++;
        }
        return count;
    }
}
