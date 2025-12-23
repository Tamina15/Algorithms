package com.tmn.leetcode;

import java.util.PriorityQueue;

public class SmallestNumberInInfiniteSet {
    PriorityQueue<Integer> queue;
    int smallest;

    public SmallestNumberInInfiniteSet() {
        queue = new PriorityQueue<>(1000);
        smallest = 1;
    }

    public int popSmallest() {
        if (queue.isEmpty()) {
            return smallest++;
        } else {
            return queue.poll();
        }
    }

    public void addBack(int num) {
        if (num >= 1 && num < smallest && !queue.contains(num)) {
            queue.add(num);
        }
    }
}
