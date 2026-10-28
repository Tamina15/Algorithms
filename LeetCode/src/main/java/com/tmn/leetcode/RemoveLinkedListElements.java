package com.tmn.leetcode;

public class RemoveLinkedListElements {

    public static ListNode removeElements(ListNode head, int val) {
        ListNode temp = new ListNode(0, head);
        ListNode current = temp;
        ListNode next = head;
        while (next != null) {
            if (next.val == val) {
                current.next = next.next;
            } else {
                current = current.next;
            }
            next = next.next;
        }
        return temp.next;
    }

    public static void main(String[] args) {
        // var head = ListNode.randomize(10000, 1, 51);
        var head = ListNode.from(7, 7, 7, 7);
        head = removeElements(head, 7);
        System.out.println(head);
    }
}
