package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.List;

public class FindEventualSafeStates {

    public static List<Integer> eventualSafeNodes(int[][] graph) {
        int[] cache = new int[graph.length];
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < graph.length; i++) {
            if (traverse(i, graph, cache)) {
                list.add(i);
            }
        }
        return list;
    }

    //cache[i] = 0, not visit
    //cache[i] = 1, visited, no result yet
    //cache[i] = 2, visited, false
    //cache[i] = 3, visited, true
    private static boolean traverse(int i, int[][] graph, int[] cache) {
        if (cache[i] > 1) {
            return cache[i] == 3;
        }
        for (int j = 0; j < graph[i].length; j++) {
            int v = graph[i][j];
            // looped, can not reach safe node
            if (cache[v] == 1) {
                return false;
            }
            cache[i] = 1;
            boolean result = traverse(v, graph, cache);
            if (!result) {
                cache[i] = 2;
                return false;
            }
        }
        cache[i] = 3;
        return true;
    }

    public static void main(String[] args) {
//        int[][] graph = {{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}};
//        int[][] graph = {{1, 2, 3, 4}, {1, 2}, {3, 4}, {0, 4}, {}};
        int[][] graph = {{}, {0, 2, 3, 4}, {3}, {4}, {}};
        System.out.println(eventualSafeNodes(graph));
    }

}
