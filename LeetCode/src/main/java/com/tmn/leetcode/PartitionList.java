package com.tmn.leetcode;

/*
Given the head of a linked list and a value x, 
partition it such that all nodes less than x 
come before nodes greater than or equal to x.

You should preserve the original relative order 
of the nodes in each of the two partitions.
 */
public class PartitionList {

    public static ListNode partition(ListNode head, int x) {
        if (head == null || head.next == null) {
            return head;
        }
        // temp1 = smaller, temp2 = larger
        ListNode temp1 = new ListNode(), temp2 = new ListNode();
        ListNode current1 = temp1, current2 = temp2;
        ListNode current = head;
        while (current != null) {
            if (current.val < x) {
                current1.next = current;
                current1 = current;
            } else {
                current2.next = current;
                current2 = current;
            }
            current = current.next;
        }
        current1.next = temp2.next; // concat smaller to larger
        current2.next = null; // remove last smaller node. ex: 5->2 = 5->null
        return temp1.next;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.from(2, 1);
        System.out.println(partition(head, 2));
    }
}
