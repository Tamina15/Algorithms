package com.tmn.algorithm;

/**
 *
 * @author nhat.tranminh
 */
public class KMPStringSearch {

    // https://www.youtube.com/watch?v=JoF0Z7nVSrA
    public static int search(String haystack, String needle) {
        char[] h = haystack.toCharArray();
        char[] n = needle.toCharArray();
        int[] lps = new int[needle.length()]; // least prefix suffix
        int prev = 0;
        for (int i = 1; i < n.length;) {
            if (n[prev] == n[i]) {
                lps[i] = ++prev;
                i++;
            } else {
                if (prev == 0) {
                    lps[i] = 0;
                    i++;
                } else {
                    prev = lps[prev - 1];
                }
            }
        }
        for (int i = 0; i < h.length;) {
            for (int j = 0; j < n.length;) {
                if (h[i] == n[j]) {
                    i++;
                    j++;
                } else {
                    if (j == 0) {
                        i++;
                    } else {
                        j = lps[j - 1];
                    }
                }
                if (j == n.length) {
                    return i - n.length;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String s = "aaaxaaaax";
        String a = "aaaa";
        int i = search(s, a);
        if (i >= 0) {
            System.out.println(s);
            System.out.println(" ".repeat(i) + a);

        } else {
            System.out.println("Not found");
        }
    }
}
