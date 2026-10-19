package com.tmn.cellularautomata;

import com.tmn.graphic.NMain;
import com.tmn.graphic.NPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JTextField;

public class Main {

    static class Panel extends NPanel {

        int fps, ups;

        public Panel(int width, int height) {
            super(width, height);

            init();

            JTextField textField = new JTextField();
            textField.setPreferredSize(new Dimension(100, 30));
            textField.setBackground(Color.white);
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        int r = Integer.parseInt(textField.getText());
                        ca.restartToRule(r);
                        wca.restartToRule(r);
                        rca.restartToRule(r);
                        rwca.restartToRule(r);
                    } catch (NumberFormatException err) {
                    }
                }
            });
            this.add(textField);
        }

        private void init() {
            int length = Long.SIZE * 3;
            int[] initital = new int[length / 3];
            Random r = new Random();
            for (int i = 0; i < initital.length; i++) {
                initital[i] = r.nextInt(length);
            }
//        ca = new CellularAutomata(100, width, height);
//        ca = new CellularAutomata(length, width, height);
            long seed = r.nextLong();
            ca = new CellularAutomata(length, seed);
            wca = new WrappedCellularAutomata(length, seed);
            rca = new CellularAutomata(length, 0L);
            rwca = new WrappedCellularAutomata(length, 0L);
        }

        @Override
        public void draw(Graphics2D g2d) {
            // draw here
            ca.draw(g2d);
            wca.draw(g2d, ca.getWidth() + 15, 0);
            rca.draw(g2d, 0, ca.getHeight() + 15);
            rwca.draw(g2d, ca.getWidth() + 15, ca.getHeight() + 15);
            ///////////////
            g2d.setColor(Color.white);
            g2d.drawString(Arrays.toString(ca.getRuleArray()), 0, -10);
        }

        @Override
        public void drawOriginal(Graphics2D g2d) {
            g2d.setColor(Color.red);
            g2d.drawString("FPS: " + fps, 0, 10);
            g2d.drawString("UPS: " + ups, 0, 20);
        }

        CellularAutomata ca, rca;
        WrappedCellularAutomata wca, rwca;

        int waitBefore = 0, waitAfter = -1, delay;

        public void update() {
            if (waitBefore > 0) {
                waitBefore--;
                return;
            }

            if (waitAfter > 0) {
                waitAfter--;
                return;
            }

            if (delay > 0) {
                delay--;
                return;
            } else {
                delay = 0;
            }

            if (!ca.done) {
                ca.update();
            }

            if (!wca.done) {
                wca.update();
            }

            if (!rca.done) {
                rca.update();
            }

            if (!rwca.done) {
                rwca.update();
            }
        }

    }

    public static void main(String[] args) {
        NMain main = new NMain(new Panel(800, 600));
        main.run();
    }
}
