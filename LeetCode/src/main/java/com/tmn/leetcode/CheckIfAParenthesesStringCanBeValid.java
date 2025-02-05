package com.tmn.leetcode;

import java.util.Stack;

public class CheckIfAParenthesesStringCanBeValid {

    public static boolean canBeValid(String s, String locked) {
        if (s.length() % 2 != 0) {
            return false;
        }
        char[] c = s.toCharArray();
        char[] l = locked.toCharArray();
        Stack<Integer> open = new Stack<>();
        Stack<Integer> wild = new Stack<>();
        for (int i = 0; i < c.length; i++) {
            if (l[i] == '0') {
                wild.push(i);
            } else {
                switch (c[i]) {
                    case '(' ->
                        open.push(i);
                    case ')' -> {
                        if (!open.empty()) {
                            open.pop();
                        } else {
                            if (!wild.empty()) {
                                wild.pop();
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        while (!open.empty()) {
            int t = open.pop();
            if (wild.empty() || t > wild.peek()) {
                return false;
            } else {
                wild.pop();
            }
        }
        return wild.size() % 2 == 0;
    }
}
