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
public class ChineseRemainder {

    int n;

    public ChineseRemainder(int n) {
        this.n = n;
    }

    // Nghich dao a^-1 ≡ a mod n
    public int NghichDao(int a, int b) {
        int m = b;
        int x2 = 1, x1 = 0, q, r, d, x;
        if (b == 0) {
            d = a;
            x = 1;
        } else {
            while (b > 0) {
                q = a / b;
                r = a - q * b; // r = a%b;
                x = x2 - q * x1;

                a = b;
                b = r;
                x2 = x1;
                x1 = x;
            }
            d = a;
            x = x2;
        }
        // if result is negative
        x = (x % m + m) % m;

        System.out.println("d = " + d + " x = " + x);
        return x;
    }

    public void Input(int n, int[] a, int[] m) {
        Scanner s = new Scanner(System.in);
        for (int i = 0; i < n; i++) {
            System.out.println("Nhap a" + i);
            a[i] = Integer.parseInt(s.nextLine());
        }
        for (int i = 0; i < n; i++) {
            System.out.println("Nhap m" + i);
            m[i] = Integer.parseInt(s.nextLine());
        }

    }

    public int Calculate_M(int[] m) {
        int M = 1;
        for (int i = 0; i < m.length; i++) {
            M *= m[i];
        }
        return M;
    }

    // M1, M2, ... Mn
    public int[] Calculate_Mn(int[] m, int M) {
        int[] Mn = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            Mn[i] = M / m[i];
        }
        return Mn;
    }

    public int[] Calculate_Y(int[] Mn, int[] m) {
        int[] y = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            y[i] = NghichDao(Mn[i], m[i]);
        }
        return y;
    }

    public int Calculate_X(int M, int[] a, int[] Mn, int[] y) {
        int x = 0;
        for (int i = 0; i < a.length; i++) {
            x = x + (a[i] * Mn[i] * y[i]);
        }
        return x % M;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n;
        System.out.println("Nhap n:");
        n = Integer.parseInt(s.nextLine());
        ChineseRemainder cr = new ChineseRemainder(n);
        int[] a = new int[n], m = new int[n], Mn, y;
        cr.Input(n, a, m);
        // Giai
        int M = cr.Calculate_M(m);
        Mn = cr.Calculate_Mn(m, M);
        y = cr.Calculate_Y(Mn, m);
        int x = cr.Calculate_X(M, a, Mn, y);
        System.out.println("x = " + x);
        System.out.println("Dap an co dang: x = " + x + " + k." + M);
    }
}
