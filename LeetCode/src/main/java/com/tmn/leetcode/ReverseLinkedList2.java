package com.tmn.leetcode;

public class ReverseLinkedList2 {

    public static ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode current = head;
        int diff = right - left;
        for (int i = left - 2; i > 0; i--) {
            current = current.next;
        }
        ListNode next = current;
        for (int i = diff; i > 0; i--) {
            next = next.next;
        }
        if (left == 1) {
            head = next;
        }
        System.out.println(current.val);
        System.out.println(next.val);
        // ListNode previous = null;
        // while (current.next != null) {
        // current.next = previous;
        // previous = current;
        // current = next;
        // next = next.next;
        // }
        // current.next = previous;
        return head;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.from(1, 2, 3, 4, 5);
        var result = reverseBetween(head, 1, 4);
        System.out.println(result);
    }
}
