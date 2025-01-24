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
public class Euclidean {

    public int gcd(int a, int b) {
        int r;
        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public void Euclid_mo_rong(int a, int b) {
        int x2 = 1, y2 = 0, x1 = 0, y1 = 1, q, r, d, x, y;
        if (b == 0) {
            d = a;
            x = 1;
            y = 0;
        } else {
            while (b > 0) {
                q = a / b;
                r = a - q * b; // r = a%b;
                x = x2 - q * x1;
                y = y2 - q * y1;

                a = b;
                b = r;
                x2 = x1;
                x1 = x;
                y2 = y1;
                y1 = y;
            }
            d = a;
            x = x2;
            y = y2;
        }
        System.out.println("d = " + d + " x = " + x + " y = " + y);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhap so a:");
        int a = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhap so b:");
        int b = Integer.parseInt(scanner.nextLine());
        Euclidean e = new Euclidean();
        e.Euclid_mo_rong(a, b);

    }

}
