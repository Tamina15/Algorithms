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
public class FastExponent {

    public static long mod_exp(long a, long x, long n) {
        long r = 1;
        while (x > 0) {
            if (x % 2 == 1) {
                r = (r * a) % n;
            }
            a = (a * a) % n;
            x /= 2;
        }
        return r;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Nhap a :");
            long a = Long.parseLong(scanner.nextLine());
            System.out.println("Nhap x :");
            long x = Long.parseLong(scanner.nextLine());
            System.out.println("Nhap n :");
            long n = Long.parseLong(scanner.nextLine());
            long exp = mod_exp(a, x, n);
            System.out.println("\n(" + a + " ^ " + x + ") mod " + n + " = " + exp);
        } catch (NumberFormatException e) {
            System.out.println("Vui long nhap so");
        }
    }
}
