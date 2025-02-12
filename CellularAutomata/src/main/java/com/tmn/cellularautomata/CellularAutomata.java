package com.tmn.cellularautomata;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class CellularAutomata {

    protected int width, height;
    BufferedImage image;
    int currentCol;
    int[] initialCells, cells, nexts;
    static int white = Color.WHITE.getRGB();

    /**
     * Represents one pixel.
     * [ red, green, blue ]
     */
    int[] pixel;

    public CellularAutomata() {
        this(0, null, null);
    }

    public CellularAutomata(int arrayLength) {
        this(arrayLength, null, null);
    }

    public CellularAutomata(int arrayLength, int[] intitial) {
        this(arrayLength, null, null, intitial);
    }

    public CellularAutomata(int arrayLength, Integer width, Integer height, int[] intitial) {
        this.width = width == null ? arrayLength : width;
        this.height = height == null ? arrayLength : height;

        initialCells = new int[arrayLength];
        nexts = new int[arrayLength];

        image = new BufferedImage(arrayLength, arrayLength, BufferedImage.TYPE_INT_RGB);
        pixel = new int[3];

        currentCol = 1;

        if (intitial != null) {
            for (int i = 0; i < intitial.length; i++) {
                set(initialCells, intitial[i], 1);
            }
        } else {
            set(initialCells, initialCells.length / 2, 1);
        }

        cells = Arrays.copyOf(initialCells, initialCells.length);
    }

    private void set(int[] array, int x, int value) {
        set(array, x, 0, value, white);
    }

    private void set(int[] array, int x, int y, int value, int color) {
        array[x] = value;
        setPixel(x, y, color);
    }

    public static String toBinary(int number, byte exponent) {
        if (exponent < 0 || exponent > 31) {
            return "0";
        }
        return toBinary(number, 1 << exponent);
    }

    public static String toBinary(int number, int length) {
        return String.format("%" + length + "s", Integer.toBinaryString(number)).replace(' ', '0');
    }

    public static int[] toIntArray(String binary) {
        int[] array = binary.chars().map((operand) -> operand == 48 /* '0' = 48 */ ? 0 : 1).toArray();
        return array;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, width, height, null);
        Color c = g2d.getColor();
        g2d.setColor(Color.GREEN);
        g2d.drawString(Arrays.toString(patternArray) + "", width, 10);
        g2d.setColor(c);
    }

    byte neightbors = 3;
    int pattern = 256, states = 1 << neightbors;
    int[] patternArray = toIntArray(toBinary(pattern, states));

    public void update() {
        if (currentCol >= image.getHeight()) {
            restart();
            return;
        }
        for (int i = 0; i < cells.length; i++) {
            doSth1(i);
        }
        currentCol++;
        int[] temp = cells;
        cells = nexts;
        nexts = temp;
    }

    private void doSth(int i) {
        int left = (i == 0) ? 0 : cells[i - 1];
        int current = cells[i];
        int right = (i == cells.length - 1) ? 0 : cells[i + 1];
        int p = 7 - ((left << 2) + (current << 1) + right);
        nexts[i] = patternArray[p];
        if (nexts[i] == 1) {
            setPixel(i, currentCol, white);
        }
    }

    private void doSth1(int i) {
        int value = 0;
        for (int j = neightbors - 1; j >= 0; j--) {
            int index = i - j + neightbors / 2;
            if (index < 0 || index >= cells.length) {
                value += 0 << j;
            } else {
                int c = cells[i - j + neightbors / 2];
                value += c << j;
            }
        }
        value = states - 1 - value;
        nexts[i] = patternArray[value];
        if (nexts[i] == 1) {
            setPixel(i, currentCol, white);
        }
    }

    private void setPixel(int x, int y, int color) {
        int pos = 0;
        pixel[pos++] = (color >> 16 & 0xff);
        pixel[pos++] = (color >> 8 & 0xff);
        pixel[pos++] = (color & 0xff);
        image.getRaster().setPixel(x, y, pixel);
    }

    private void restart() {
        pattern = (pattern + 1);
        patternArray = toIntArray(toBinary(pattern, states));
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 1, image.getWidth(), image.getHeight());
        g2d.dispose();
        for (int i = 0; i < cells.length; i++) {
            cells[i] = 0;
            nexts[i] = 0;
        }
        currentCol = 1;
        cells = Arrays.copyOf(initialCells, initialCells.length);
    }
}
