package com.tmn.leetcode;

public class RotatingTheBox {

    private static char[][] boxGrid = {
        {'#', '#', '*', '.', '*', '.'},
        {'#', '#', '#', '*', '.', '.'},
        {'#', '#', '#', '.', '#', '.'}
    };
//    private static char[][] boxGrid = {
//        {'#', '#', '.', '#'}
//    };
//    private static char[][] boxGrid = {
//        {'#', '.', '*', '.'},
//        {'#', '#', '*', '.'},};

    public static char[][] rotateTheBox(char[][] boxGrid) {
        int row = boxGrid.length;
        int col = boxGrid[0].length;
        for (int i = 0; i < row; i++) {
            int ground = col - 1;
            for (int j = col - 1; j >= 0; j--) {
                if (boxGrid[i][j] == '.') {
                    ground = j;
                    break;
                }
            }
            for (int j = ground - 1; j >= 0; j--) {
//                System.out.println(boxGrid[i][j]);
                // if cell is a stone and ground is empty
                if (boxGrid[i][j] == '#' && boxGrid[i][ground] == '.') {
                    // fall the cell, and move the ground up
                    boxGrid[i][j] = '.';
                    boxGrid[i][ground] = '#';
                    ground--;
                }

                // if next cell is an obtacle
                if (boxGrid[i][j] == '*') {
                    // move ground to next empty cell
                    for (; j >= 0; j--) {
                        if (boxGrid[i][j] == '.') {
                            ground = j;
                            break;
                        }
                    }
                }
            }
        }
        char[][] result = new char[col][row];
        for (int i = 0; i < col; i++) {
            for (int j = row - 1; j >= 0; j--) {
                result[i][row - 1 - j] = boxGrid[j][i];
            }
        }
        return result;
    }

    static void printResult(char[][] result) {
        for (char[] result1 : result) {
            for (char c : result1) {
                System.out.print(c + " ");
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        char[][] result = rotateTheBox(boxGrid);
        printResult(result);
    }
}
