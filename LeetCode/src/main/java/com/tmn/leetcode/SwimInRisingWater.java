package com.tmn.leetcode;

import java.util.Arrays;
import java.util.PriorityQueue;

public class SwimInRisingWater {

    static class Cell implements Comparable<Cell> {

        int v, x, y;
        boolean inqueue;
        Cell previous;

        public Cell(int v, int x, int y) {
            this.v = v;
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Cell o) {
            return v - o.v;
        }
    }

    public static int swimInWater(int[][] grid) {
        for (int[] i : grid) {
            System.out.println(Arrays.toString(i));
        }
        Cell[][] cells = new Cell[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                cells[i][j] = new Cell(grid[i][j], i, j);
            }
        }
        PriorityQueue<Cell> queue = new PriorityQueue();
        queue.add(cells[0][0]);
        cells[0][0].inqueue = true;
        int a = 0;
        while (!queue.isEmpty()) {
            Cell c = queue.poll();
            System.out.println(c.x + " " + c.y);
            if (c.v > a) {
                a = c.v;
            }
            if (c.x == (grid.length - 1) && c.y == (grid.length - 1)) {
                break;
            }
            doSth(cells, c, queue);
        }
        return a;
    }

    public static void doSth(Cell[][] cells, Cell cell, PriorityQueue<Cell> queue) {
        int x = cell.x;
        int y = cell.y;
        Cell up = y > 0 ? cells[x][y - 1] : null;
        Cell right = x < cells.length - 1 ? cells[x + 1][y] : null;
        Cell down = y < cells.length - 1 ? cells[x][y + 1] : null;
        Cell left = x > 0 ? cells[x - 1][y] : null;
        if (up != null && !up.inqueue) {
            queue.add(up);
            up.inqueue = true;
            up.previous = cell;
        }
        if (down != null && !down.inqueue) {
            queue.add(down);
            down.inqueue = true;
            down.previous = cell;
        }
        if (left != null && !left.inqueue) {
            queue.add(left);
            left.inqueue = true;
            left.previous = cell;
        }
        if (right != null && !right.inqueue) {
            queue.add(right);
            right.inqueue = true;
            right.previous = cell;
        }
    }

    public static void main(String[] args) {
        int[][] arr = {{0, 1, 2, 3, 4}, {24, 23, 22, 21, 5}, {12, 13, 14, 19, 16}, {11, 17, 18, 19, 20}, {10, 9, 8, 7, 6}};
        System.out.println(swimInWater(arr));
    }
}
