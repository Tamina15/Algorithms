package com.tmn.cellularautomata;

public class WrappedCellularAutomata extends CellularAutomata {

    public WrappedCellularAutomata() {
    }

    public WrappedCellularAutomata(int arrayLength) {
        super(arrayLength);
    }

    public WrappedCellularAutomata(int arrayLength, Integer width, Integer height) {
        super(arrayLength, width, height);
    }

    public WrappedCellularAutomata(int arrayLength, int[] intitial) {
        super(arrayLength, intitial);
    }

    public WrappedCellularAutomata(int arrayLength, Integer width, Integer height, int[] intitial) {
        super(arrayLength, width, height, intitial);
    }

    @Override
    protected void neighbor_N(int i) {
        int value = 0;
        int half = neightbors / 2;
        int length = cells.length;
        for (int j = neightbors - 1; j >= 0; j--) {
            int index = ((i - j + half) + length) % length;
            int c = cells[index];
            value += c << j;
        }
        value = states - 1 - value;
        nexts[i] = patternArray[value];
        if (nexts[i] == 1) {
            setPixel(i, currentCol, Integer.MAX_VALUE);
        }
    }

}
