package com.tmn.leetcode;

public class ReverseLinkedList {

    static class ListNode {

        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

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
        ListNode l = new ListNode(0);
        ListNode now = l;
        for (int i = 1; i < 2; i++) {
            ListNode a = new ListNode(i);
            now.next = a;
            now = a;
        }

        ReverseLinkedList r = new ReverseLinkedList();
        ListNode result = r.reverseList(l);
        ListNode temp = result;
        while (temp != null) {
            System.out.println(temp.val);
            temp = temp.next;
        }
    }
}
