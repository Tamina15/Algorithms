package com.tmn.cellularautomata;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class CellularAutomata {

    /**
     * The appearance width of the grid
     */
    protected int width;

    /**
     * The appearance height of the grid
     */
    protected int height;

    /**
     * The image representing the 2D grid of cells.
     * Each pixel represents 1 cell.
     * Use to replace {@code Graphics.drawLine} for performance
     */
    BufferedImage image;

    int currentCol = 1;

    /**
     * The initial state of the grid
     */
    int[] initialCells;

    /**
     * The current state of the grid
     */
    int[] cells;

    /**
     * The next state of the grid
     */
    int[] nexts;

    /**
     * The color WHITE in integer value.
     */
    static int white = Color.WHITE.getRGB();

    /**
     * Represents one pixel as [red, green, blue]
     */
    int[] pixel;

    /**
     * Control parameters
     */
    Option option;

    public boolean done;

    /**
     * Create an empty grid
     */
    public CellularAutomata() {
        this(0, null, null);
    }

    /**
     * Create a grid with a number of cell and default dimension.
     *
     * @param cellsLength the number of cell
     */
    public CellularAutomata(int cellsLength) {
        this(cellsLength, null, null, null);

    }

    /**
     * Create a grid with a number of cell
     * that will render {@code width} and {@code height} long.
     *
     * @param cellsLength the number of cell
     * @param width       The appearance width of the grid
     * @param height      The appearance height of the grid
     */
    protected CellularAutomata(int cellsLength, Integer width, Integer height) {
        if (cellsLength == 191) {
            System.out.println("Length of 191 can introduce cycle with no-wrap 3-neightbor option");
        }
        int length = cellsLength;
        this.width = (width != null) ? width : length;
        this.height = (height != null) ? height : length;

        image = new BufferedImage(length, cellsLength, BufferedImage.TYPE_INT_RGB);
        pixel = new int[3];

        initialCells = new int[length];

        cells = new int[length];
        nexts = new int[length];
    }

    /**
     * Create a grid with a number of cells and an initial condition
     * that will render {@code width} and {@code height} long.
     *
     * @param cellsLength the number of cell
     * @param intitial    array contains index of 'on' cell
     */
    public CellularAutomata(int cellsLength, int[] intitial) {
        this(cellsLength, null, null, intitial);
    }

    /**
     * Create a grid with a number of cells and an initial condition
     * that will render {@code width} and {@code height} long.
     *
     * @param cellsLength the number of cell
     * @param width       The appearance width of the grid
     * @param height      The appearance height of the grid
     * @param intitial    array contains index of 'on' cell
     */
    public CellularAutomata(int cellsLength, Integer width, Integer height, int[] intitial) {
        this(cellsLength, width, height);

        if (intitial != null) {
            for (int i = 0; i < intitial.length; i++) {
                set(initialCells, intitial[i], 0, 1, white);
            }
        } else {
            set(initialCells, initialCells.length / 2, 0, 1, white);
        }

        Utils.copyOf(cells, initialCells);
    }

    private static final int[] randomPadding = Utils.toBinaryArray(1515151515151515151l, 64);

    /**
     * Create a grid with a number of cells and an initial condition
     * that will render {@code width} and {@code height} long.
     *
     * @param cellsLength the number of cell
     * @param width       The appearance width of the grid
     * @param height      The appearance height of the grid
     * @param seed        the initial states of the cells
     */
    public CellularAutomata(int cellsLength, Integer width, Integer height, long seed) {
        this(cellsLength, width, height);

        int[] bits = Utils.toBinaryArray(seed, 64);
        int[] reversedBits = Utils.newReversedArray(bits);
        interleave(initialCells, bits, randomPadding, reversedBits);
        Utils.copyOf(cells, initialCells);
    }

    public CellularAutomata(int cellsLength, long seed) {
        this(cellsLength, null, null, seed);
    }

    private int[] interleave(int[] output, int[]... arrays) {
        for (int i = 0; i < output.length; i++) {
            try {
                int a = arrays[i % arrays.length][i / arrays.length];
                output[i] = a;
                if (a == 1) {
                    set(initialCells, i, 0, 1, white);
                }
            } catch (Exception e) {
//                break;
            }
        }
        return output;
    }

    /**
     * Draw the grid with an offset
     *
     * @param g2d     The Graphics2D object
     * @param offsetX horizontal offset
     * @param offsetY vertical offset
     */
    public void draw(Graphics2D g2d, int offsetX, int offsetY) {
        AffineTransform at = g2d.getTransform();

        g2d.translate(offsetX, offsetY);
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.drawString(Arrays.toString(ruleArray), 0, -10);

        g2d.setTransform(at);
    }

    /**
     * Draw the grid with no offset
     *
     * @param g2d The Graphics2D object
     */
    public void draw(Graphics2D g2d) {
        draw(g2d, 0, 0);
    }

    /**
     * The number of neighbor in the neighborhood, including itself
     */
    byte neightbors = 3;

    /**
     * The rule. Range from 0 to 255.
     * <br>
     * Value less than 0 can cause unexpected behavior.
     * <br>
     * Value larger than 255 will be rolled back.
     */
    int rule = 150;

    /**
     * The numbers of possible next state. i.e 2<sup>{@code neighbors}</sup>.
     */
    int numberOfStates = 1 << neightbors;

    /**
     * The binary representation of the rule
     */
    int[] ruleArray = Utils.toExactBinaryArray(rule, numberOfStates, true);

    /**
     * The inverse binary representation of the rule
     */
    int[] reversedPatternArray = Utils.newReversedArray(ruleArray);

    /**
     * Current index of the cell to be update.
     */
    private int cI = 0;

    /**
     * Update a number of cells from the current state to the next state.
     * <br>
     * This method can leave a state half-updated.
     *
     * @param amount the number of cells to update
     */
    public void update(int amount) {
        for (int i = 0; i < amount; i++) {
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

    /**
     * Update the current state to the next state.
     */
    public void update() {
        if (currentCol >= image.getHeight()) {
            currentCol = 1;
//            done = true;
            return;
        }
        clearLine(currentCol + 1);
        for (int i = 0; i < cells.length; i++) {
            neighbor_N(i);
        }
        currentCol++;
        int[] temp = cells;
        cells = nexts;
        nexts = temp;
    }

    /**
     * Update a cell from the current state to the next state.
     *
     * @param i the index of the cell
     * @see neighbor_N2
     */
    protected void neighbor_N(int i) {
        int value = 0;
        for (int j = neightbors - 1; j >= 0; j--) {
            int index = i - j + neightbors / 2;
            int c = index < 0 || index >= cells.length ? 0 : cells[index];
            value += c << j;
        }
        value = numberOfStates - 1 - value;
        nexts[i] = ruleArray[value];
        if (nexts[i] == 1) {
            setPixel(i, currentCol, white);
        }
    }

    /**
     * Update a cell from the current state to the next state.
     *
     * @param i the index of the cell
     * @see neighbor_N
     */
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

    /**
     * Set the value and color of a cell
     *
     * @param array the state array
     * @param x     the column of the cell
     * @param y     the row of the cell
     * @param value value to be set
     * @param color color to be set
     */
    private void set(int[] array, int x, int y, int value, int color) {
        array[x] = value;
        setPixel(x, y, color);
    }

    /**
     * Set color of a pixel.
     *
     * @param x     the x-coordinate of the pixel
     * @param y     the y-coordinate of the pixel
     * @param color the new color to the pixel
     */
    protected void setPixel(int x, int y, int color) {
        int pos = 0;
        pixel[pos++] = (color >> 16 & 0xff);
        pixel[pos++] = (color >> 8 & 0xff);
        pixel[pos++] = (color & 0xff);
        image.getRaster().setPixel(x, y, pixel);
    }

    protected void restartToNextRule() {
        restartToRule(rule + 1);
    }

    public void restartToRule(int rule) {
        this.rule = rule;
        ruleArray = Utils.toExactBinaryArray(this.rule, numberOfStates, true);
        reversedPatternArray = Utils.newReversedArray(ruleArray);
        restart();
    }

    protected void restartToRandomRule() {
        throw new UnsupportedOperationException();
    }

    protected void restartToRandomInintial() {
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
        done = false;
    }

    protected void clearImage() {
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 1, image.getWidth(), image.getHeight());
        g2d.dispose();
    }

    protected void clearLine(int col) {
        col = col % image.getHeight();
        if (col == 0) {
            col = 1;
        }
        int black = Color.black.getRGB();
        for (int i = 0; i < cells.length; i++) {
            setPixel(i, col, black);
        }
    }

    private void printImage(String fileExtension, String path) {
        try {
            File f = new File(path);
            ImageIO.write(image, fileExtension, f);
            System.out.println("Print image to " + f.getPath());
        } catch (IOException ex) {
            Logger.getLogger(CellularAutomata.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
