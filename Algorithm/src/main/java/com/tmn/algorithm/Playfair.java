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
public class Playfair {

    // English Alphabet
    char[] character_set = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
        'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    // Array to store used character, false if not used, true if used
    boolean[] used_character = new boolean[26];

    // key matrix
    char[][] key_matrix = new char[5][5];

    String key = "";

    public Playfair(String key) {
        this.key = key.replaceAll("\\W", "");
        Create_Key_Matrix();
    }

    private void Create_Key_Matrix() {
        char[] key_array = key.toUpperCase().toCharArray();
        // Counter for how many character have been used, to fill the rest of the matrix
        int j = 0;
        // For every character in the key
        for (int i = 0; i < key_array.length; i++) {
            // If that character have NOT been used
            //(key_array[i] - 'A') % 26, index of character in english alphabet
            if (!used_character[(key_array[i] - 'A') % 26]) {
                // Set the value of the key matrix at row = j/5, column = j%5 to that character
                key_matrix[j / 5][j % 5] = key_array[i];
                // mark that character have been used
                used_character[(key_array[i] - 'A') % 26] = true;
                // Increase counter
                j++;
                // Check for I and J
                if (key_array[i] == 'I') {
                    // Remove character J
                    used_character[9] = true;
                } else if (key_array[i] == 'J') {
                    // Remove character I
                    used_character[8] = true;
                }
            }
        }
        // Counter for the first character that is not used
        int k = 0;
        // For the rest of the matrix, start from j
        for (int i = j; i < 25; i++) {
            // Skip used chracter
            while (used_character[k]) {
                k++;
            }
            // Set first unused character to the next index in key matrix
            key_matrix[i / 5][i % 5] = character_set[k];
            // Mark that character have been used
            used_character[k] = true;
            // Check for I and J
            if (character_set[k] == 'I') {
                // Remove character J
                used_character[9] = true;
            } else if (character_set[k] == 'J') {
                // Remove character I
                used_character[8] = true;
            }
        }
    }

    private String Pair(String text) {
        text = text.replaceAll("\\W", "").replace('j', 'i');
        // for every pair in text
        for (int i = 0; i < text.length(); i = i + 2) {
            // last character
            if (i + 1 >= text.length()) {
                if (text.charAt(i) != 'x') {
                    text = text.concat("x");
                } else {
                    text = text.concat("z");
                }
                break;
            }
            // if two charaters are the same
            if (text.charAt(i) == text.charAt(i + 1)) {
                // Get string from start to between, i.e: bal in "balloon"
                String a = text.substring(0, i + 1);
                // Get string from between to end, i.e: loon in "balloon"
                String b = text.substring(i + 1);
                // Add buffer character, i.e: bal + x + loon, balxloon
                if (text.charAt(i) != 'x') {
                    text = a.concat("x".concat(b));
                } else {
                    text = a.concat("z".concat(b));
                }
            }
        }
        return text;
    }

    public String Encode(String text) {
        char[] plaintext = Pair(text).toUpperCase().toCharArray();
        String cipher = "";
        // Coordinate
        int xa = 0, xb = 0, ya = 0, yb = 0;
        // For every pair
        for (int i = 0; i < plaintext.length; i = i + 2) {
            // Search for cordinates in key matrix
            for (int j = 0; j < 25; j++) {
                // First Character
                if (key_matrix[j / 5][j % 5] == plaintext[i]) {
                    xa = j / 5;
                    ya = j % 5;
                }
                // Second Character
                if (key_matrix[j / 5][j % 5] == plaintext[i + 1]) {
                    xb = j / 5;
                    yb = j % 5;
                }
            }
            // Encode one Pair
            cipher = cipher + GetCipherText(xa, ya, xb, yb);
        }
        return cipher;
    }

    // Encode one pair of characters
    private String GetCipherText(int xa, int ya, int xb, int yb) {
        // Nếu cả hai chữ trong cặp đều rơi vào cùng một hàng, thì
        // mã mỗi chữ bằng chữ ở phía bên phải nó trong cùng hàng
        // của ma trận khóa (cuộn vòng quanh từ cuối về đầu),
        if (xa == xb) {
            return "" + key_matrix[xa][(ya + 1) % 5] + key_matrix[xb][(yb + 1) % 5];
        }
        // Nếu cả hai chữ trong cặp đều rơi vào cùng một cột, thì mã
        // mỗi chữ bằng chữ ở phía bên dưới nó trong cùng cột của
        // ma trận khóa (cuộn vòng quanh từ cuối về đầu)
        if (ya == yb) {
            return "" + key_matrix[(xa + 1) % 5][ya] + key_matrix[(xb + 1) % 5][yb];
        }
        // Trong các trường hợp khác, mỗi chữ trong cặp được mã
        // bởi chữ cùng hàng với nó và cùng cột với chữ cùng cặp với
        // nó trong ma trận khóa
        return "" + key_matrix[xa][yb] + key_matrix[xb][ya];
    }

    public String Decode(String ciphertext) {
        char[] cipher = ciphertext.replaceAll("\\W", "").toUpperCase().replace('J', 'I').toCharArray();
        String plaintext = "";
        int xa = 0, xb = 0, ya = 0, yb = 0;
        for (int i = 0; i < cipher.length; i = i + 2) {
            for (int j = 0; j < 25; j++) {
                if (key_matrix[j / 5][j % 5] == cipher[i]) {
                    xa = j / 5;
                    ya = j % 5;
                }
                if (key_matrix[j / 5][j % 5] == cipher[i + 1]) {
                    xb = j / 5;
                    yb = j % 5;
                }
            }
            plaintext = plaintext + GetPlainText(xa, ya, xb, yb);
        }
        return plaintext;
    }

    // Decode one Pair of characters, Do reverse of Encode
    private String GetPlainText(int xa, int ya, int xb, int yb) {
        if (xa == xb) {                //=(ya - 1) mod 5, because -1 % 5 = -1
            return "" + key_matrix[xa][((ya - 1) + 5) % 5] + key_matrix[xb][((yb - 1) + 5) % 5];
        }
        if (ya == yb) {
            return "" + key_matrix[((xa - 1) + 5) % 5][ya] + key_matrix[((xb - 1) + 5) % 5][yb];
        }
        return "" + key_matrix[xa][yb] + key_matrix[xb][ya];
    }

    public void PrintCharset() {
        for (int i = 0; i < 26; i++) {
            System.out.print("'" + character_set[i] + "', ");
        }
        System.out.println("");
    }

    public void Print_Key_Matrix() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(key_matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static void main(String[] args) {
//        Playfair playfair = new Playfair("tinhoc");
//        playfair.Print_Key_Matrix();
//        String cipher = playfair.Encode("dajhocsaigon");
//        System.out.println(cipher);
//        System.out.println(playfair.Decode(cipher));
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input key:");
        String key = scanner.nextLine();
        System.out.println("Input plaintext:");
        String text = scanner.nextLine();
        Playfair playfair = new Playfair(key);
        String cipher = playfair.Encode(text);
        System.out.println(cipher);
        System.out.println(playfair.Decode(cipher));

    }

}
