package com.tmn.leetcode;

public class RemoveDuplicatesFromSortedList {

    public static ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode current = head, next;
        while (current != null) {
            next = current;
            while (next != null && current.val == next.val) {
                next = next.next;
            }
            current.next = next;
            current = next;
        }
        return head;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.from(1, 1, 2, 3, 3);
        var result = deleteDuplicates(head);
        System.out.println(result);
    }
}
