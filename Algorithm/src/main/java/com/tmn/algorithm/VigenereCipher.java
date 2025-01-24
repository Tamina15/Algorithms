/*
 * To change this license header, choose License Headers in Project Properties.
 * To chtange this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

import java.util.Scanner;

/**
 *
 * @author Tran Minh Nhat
 */
public class VigenereCipher {

    String key = "";

    public VigenereCipher() {
    }

    public VigenereCipher(String key) {
        this.key = key.toLowerCase();
    }

    public String Encode(String plaintext) {
        return Encode(key, plaintext);
    }

    public static String Encode(String key, String plaintext) {
        String result = "";
        int m = key.length();
        int offset = 0;
        for (int i = 0; i < plaintext.length(); i++) {
            char x = plaintext.charAt(i);
            if (Character.isLowerCase(x)) {
                /* 
                ([char] - 'a') : character index in the English alphabet, for lowercase character
                ([char] + 'a') : character index in the ASCII table, for lowercase character
                offset: offset for non-English chracter (number, ...)
                i - offset: position if character at offset position not exists, i.e. skip the non-English character and continue
                (key.charAt((i - offset) % m): corresponded character in the key, an alternative way instead of repeating key, e.g. keykeykeyke
                 */
                //C  =              (P        +   K)                                   mod 26
                result += (char) ((((x - 'a') + ((key.charAt((i - offset) % m)) - 'a')) % 26) + 'a');
            } else if (Character.isUpperCase(x)) {
                result += (char) ((((x - 'A') + ((key.charAt((i - offset) % m)) - 'a')) % 26) + 'A');
            } else {
                result += x;
                offset++;
            }
        }
        return result;
    }

    public String Decode(String cipher) {
        return Decode(key, cipher);
    }

    public static String Decode(String key, String cipher) {
        String result = "";
        int m = key.length();
        int offset = 0;
        for (int i = 0; i < cipher.length(); i++) {
            char x = cipher.charAt(i);
            if (Character.isLowerCase(x)) {
                /* 
                ([char] - 'a') : character index in the English alphabet, for lowercase characcter
                ([char] + 'a') : character index in the ASCII table, for lowercase characcter
                offset: offset for non-English chracter (number, ...)
                i - offset: position if character at offset position not exists, i.e. skip the non-English character and continue with current index
                (key.charAt((i - offset) % m): corresponded character in the key, an alternative way instead of repeating key, e.g. keykeykeyke
                 */
                //P  =               (C        -    K)                                   mod 26
                //                                                                               + 26) % 26 if result is negative
                result += (char) (((((x - 'a') - (((key.charAt((i - offset) % m)) - 'a')) % 26)) + 26) % 26 + 'a');
            } else if (Character.isUpperCase(x)) {
                result += (char) (((((x - 'A') - (((key.charAt((i - offset) % m)) - 'a')) % 26)) + 26) % 26 + 'A');
            } else {
                result += x;
                offset++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String key = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input keyword: ");
        key = scanner.nextLine();
        System.out.println("Input Plaintext: ");
        String text = scanner.nextLine();
        VigenereCipher vi = new VigenereCipher(key);
        String cipher = vi.Encode(text);
        System.out.println("Encoded: " + cipher);
        String plaintext = vi.Decode(cipher);
        System.out.println("Decoded: " + plaintext);
    }
}
