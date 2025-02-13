package com.tmn.cellularautomata;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

public class CellularAutomata {

    /**
     * Values to scale the {@code image}
     */
    protected int width, height;

    /**
     * The image represents the 2D grid.
     * <p/>
     * Each pixel represents 1 cell.
     * Scale the image by using {@code width} and {@code height}
     * <p/>
     * Use to replace {@code Graphics.drawLine} for performance
     */
    BufferedImage image;

    int currentCol = 1;

    int[] initialCells, cells, nexts;
    static int white = Color.WHITE.getRGB();

    /**
     * Represents one pixel as [red, green, blue]
     */
    int[] pixel;

    Option option;

    public CellularAutomata() {
        this(0, null, null);
    }

    public CellularAutomata(int cellsLength) {
        this(cellsLength, null, null);
    }

    public CellularAutomata(int cellsLength, Integer width, Integer height) {
        this(cellsLength, width, height, null);
    }

    public CellularAutomata(int cellsLength, int[] intitial) {
        this(cellsLength, null, null, intitial);
    }

    public CellularAutomata(int cellsLength, Integer width, Integer height, int[] intitial) {
        this.width = (width != null) ? width : cellsLength;
        this.height = (height != null) ? height : cellsLength;

        image = new BufferedImage(cellsLength, cellsLength, BufferedImage.TYPE_INT_RGB);
        pixel = new int[3];

        initialCells = new int[cellsLength];
        if (intitial != null) {
            for (int i = 0; i < intitial.length; i++) {
                set(initialCells, intitial[i], 1);
            }
        } else {
            set(initialCells, initialCells.length / 2, 1);
        }

        cells = Arrays.copyOf(initialCells, initialCells.length);
        nexts = new int[cellsLength];
    }

    public void draw(Graphics2D g2d, int offsetX, int offsetY) {
        AffineTransform at = g2d.getTransform();

        g2d.translate(offsetX, offsetY);
        g2d.drawImage(image, 0, 0, width, height, null);

        g2d.setTransform(at);
    }

    public void draw(Graphics2D g2d) {
        draw(g2d, 0, 0);
    }

    byte neightbors = 3;
    int pattern = 150, numberOfStates = 1 << neightbors;
    int[] patternArray = Utils.toBinaryArray(pattern, numberOfStates);
    int[] reversedPatternArray = Utils.newReversedArray(patternArray);

    private int cI = 0;

    public void update(int time) {
        for (int i = 0; i < time; i++) {
            if (currentCol >= image.getHeight()) {
                return;
            }
            if (cI < cells.length) {
                neighbor_N(cI);
                cI++;
            } else {
                cI = 0;
                currentCol++;
                int[] temp = cells;
                cells = nexts;
                nexts = temp;
            }
        }
    }

    public void update() {
        if (currentCol >= image.getHeight()) {
            return;
        }
        clearLine(currentCol);
        for (int i = 0; i < cells.length; i++) {
            neighbor_N(i);
        }
        currentCol++;
        int[] temp = cells;
        cells = nexts;
        nexts = temp;
    }

    protected void neighbor_N(int i) {
        int value = 0;
        for (int j = neightbors - 1; j >= 0; j--) {
            int index = i - j + neightbors / 2;
            int c = index < 0 || index >= cells.length ? 0 : cells[index];
            value += c << j;
        }
        value = numberOfStates - 1 - value;
        nexts[i] = patternArray[value];
        if (nexts[i] == 1) {
            setPixel(i, currentCol, white);
        }
    }

    protected void neighbor_N2(int i) {
        int value = 0;
        for (int j = neightbors - 1; j >= 0; j--) {
            int index = i - j + neightbors / 2;
            int c = index < 0 || index >= cells.length ? 0 : cells[index];
            value += c << j;
        }
        nexts[i] = reversedPatternArray[value];
        if (nexts[i] == 1) {
            setPixel(i, currentCol, white);
        }
    }

    private void set(int[] array, int x, int value) {
        set(array, x, 0, value, white);
    }

    private void set(int[] array, int x, int y, int value, int color) {
        array[x] = value;
        setPixel(x, y, color);
    }

    protected void setPixel(int x, int y, int color) {
        int pos = 0;
        pixel[pos++] = (color >> 16 & 0xff);
        pixel[pos++] = (color >> 8 & 0xff);
        pixel[pos++] = (color & 0xff);
        image.getRaster().setPixel(x, y, pixel);
    }

    protected void restartToNextPattern() {
        restart(pattern + 1);
    }

    public void restart(int p) {
        pattern = p;
        patternArray = Utils.toBinaryArray(pattern, numberOfStates);
        Utils.newReversedArray(reversedPatternArray, patternArray);
        restart();
    }

    protected void restartRandom() {
        restart();
        Random r = new Random();
        for (int i = 0; i < cells.length; i++) {
            cells[i] = r.nextInt(2);
        }
    }

    protected void restart() {
        clearImage();
        for (int i = 0; i < cells.length; i++) {
            cells[i] = 0;
            nexts[i] = 0;
        }
        cI = 0;
        currentCol = 1;
        cells = Arrays.copyOf(initialCells, initialCells.length);
    }

    protected void clearImage() {
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 1, image.getWidth(), image.getHeight());
        g2d.dispose();
    }

    protected void clearLine(int col) {
        col = col % image.getHeight();
        int black = Color.black.getRGB();
        for (int i = 0; i < cells.length; i++) {
            setPixel(i, col, black);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
