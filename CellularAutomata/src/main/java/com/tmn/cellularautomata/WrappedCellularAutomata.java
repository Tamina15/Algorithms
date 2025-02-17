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

    public WrappedCellularAutomata(int cellsLength, Integer width, Integer height, long seed) {
        super(cellsLength, width, height, seed);
    }

    @Override
    protected void neighbor_N(int i) {
        int value = 0;
        int half = neightbors / 2;
        int length = cells.length;
        for (int j = neightbors - 1; j >= 0; j--) {
            // Wrapped around
            int index = ((i - j + half) + length) % length;
            value += cells[index] << j;
        }
        value = numberOfStates - 1 - value;
        nexts[i] = ruleArray[value];
        if (nexts[i] == 1) {
            setPixel(i, currentCol, Integer.MAX_VALUE);
        }
    }

}
