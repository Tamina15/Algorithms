/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

/**
 *
 * @author Tran Minh Nhat
 */
public class NghichDaoModN {

    // Nghich dao a^-1 ≡ a mod n
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
            System.out.println("d = " + d + " x = " + x);
            return x;
        }
        System.out.println("Khong co ngich dao a mod b");
        return -1;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 26; i++) {
            System.out.print(i + " ");
            NghichDao(i, 26);
        }
    }
}
