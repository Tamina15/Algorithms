package com.tmn.leetcode;

public class RotateList {

    // Given the head of a linked list, rotate the list to the right by k places.
    public static ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) {
            return head;
        }
        int n = 1;
        ListNode current = head;
        while (current.next != null) {
            n++;
            current = current.next;
        }
        // current is now at last node

        k = n - (k % n);
        ListNode newTail = head;
        for (int i = 1; i < k; i++) {
            newTail = newTail.next;
        }
        current.next = head; // concat tail to head
        ListNode newHead = newTail.next; // newTail.next will not be null
        newTail.next = null; // seperate new head and new tail;
        return newHead;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            ListNode head = ListNode.fromRange(1, 6);
            System.out.println(rotateRight(head, i));
        }

    }
}
