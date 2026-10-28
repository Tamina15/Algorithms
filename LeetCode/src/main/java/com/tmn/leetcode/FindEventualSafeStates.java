package com.tmn.leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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

    //cache[i] = 0, not visit
    //cache[i] = 1, visited, no result yet
    //cache[i] = 2, visited, false
    //cache[i] = 3, visited, true
    public static List<Integer> eventualSafeNodes1(int[][] graph) {
        int[] cache = new int[graph.length];
        List<Integer> list = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < graph.length; i++) {
            if (graph[i].length == 0) {
                cache[i] = 3;
            } else {
                stack.push(i);
            }
        }
        while (!stack.isEmpty()) {
            int i = stack.peekLast();
            if (cache[i] > 1) {
                stack.removeLast();
                continue;
            }
            boolean success = true, traverseChild = false;
            for (int j = 0; j < graph[i].length; j++) {
                int v = graph[i][j];
                if (cache[v] == 3) {
                    continue;
                }
                // looped, can not reach safe node
                if (cache[v] == 1 || cache[v] == 2) {
                    cache[i] = 2;
                    success = false;
                    break;
                }
                cache[i] = 1;
                stack.addLast(v);
                traverseChild = true;
            }
            if (success && !traverseChild) {
                cache[i] = 3;
            }
        }
        for (int i = 0; i < cache.length; i++) {
            if (cache[i] == 3) {
                list.add(i);
            }
        }
        return list;
    }

    static void print(int[] stack) {
        StringBuilder sb = new StringBuilder("[");
        for (var s : stack) {
            sb.append(String.format("%3d,", s));
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    static void print(Object[] stack) {
        StringBuilder sb = new StringBuilder("[");
        for (var s : stack) {
            sb.append(String.format("%3d,", s));
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        int[][][] graphs = {
            {{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}},
            {{1, 2, 3, 4}, {1, 2}, {3, 4}, {0, 4}, {}},
            {{}, {0, 2, 3, 4}, {3}, {4}, {}},
            {{}, {0, 1, 2, 3, 4}, {2, 3}, {4}, {}},
            {{4}, {0, 1, 2, 3, 4}, {2, 3}, {2, 4}, {1}},
            {{0}, {}}, {{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}},
            {{1, 2, 3, 4}, {1, 2}, {3, 4}, {0, 4}, {}},
            {{}, {0, 2, 3, 4}, {3}, {4}, {}},
            {{17, 21, 22, 27}, {11, 12, 14, 19, 23}, {4, 27}, {16, 18, 20, 27}, {5, 14, 19, 27, 28}, {7, 15, 21, 25, 29}, {7, 10, 18, 28}, {12, 14, 21, 23, 29}, {9, 12, 18, 22, 24}, {19, 21, 25}, {15, 18, 21, 28}, {19, 23, 24, 28}, {13, 19, 21}, {14, 19, 25}, {2, 26, 27}, {}, {}, {3, 23, 24, 25, 26}, {19, 20, 24, 27, 29}, {23, 25}, {11, 22, 24, 25, 26, 27, 29}, {24, 25}, {23, 29}, {24, 25, 28, 29}, {27}, {26, 27, 28, 29}, {}, {28, 29}, {29}, {}},
            {{1, 2, 3, 4, 5, 6}, {}, {}, {}, {}, {}, {6, 5, 4, 3, 2, 1}},
            {{1}, {2}, {3}, {4}, {5}, {6}, {}}};
//        int[][] graph = {{1}, {2}, {3}, {4}, {5}, {6}, {}};
        int[][] graph = {{1, 2, 3, 4, 5, 6}, {}, {}, {}, {}, {}, {6, 5, 4, 3, 2, 1}};
//        int[][] graph = {{1, 2, 3, 4}, {1, 2}, {3, 4}, {0, 4}, {}};
//        int[][] graph = {{}, {0, 2, 3, 4}, {3}, {4}, {}};
//        int[][] graph = {{17, 21, 22, 27}, {11, 12, 14, 19, 23}, {4, 27}, {16, 18, 20, 27}, {5, 14, 19, 27, 28}, {7, 15, 21, 25, 29}, {7, 10, 18, 28}, {12, 14, 21, 23, 29}, {9, 12, 18, 22, 24}, {19, 21, 25}, {15, 18, 21, 28}, {19, 23, 24, 28}, {13, 19, 21}, {14, 19, 25}, {2, 26, 27}, {1}, {4}, {3, 23, 24, 25, 26}, {19, 20, 24, 27, 29}, {23, 25}, {11, 22, 24, 25, 26, 27, 29}, {24, 25}, {23, 29}, {24, 25, 28, 29}, {27}, {26, 27, 28, 29}, {9}, {28, 29}, {29}, {9}};
//        int[][] graph = new int[100][100];
//        for (int i = 0; i < 100; i++) {
//            graph[i] = new int[100];
//            for (int j = 0; j < 100; j++) {
//                graph[i][j] = j;
//            }
//        }
//        System.out.println(eventualSafeNodes1(graph));
        for (int a = 0; a < 100; a++) {
            long start = System.nanoTime();
            for (int i = 0; i < 100; i++) {
                for (var g : graphs) {
                    eventualSafeNodes(g);
                }
            }
            var result = eventualSafeNodes(graph);
            long end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000 + " ms");

            start = System.nanoTime();
            for (int i = 0; i < 100; i++) {
                for (var g : graphs) {
                    eventualSafeNodes1(g);
                }
            }
            var result2 = eventualSafeNodes1(graph);
            end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000 + " ms");
            System.out.println("");
        }
    }
}
