package com.tmn.edgedetection;

import com.tmn.graphic.NMain;
import com.tmn.graphic.NPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Main {

    static class Panel extends NPanel {

        public Panel(int width, int height) {
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int c = e.getKeyCode();
                    if (c == KeyEvent.VK_CONTROL) {
                        setZoomFactor(1.15);
                    }
                    if (c == KeyEvent.VK_Q) {
                        option.setNumDev(option.getNumDev() + 0.01);
                    }
                    if (c == KeyEvent.VK_A) {
                        option.setNumDev(option.getNumDev() - 0.01);
                    }
                    if (c == KeyEvent.VK_W) {
                        option.setLowFraction(option.getLowFraction() + 0.01);
                    }
                    if (c == KeyEvent.VK_S) {
                        option.setLowFraction(option.getLowFraction() - 0.01);
                    }
                    doUpdate = true;
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    int c = e.getKeyCode();
                    if (c == KeyEvent.VK_CONTROL) {
                        setZoomFactor(1.05);
                    }
                    if (c == KeyEvent.VK_Q || c == KeyEvent.VK_A || c == KeyEvent.VK_W || c == KeyEvent.VK_S) {
//                    doUpdate = true;
                    }
                }
            });

            option = new Option(1, 0.3);
            getImage();
            initImages();
        }

        private final int gap = 15;

        private int drawImage(Graphics2D g2d, Image image, int x, int y, String label) {
            g2d.drawString(label, x, y - 15);
            g2d.drawImage(image, x, y, null);
            return x + image.getWidth(null) + gap;
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(Color.white);
            g2d.setFont(new Font(null, 1, 40));
            int x = 0, y = image.getHeight();
            x = drawImage(g2d, image, x, 0, "Original");
            x = drawImage(g2d, grayScaleImage, x, 0, "Gray scale");
            x = drawImage(g2d, sobelX, x, 0, "Sobel X");
            x = drawImage(g2d, sobelY, x, 0, "Sobel Y");
            x = drawImage(g2d, sobel, x, 0, "Sobel Filter Gradient");
            x = drawImage(g2d, angles, x, 0, "Gradient Angles");
            x = drawImage(g2d, nonMaxImage, x, 0, "Non-max Suppression");
            g2d.drawString(pTime + "", x, y + 50);
            g2d.drawString(option.getNumDev() + "", x, y + 100);
            g2d.drawString(option.getLowFraction() + "", x, y + 150);
            x = drawImage(g2d, doubleThreshold, x, 0, "Double Threshold");
            x = drawImage(g2d, hysteresis, x, 0, "Hysteresis");
            x = drawImage(g2d, image, x, 0, "Original");

            x = 0;
            y = y + 250;
            x = drawImage(g2d, image, x, y, "Original");
            x = drawImage(g2d, grayScaleImage1, x, y, "Gray scale");
            x = drawImage(g2d, sobelX1, x, y, "Sobel X");
            x = drawImage(g2d, sobelY1, x, y, "Sobel Y");
            x = drawImage(g2d, sobel1, x, y, "Sobel Filter Gradient");
            x = drawImage(g2d, angles1, x, y, "Gradient Angles");
            x = drawImage(g2d, nonMaxImage1, x, y, "Non-max Suppression");
            x = drawImage(g2d, doubleThreshold1, x, y, "Double Threshold");
            x = drawImage(g2d, hysteresis1, x, y, "Hysteresis");
            x = drawImage(g2d, image, x, y, "Original");
        }

        boolean doUpdate = false;

        public void update() {
            if (doUpdate) {
                filter();
                doUpdate = false;
            }
        }

        Option option;
        CannyEdgeDetection canny;
        CannyEdgeDetection canny1;
        BufferedImage image;
        BufferedImage grayScaleImage, sobelX, sobelY, sobel, angles, nonMaxImage, doubleThreshold, hysteresis;
        BufferedImage grayScaleImage1, sobelX1, sobelY1, sobel1, angles1, nonMaxImage1, doubleThreshold1, hysteresis1;
        double pTime;

        private void getImage() {
            try {
                URL url = URI.create("https://picsum.photos/1500").toURL();
                File file = new File("sample-4.jpg");
                image = ImageIO.read(file);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void initImages() {
            if (image == null) {
                return;
            }

            canny = new CannyEdgeDetection(image, option);
            canny1 = new CannyEdgeDetection(image, option);

            filter();

            grayScaleImage = canny.getGrayScaleImage();
            sobelX = canny.getSobelXImage();
            sobelY = canny.getSobelYImage();
            sobel = canny.getSobelImage();
            angles = canny.getAngleImage();
            nonMaxImage = canny.getNonMaximumSuppressionImage();

            grayScaleImage1 = canny1.getGrayScaleImage();
            sobelX1 = canny1.getSobelXImage();
            sobelY1 = canny1.getSobelYImage();
            sobel1 = canny1.getSobelImage();
            angles1 = canny1.getAngleImage();
            nonMaxImage1 = canny1.getNonMaximumSuppressionImage();
        }

        private void filter() {
            pTime = canny.filter();
            doubleThreshold = canny.getDoubleThesholdingImage();
            hysteresis = canny.getHysteresisImage();

            canny1.filter();
            doubleThreshold1 = canny1.getDoubleThesholdingImage();
            hysteresis1 = canny1.getHysteresisImage();

        }
    }

    public static void main(String[] args) throws InterruptedException {
        NMain main = new NMain(new Panel(800, 500));
        main.run();
    }
}
