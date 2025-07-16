package com.tmn.leetcode;

public class ReverseLinkedList {

    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode previous = null;
        ListNode current = head;
        ListNode next = head.next;
        while (current.next != null) {
            current.next = previous;
            previous = current;
            current = next;
            next = next.next;
        }
        current.next = previous;
        return current;
    }

    public static void main(String[] args) {
        ListNode l = ListNode.shuffle(20);
        ReverseLinkedList r = new ReverseLinkedList();
        System.out.println(l);
        ListNode result = r.reverseList(l);
        System.out.println(result);
    }
}
