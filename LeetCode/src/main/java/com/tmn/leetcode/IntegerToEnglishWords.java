package com.tmn.leetcode;

import java.util.HashMap;
import java.util.Map;

public class IntegerToEnglishWords {

    public static final Map<Integer, String> map = new HashMap();
    public static final String MIN = "Negative Two Billion One Hundred Forty Seven Million Four Hundred Eighty Three Thousand Six Hundred Forty Eight";

    static {
        map.put(1, "One");
        map.put(2, "Two");
        map.put(3, "Three");
        map.put(4, "Four");
        map.put(5, "Five");
        map.put(6, "Six");
        map.put(7, "Seven");
        map.put(8, "Eight");
        map.put(9, "Nine");
        map.put(10, "Ten");
        map.put(11, "Eleven");
        map.put(12, "Twelve");
        map.put(13, "Thirteen");
        map.put(14, "Fourteen");
        map.put(15, "Fifteen");
        map.put(16, "Sixteen");
        map.put(17, "Seventeen");
        map.put(18, "Eighteen");
        map.put(19, "Nineteen");
        map.put(20, "Twenty");
        map.put(30, "Thirty");
        map.put(40, "Forty");
        map.put(50, "Fifty");
        map.put(60, "Sixty");
        map.put(70, "Seventy");
        map.put(80, "Eighty");
        map.put(90, "Ninety");
    }

    public static String numberToWords(int num) {
        if (num == 0) {
            return "Zero";
        }
        if (num == Integer.MIN_VALUE) {
            return MIN;
        }
        String sign = num < 0 ? "Negative " : "";
        return sign + billions(Math.abs(num)).trim().replaceAll("\\s+", " ");
    }

    public static String tens(int num) {

        if (num > 0 && num <= 20) {
            return map.get(num);
        }
        int a = num / 10;
        String t = "";
        if (a > 0) {
            t = map.get(a * 10);
        }
        int b = num % 10;
        String d = "";
        if (b > 0) {
            d = " " + map.get(b);
        }
        return t + d;
    }

    public static String hundreds(int num) {
        int a = num / 100;
        String h = "";
        if (a > 0) {
            h = map.get(a) + " Hundred ";
        }
        return h + tens(num % 100);
    }

    public static String thousands(int num) {
        int a = num / 1000;
        String h = "";
        if (a > 0) {
            h = hundreds(a) + " Thousand ";
        }
        return h + hundreds(num % 1000);
    }

    public static String millions(int num) {
        int a = num / 1000000;
        String h = "";
        if (a > 0) {
            h = thousands(a) + " Million ";
        }
        return h + thousands(num % 1000000);
    }

    public static String billions(int num) {
        int a = num / 1_000_000_000;
        String b = "";
        if (a > 0) {
            b = thousands(a) + " Billion ";
        }
        return b + millions(num % 1_000_000_000);
    }

    public static void main(String[] args) {
        System.out.println(numberToWords(Integer.MIN_VALUE).trim().replaceAll("\\s+", " "));
    }
}
