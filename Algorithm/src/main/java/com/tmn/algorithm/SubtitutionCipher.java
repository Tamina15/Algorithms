/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Tran Minh Nhat
 */
public class SubtitutionCipher {

    String key = "abcdefghijklmnopqrstuvwxyz";
    String reverse_key = "abcdefghijklmnopqrstuvwxyz";
    String plaintext = "";

    public SubtitutionCipher() {
    }

    public SubtitutionCipher(String url) {
        ReadFile(url);
        reverse_key = DecodeKey(key);
    }

    private void ReadFile(String url) {
        File f = new File(url);
        Scanner s;
        try {
            s = new Scanner(f);
            key = s.nextLine().toLowerCase();
            plaintext = s.nextLine().toLowerCase();
        } catch (FileNotFoundException ex) {
            System.out.println(f.getAbsolutePath());
        }
    }

    public String Encode() {
        String cipher = "";
        for (int i = 0; i < plaintext.length(); i++) {
            cipher += key.charAt((plaintext.charAt(i) - 'a') % 26);
        }
        System.out.println(cipher);
        return cipher;
    }

    public String Encode(String key, String plaintext) {
        String cipher = "";
        for (int i = 0; i < plaintext.length(); i++) {
            cipher += key.charAt((plaintext.charAt(i) - 'a') % 26);
        }
        System.out.println(cipher);
        return cipher;
    }

    public String Decode(String cipher) {
        String plain = "";
        for (int i = 0; i < cipher.length(); i++) {
            plain += reverse_key.charAt((cipher.charAt(i) - 'a') % 26);
        }
        System.out.println(plain);
        return plain;
    }

    public String Decode(String key, String cipher) {
        String r_key = DecodeKey(key);
        String plain = "";
        for (int i = 0; i < cipher.length(); i++) {
            plain += r_key.charAt((cipher.charAt(i) - 'a') % 26);
        }
        System.out.println(plain);
        return plain;
    }

    private String DecodeKey(String s) {
        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] sort = s.toCharArray();
        for (int i = 0; i < sort.length; i++) {
            for (int j = i + 1; j < sort.length; j++) {
                if (sort[i] > sort[j]) {
                    char temp = sort[i];
                    sort[i] = sort[j];
                    sort[j] = temp;

                    char temp2 = alphabet[i];
                    alphabet[i] = alphabet[j];
                    alphabet[j] = temp2;
                }
            }
        }
        return String.valueOf(alphabet);
    }

    public void setKey(String key) {
        this.key = key;
        reverse_key = DecodeKey(key);
    }

    public static void main(String[] args) {
        SubtitutionCipher sc = new SubtitutionCipher("src/main/java/Tuan5/input.txt");
        sc.Encode();
        sc.Decode("ghsmfzmgunxd");
    }
}
