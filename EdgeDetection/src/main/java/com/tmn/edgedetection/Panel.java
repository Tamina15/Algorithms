package com.tmn.edgedetection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Panel extends JPanel {

    private int width, height;
    private final Point origin = new Point(0, 0);
    private final Point oldOrigin = new Point(0, 0);
    private final Point mousePt = new Point(0, 0);
    private double zoomFactor = 1, zoomMutiplier = 1.05;
    private int scale = 1;
    double delta = 0;

    public Panel(int width, int height) {
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
        this.setDoubleBuffered(true);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    setAnchorPoint(e);
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    move(e);
                }
            }
        });

        this.addMouseWheelListener((MouseWheelEvent e) -> {
            zoom(e);
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_CONTROL) {
                    zoomMutiplier = 1.15;
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
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_CONTROL) {
                    zoomMutiplier = 1.05;
                }
                if (c == KeyEvent.VK_Q || c == KeyEvent.VK_A || c == KeyEvent.VK_W || c == KeyEvent.VK_S) {
                    doUpdate = true;
                }
            }
        });

        option = new Option(1, 0.3);
        getImage();
        initImages();
    }

    private void setAnchorPoint(MouseEvent e) {
        oldOrigin.setLocation(origin);
        mousePt.setLocation(e.getPoint());
    }

    private void move(MouseEvent e) {
        double dx = ((e.getX() - mousePt.x) / zoomFactor);
        double dy = ((e.getY() - mousePt.y) / zoomFactor);
        origin.setLocation(oldOrigin.getX() + dx, oldOrigin.getY() + dy);
    }

    private void zoom(MouseWheelEvent e) {
        // Zoom in
        if (e.getWheelRotation() < 0) {
            zoomFactor *= zoomMutiplier;
        }
        // Zoom out
        if (e.getWheelRotation() > 0) {
            zoomFactor /= zoomMutiplier;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomFactor, zoomFactor);
        g2d.translate(origin.x, origin.y);

        draw(g2d);
        g2d.dispose();
    }

    private int gap = 15;

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
    BufferedImage image, grayScaleImage, sobelX, sobelY, sobel, angles, nonMaxImage, doubleThreshold, hysteresis;
    double pTime;

    private void getImage() {
        try {
            File file = new File("sample-1.jpg");
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
        filter();
        grayScaleImage = canny.getGrayScaleImage();
        sobelX = canny.getSobelXImage();
        sobelY = canny.getSobelYImage();
        sobel = canny.getSobelImage();
        angles = canny.getAngleImage();
        nonMaxImage = canny.getNonMaximumSuppressionImage();
    }

    private void filter() {
        pTime = canny.filter();
        doubleThreshold = canny.getDoubleThesholdingImage();
        hysteresis = canny.getHysteresisImage();
    }

    public void setWidth(int width) {
        this.width = width;
        this.setPreferredSize(new Dimension(width, height));
    }

    public void setHeight(int height) {
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
    }

}
