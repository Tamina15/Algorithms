package com.tmn.leetcode;

public class CompareVersionNumbers {

    public int compareVersion(String version1, String version2) {
        String[] a = version1.split("\\.");
        String[] b = version2.split("\\.");
        for (int i = 0; i < a.length || i < b.length; i++) {
            int v1 = 0;
            int v2 = 0;
            try {
                v1 = Integer.parseInt(a[i]);
            } catch (Exception ex) {

            }
            try {
                v2 = Integer.parseInt(b[i]);
            } catch (Exception ex) {

            }
            if (v1 == v2) {
                continue;
            }
            return v1 - v2 < 0 ? -1 : 1;
        }
        return 0;
    }
}
