package com.tmn.cellularautomata;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

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
        this(cellsLength, null, null);
    }

    /**
     * Create a grid with a number of cell that will render to {@code width} and {@code height} values.
     *
     * @param cellsLength the number of cell
     * @param width       The appearance width of the grid
     * @param height      The appearance height of the grid
     */
    public CellularAutomata(int cellsLength, Integer width, Integer height) {
        this(cellsLength, width, height, null);
    }

    /**
     * Create a grid with a number of cells and an initial condition
     * that will render to {@code width} and {@code height} values.
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
        this.width = (width != null) ? width : cellsLength;
        this.height = (height != null) ? height : cellsLength;

        image = new BufferedImage(cellsLength, cellsLength, BufferedImage.TYPE_INT_RGB);
        pixel = new int[3];

        initialCells = new int[cellsLength];
        if (intitial != null) {
            for (int i = 0; i < intitial.length; i++) {
                set(initialCells, intitial[i], 0, 1, white);
            }
        } else {
            set(initialCells, initialCells.length / 2, 0, 1, white);
        }

        cells = Arrays.copyOf(initialCells, initialCells.length);
        nexts = new int[cellsLength];
    }

    /**
     * Draw the grid with an offset
     *
     * @param g2d     The Graphics object
     * @param offsetX horizontal offset
     * @param offsetY vertical offset
     */
    public void draw(Graphics2D g2d, int offsetX, int offsetY) {
        AffineTransform at = g2d.getTransform();

        g2d.translate(offsetX, offsetY);
        g2d.drawImage(image, 0, 0, width, height, null);

        g2d.setTransform(at);
    }

    /**
     * Draw the grid with no offset
     *
     * @param g2d The Graphics object
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
     * Value larger than 255 will roll back.
     */
    int rule = 150;
    /**
     * The numbers of possible next state. i.e 2<sup>{@code neighbors}</sup>.
     */
    int numberOfStates = 1 << neightbors;

    /**
     * The binary representation of the rule
     */
    int[] ruleArray = Utils.toBinaryArray(rule, numberOfStates);

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
     * This method can leave a state half-update, either current or next state.
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

    /**
     * Update a cell from the current state to the next state.
     *
     * @param i the index of the cell
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
     * @see neighbor_N1
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
    
    
    protected void restartToNextPattern() {
        restart(rule + 1);
    }

    public void restart(int rule) {
        this.rule = rule;
        ruleArray = Utils.toBinaryArray(this.rule, numberOfStates);
        Utils.newReversedArray(reversedPatternArray, ruleArray);
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
