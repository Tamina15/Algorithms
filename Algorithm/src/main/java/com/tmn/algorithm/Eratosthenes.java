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
public class Eratosthenes {

    private boolean SoNguyenTo(int n) {
        for (int i = 2; i * i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return n > 1;
    }

    public boolean[] Eratos(int n) {
        boolean[] snt = new boolean[n];
        for (int i = 0; i < n; i++) {
            snt[i] = true;
        }
        snt[0] = snt[1] = false;
        for (int i = 2; i * i < n; i++) {
            if (snt[i]) {
                for (int j = i * i; j < n; j += i) {
                    snt[j] = false;
                }
            }
        }
        return snt;
    }

    static Scanner s = new Scanner(System.in);

    public static void main(String[] agrs) {

        Eratosthenes e = new Eratosthenes();
        int n;
        System.out.println("Nhap vao so nguyen duong:");
        n = Integer.parseInt(s.nextLine());
        long ts = System.nanoTime();
        boolean[] snt = e.Eratos(n);
        long te = System.nanoTime();
        System.out.println((te-ts)/1000000000f);
//        for (int i = 0; i < n; i++) {
//            if (snt[i]) {
//                System.out.println(i + " là so nguyen to");
//            }
//        }
    }

}
