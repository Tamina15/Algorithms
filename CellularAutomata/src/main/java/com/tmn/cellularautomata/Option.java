package com.tmn.cellularautomata;

public class Option {

    int cellsLength;
    Integer width;
    Integer height;
    int[] intitial;
    byte neightbors = 3;
    int pattern = 150, numberOfStates = 1 << neightbors;
    int[] patternArray = Utils.toBinaryArray(pattern, numberOfStates);
    int[] reversedPatternArray = Utils.newReversedArray(patternArray);
    boolean wrapped;
}
