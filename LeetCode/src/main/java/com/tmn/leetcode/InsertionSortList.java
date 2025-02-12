package com.tmn.leetcode;

import java.util.Random;

public class InsertionSortList {

    public static ListNode insertionSortList(ListNode head) {
        ListNode start = head;
        ListNode current = head.next;
        while (current != null) {
            
        }
        return start;
    }
 // not done
    public static void main(String[] args) {
        int[] listNode = new int[100];
        Random random = new Random();
        for (int i = 0; i < listNode.length; i++) {
            int index = random.nextInt(i + 1);
            int temp = listNode[index];
            listNode[index] = i;
            listNode[i] = temp;
        }
        ListNode head = ListNode.from(listNode);
        System.out.println(head);
//        System.out.println(insertionSortList(head));
    }
}
