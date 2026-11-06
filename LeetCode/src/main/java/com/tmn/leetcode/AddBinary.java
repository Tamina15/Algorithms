package com.tmn.leetcode;

public class AddBinary {

    public static String addBinary(String a, String b) {
        int carrier = 0;
        int n = Math.min(a.length(), b.length());
        int m = Math.max(a.length(), b.length());
        StringBuilder sb = new StringBuilder(n);
        for (int i = n - 1; i >= 0; i--) {
            int ca = (int) (a.charAt(i) - '0');
            int cb = (int) (b.charAt(i) - '0');
            int c = ca + cb + carrier;
            if (c > 1) {
                sb.append(c == 2 ? '0' : '1');
                carrier = 1;
            } else {
                sb.append(c == 0 ? '0' : '1');
                carrier = 0;
            }
        }
        for (int i = a.length() - n - 1; i >= 0; i--) {
            int ca = (int) (a.charAt(i) - '0');
            int c = ca + carrier;
            if (c > 1) {
                sb.append('0');
                carrier = 1;
            } else {
                sb.append('1');
                carrier = 0;
            }
        }
        for (int i = b.length() - n - 1; i >= 0; i--) {
            int cb = (int) (b.charAt(i) - '0');
            int c = cb + carrier;
            if (c > 1) {
                sb.append('0');
                carrier = 1;
            } else {
                sb.append('1');
                carrier = 0;
            }
        }
        if (carrier == 1) {
            sb.append('1');
        }
        return sb.reverse().toString();
    }
// not done

    public static void main(String[] args) {
//        System.out.println(addBinary("11010", "1011"));
    }
}
