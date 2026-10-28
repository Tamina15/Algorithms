package com.tmn.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class CountWordFrequency {

    public static Map<String, Integer> countWordFrequency(String s) {
        StringTokenizer st = new StringTokenizer(s, " ");
        Map<String, Integer> map = new HashMap<>(st.countTokens());
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            map.merge(token.strip().toLowerCase(), 1, Integer::sum);
        }
        return map;
    }

    public static void main(String[] args) {
        var map = countWordFrequency("Toi yeu Java va Java yeu toi");
        System.out.println(map);
    }
}
