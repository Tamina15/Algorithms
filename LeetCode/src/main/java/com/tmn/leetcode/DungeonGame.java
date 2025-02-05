package com.tmn.leetcode;

import java.util.PriorityQueue;

public class DungeonGame {

    public static int calculateMinimumHP(int[][] dungeon) {
        int result = 0;
        int m = dungeon.length;
        int n = dungeon[0].length;
        PriorityQueue<Integer> queue = new PriorityQueue<>((o1, o2) -> {
            if (dungeon[o1 / m][o1 % m] < dungeon[o2 / m][o2 % m]) {
                return 1;
            } else {
                return 0;
            }
        });
        queue.add(0);
        while (!queue.isEmpty()) {
            
        }
        return result;
    }
    // not done
    public static void main(String[] args) {
        int[][] dungeon = {{-2, -3, 3}, {-5, -10, 1}, {10, 30, -5}};
        System.out.println(calculateMinimumHP(dungeon));
    }
}
