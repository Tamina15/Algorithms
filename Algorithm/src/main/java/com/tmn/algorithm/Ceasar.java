/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

import java.util.Scanner;

/**
 *
 * @author HP
 */
public class Ceasar {

    public char[] nguyenam = {'a', 'u', 'i', 'o', 'e'};

    public boolean check(char[] c) {
        for (int i = 0; i < c.length; i++) {
            for (int j = i + 1; j <= c.length; j++) {
                if (c[j] == ' ') {

                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        char[] character;
        System.out.println("Nhap chuoi:");
        Scanner scanner = new Scanner(System.in);
        character = scanner.nextLine().toLowerCase().toCharArray();
        char[] text = new char[character.length];
        for (int i = 1; i <= 26; i++) {
            for (int j = 0; j < character.length; j++) {
                if (character[j] == ' ') {
                    text[j] = character[j];
                } else {
                    text[j] = (char) ((character[j] + i) % 26 + 97);
                }
            }
            System.out.println("\n" + String.valueOf(text));
        }
    }
}
