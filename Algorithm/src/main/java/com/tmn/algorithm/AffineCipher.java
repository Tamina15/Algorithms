/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

import java.util.Scanner;

/**
 *
 * @author Tran Minh Nhat
 */
public class AffineCipher {

    private int a, b;

    public AffineCipher() {
        a = 1;
        b = 0;
    }

    public AffineCipher(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public static int NghichDao(int a, int n) {
        int m = n;
        int x2 = 1, x1 = 0, q, r, d, x;
        if (n == 0) {
            d = a;
            x = 1;
        } else {
            while (n > 0) {
                q = a / n;
                r = a - q * n; // r = a%b;
                x = x2 - q * x1;

                a = n;
                n = r;
                x2 = x1;
                x1 = x;
            }
            d = a;
            x = x2;
        }
        if (d == 1) {
            // if result is negative
            x = (x % m + m) % m;
//            System.out.println("d = " + d + " x = " + x);
            return x;
        }
//        System.out.println("Khong co ngich dao a mod b");
        return -1;
    }

    public String Encode(String plaintext) {
        return Encode(a, b, plaintext);
    }

    public static String Encode(int a, int b, String plaintext) {
        String result = "";
        if (NghichDao(a, 26) != -1) {
            for (int i = 0; i < plaintext.length(); i++) {
                char x = plaintext.charAt(i);
                if (Character.isLowerCase(x)) {
                    /*
                    ([char] - 'a') : character index in the English alphabet, for lowercase characcter
                    ([char] + 'a') : character index in the ASCII table, for lowercase characcter
                     */
                    // C    =           a * (x        + b) mod 26
                    result += (char) (((a * (x - 'a') + b) % 26) + 'a');
                } else if (Character.isUpperCase(x)) {
                    result += (char) (((a * (x - 'A') + b) % 26) + 'A');
                } else {
                    result += x;
                }
            }
        } else {
            result = a + " and 26 is not coprime";
        }
        return result;
    }

    public String Decode(String cipher) {
        return Decode(a, b, cipher);
    }

    public static String Decode(int a, int b, String cipher) {
        int r = NghichDao(a, 26);
        if (r == -1) {
            return a + " and 26 is not coprime";
        }
        String result = "";
        for (int i = 0; i < cipher.length(); i++) {
            char y = cipher.charAt(i);
            if (Character.isLowerCase(y)) {
                /*
                ([char] - 'a') : character index in the English alphabet, for lowercase characcter
                ([char] + 'a') : character index in the ASCII table, for lowercase characcter
                 */
                // P    =            r *  (y        - b)  mod 26
                //                                              + 26) % 26 if result is negative
                result += (char) ((((r * ((y - 'a') - b)) % 26) + 26) % 26 + 'a');
            } else if (Character.isUpperCase(y)) {
                result += (char) ((((r * ((y - 'A') - b)) % 26) + 26) % 26 + 'A');
            } else {
                result += y;
            }
        }
        return result;
    }

    public void SetA(int a) {
        this.a = a;
    }

    public void SetB(int b) {
        this.b = b;
    }
    public static void main(String[] args) {
        int a, b;
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Input a:");
        a = Integer.parseInt(scanner.nextLine());
        
        System.out.println("Input b:");
        b = Integer.parseInt(scanner.nextLine());
        
        System.out.println("Input plaintext:");
        String text = scanner.nextLine();
        
        AffineCipher af = new AffineCipher(a, b);
        
        String cipher = af.Encode(text);
        System.out.println("Encoded: " + cipher);
        
        String plaintext = af.Decode(cipher);
        System.out.println("Decoded: " + plaintext);

    }
}
