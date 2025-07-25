/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Tran Minh Nhat
 */
public class ClimateSpiral extends JPanel {

    int panelWidth = 800;
    int panelHeight = 800;
    Random random = new Random();
    ArrayList<ArrayList<String>> table;
    List<String> months;
    ArrayList<Integer> years;
    float[][] data;
    int rowCount;

    public ClimateSpiral() {
        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setDoubleBuffered(true);
        this.setBackground(Color.black);
        this.setFocusable(true);
        table = readCSV("public/GlobalTempMean.csv");
        months = table.get(0).subList(1, 13);
        years = new ArrayList();
        rowCount = table.size() - 1;
        data = new float[rowCount][12];
        for (int i = 1; i < table.size(); i++) {
            for (int j = 1; j < 13; j++) {
                try {
                    data[i - 1][j - 1] = Float.valueOf(table.get(i).get(j));
                } catch (NumberFormatException e) {
                    data[i - 1][j - 1] = 0;
                }
            }
            years.add(Integer.parseInt(table.get(i).get(0)));
        }

    }

    private ArrayList<ArrayList<String>> readCSV(String name) {
        ArrayList<ArrayList<String>> temp_table = new ArrayList();
        try {
            File f = new File(name);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                Scanner sc2 = new Scanner(sc.nextLine());
                sc2.useDelimiter(",");
                ArrayList<String> d = new ArrayList();
                while (sc2.hasNext()) {
                    d.add(sc2.next());
                }
                temp_table.add(d);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClimateSpiral.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp_table;
    }
    int fontWidth = 20;
    int monthCircle = 600;
    int bigCircle = 500;
    int smallCircle = 300;
    int prei = 0;
    int prej = 0;
    int currenti = 0;
    int currentj = 0;
    double lerp1 = 1;
    double lerp2 = 1.5;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
//        g2d.scale(0.1, 0.1);
        g2d.setColor(Color.white);
        g2d.translate(panelWidth / 2, panelHeight / 2);
        g2d.drawOval(-monthCircle / 2, -monthCircle / 2, monthCircle, monthCircle);
        g2d.drawOval(-bigCircle / 2, -bigCircle / 2, bigCircle, bigCircle);
        g2d.drawOval(-smallCircle / 2, -smallCircle / 2, smallCircle, smallCircle);

        g2d.setFont(new Font("Times New Roman", Font.BOLD, fontWidth));
        FontMetrics metrics = getFontMetrics(g.getFont());
        for (int i = 0; i < months.size(); i++) {
            int stringWidth = metrics.stringWidth(months.get(i));
            double x = (monthCircle / 2 + stringWidth) * Math.cos((i * Math.PI / 6) - Math.PI / 3);
            double y = (monthCircle / 2 + stringWidth) * Math.sin((i * Math.PI / 6) - Math.PI / 3);
            g2d.drawString(months.get(i), (int) x - stringWidth / 2, (int) y + fontWidth / 4);
        }
        int year = years.get(prei % rowCount);
        String month = months.get(currentj);
        String my = month + "/" + year;
        g2d.drawString(my, 0 - metrics.stringWidth(my) / 2, fontWidth / 4);

        drawSpiral(g2d);

//        double x1 = (smallCircle + 20) / 2 * Math.cos((prej * Math.PI / 6) - Math.PI / 3);
//        double y1 = (smallCircle + 20) / 2 * Math.sin((prej * Math.PI / 6) - Math.PI / 3);
//        x1 *= lerp(1, 2, data[prei][prej]);
//        y1 *= lerp(1, 2, data[prei][prej]);
//        double x2 = (smallCircle + 20) / 2 * Math.cos((currentj * Math.PI / 6) - Math.PI / 3);
//        double y2 = (smallCircle + 20) / 2 * Math.sin((currentj * Math.PI / 6) - Math.PI / 3);
//        x2 *= lerp(1, 2, data[currenti][currentj]);
//        y2 *= lerp(1, 2, data[currenti][currentj]);
//        g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        prei = currenti;
        prej = currentj;
        currentj++;
        if (currentj > 11) {
            currentj = 0;
            currenti++;
            if (currenti >= data.length) {
                currenti = 0;
            }
        }
        g2d.dispose();
        g.dispose();
    }

    private void drawSpiral(Graphics2D g2d) {
        double x1 = (smallCircle + 20) / 2 * Math.cos((0 * Math.PI / 6) - Math.PI / 3);
        double y1 = (smallCircle + 20) / 2 * Math.sin((0 * Math.PI / 6) - Math.PI / 3);
        x1 *= lerp(lerp1, lerp2, data[0][0]);
        y1 *= lerp(lerp1, lerp2, data[0][0]);
        for (int i = 0; i < currenti * 12 + currentj; i++) {
            int row = i / 12;
            int col = i % 12;
            double x2 = (smallCircle + 20) / 2 * Math.cos((col * Math.PI / 6) - Math.PI / 3);
            double y2 = (smallCircle + 20) / 2 * Math.sin((col * Math.PI / 6) - Math.PI / 3);
            x2 *= lerp(lerp1, lerp2, data[row][col]);
            y2 *= lerp(lerp1, lerp2, data[row][col]);
            g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            x1 = x2;
            y1 = y2;
        }
    }

    public static double lerp(double n0, double n1, double a) {
        return (1.0 - a) * n0 + (a * n1);
    }

    public static void main(String[] args) {
        Frame frame = new Frame();
    }
}

class Frame extends JFrame implements Runnable {

    Thread thread;
    public final int FPS = 60;
    public static String FPScount = "NaN";
    public static double delta = 0;

    ClimateSpiral panel;

    Frame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new ClimateSpiral();
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
                count = 0;
                timer = 0;
            }
        }
    }
}
