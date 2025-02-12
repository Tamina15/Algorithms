package com.tmn.leetcode;

import java.util.ArrayDeque;

public class EvaluateReversePolishNotation {

    public static int evalRPN(String[] tokens) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        for (String s : tokens) {
            switch (s) {
                case "+" -> {
                    int a = stack.pop();
                    int b = stack.pop();
                    stack.push(a + b);
                }
                case "-" -> {
                    int a = stack.pop();
                    int b = stack.pop();
                    stack.push(b - a);
                }
                case "*" -> {
                    int a = stack.pop();
                    int b = stack.pop();
                    stack.push(a * b);
                }
                case "/" -> {
                    int a = stack.pop();
                    int b = stack.pop();
                    stack.push(b / a);
                }
                default ->
                    stack.push(Integer.valueOf(s));
            }
        }
        return stack.getLast();
    }

    public static void main(String[] args) {
        String[] tokens = {"4","13","5","/","+"};
        System.out.println(evalRPN(tokens));
    }
}
