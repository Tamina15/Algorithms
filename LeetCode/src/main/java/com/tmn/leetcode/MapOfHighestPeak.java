package com.tmn.leetcode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class MapOfHighestPeak {

    public static int[][] highestPeak(int[][] isWater) {
        int row = isWater.length;
        int col = isWater[0].length;
        int[][] result = new int[row][col];
        // No need priority queue 
        // because all elements that is added in the same loop will have equal value
        // 200ms faster
//        PriorityQueue<Integer[]> queue = new PriorityQueue<>((o1, o2) -> {
//            if (result[o1[0]][o1[1]] < result[o2[0]][o2[1]]) {
//                return 0;
//            } else {
//                return 1;
//            }
//        });

        // no need for Integer[]
        // 40ms faster
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                result[i][j] = -1;
                if (isWater[i][j] == 1) {
                    result[i][j] = 0;
                    queue.add(new int[]{i, j});
                }
            }
        }

        // multi-source BFS
        while (!queue.isEmpty()) {
            int n = queue.size();
            for (int i = 0; i < n; i++) {
                int[] position = queue.poll();
                int x = position[0];
                int y = position[1];
                int[] up = y > 0 ? new int[]{x, y - 1} : null;
                int[] right = x < row - 1 ? new int[]{x + 1, y} : null;
                int[] down = y < col - 1 ? new int[]{x, y + 1} : null;
                int[] left = x > 0 ? new int[]{x - 1, y} : null;
                int[][] neightbors = new int[][]{up, right, down, left};
                for (int[] neightbor : neightbors) {
                    if (neightbor != null) {
                        int nx = neightbor[0];
                        int ny = neightbor[1];
                        if (result[nx][ny] == -1) {
                            result[nx][ny] = result[x][y] + 1;
                            queue.add(neightbor);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[][] isWater = {{0, 0, 1, 0, 0, 0, 0, 0}, {1, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 1, 0, 0, 0, 0}};
        int[][] result = highestPeak(isWater);
        for (int[] i : result) {
            System.out.println(Arrays.toString(i));
        }
    }
}
