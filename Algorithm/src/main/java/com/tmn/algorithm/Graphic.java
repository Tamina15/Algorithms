/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tmn.algorithm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author HP
 * @author Tamina
 */
public class Graphic extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
    }

    public static void main(String[] args) {
        Frame frame = new Frame();
    }

    static class Panel extends JPanel {

        int panelWidth = 800;
        int panelHeight = 800;

        public Panel() {
            this.setPreferredSize(new Dimension(panelWidth, panelHeight));
            this.setDoubleBuffered(true);
            this.setBackground(Color.black);
            this.setFocusable(true);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.dispose();
            g.dispose();
        }


        void update() {
        }
    }

    static class Frame extends JFrame implements Runnable {

        Thread thread;
        public final int FPS = 60;
        public static String FPScount = "NaN";
        public static double delta = 0;
        public static boolean updating;

        Graphic.Panel panel;

        Frame() {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            panel = new Graphic.Panel();
            this.add(panel);
            this.pack();
            this.setResizable(false);
            this.setLocationRelativeTo(null);
            this.setBackground(Color.black);
            this.setVisible(true);
            newThread();
        }

        private void newThread() {
            thread = new Thread(this);
            thread.start();
        }

        @Override
        public void run() {
            double drawInterval = 1000000000 / FPS;
            long lastTime = System.nanoTime();
            long timer = 0;
            int count = 0;
            while (thread != null) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                timer += (currentTime - lastTime);
                lastTime = currentTime;
                if (delta >= 1) {
                    panel.repaint();
                    delta--;
                    count++;
                }
                if (timer >= 1000000000) {
                    FPScount = "" + count;
                    panel.update();
                    count = 0;
                    timer = 0;
                    updating = false;
                }
            }
        }
    }
}
