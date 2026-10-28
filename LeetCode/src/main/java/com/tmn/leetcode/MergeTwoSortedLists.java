package com.tmn.leetcode;

/**
 * You are given the heads of two sorted linked lists.
 * <br>
 * <br>
 *
 * Merge the two lists into one sorted list.
 * The list should be made by splicing together the nodes of the first two
 * lists.
 * <br>
 * <br>
 *
 * Return the head of the merged linked list.
 *
 * @author NhatTranMinh
 */
public class MergeTwoSortedLists {

    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null || list2 == null) {
            if (list1 == null && list2 == null) {
                return null;
            }
            if (list1 == null) {
                return list2;
            } else {
                return list1;
            }
        }
        ListNode head, current1 = list1, current2 = list2;
        if (list1.val <= list2.val) {
            head = list1;
            current1 = list1.next;
        } else {
            head = list2;
            current2 = list2.next;
        }
        ListNode current = head;
        while (current1 != null && current2 != null) {
            if (current1.val <= current2.val) {
                current.next = current1;
                current = current1;
                current1 = current1.next;
            } else {
                current.next = current2;
                current = current2;
                current2 = current2.next;
            }
        }
        if (current1 != null) {
            current.next = current1;
        }
        if (current2 != null) {
            current.next = current2;
        }
        return head;
    }

    public static void main(String[] args) {
        System.out.println(mergeTwoLists(ListNode.from(1, 2, 4, 5), ListNode.from(1, 3, 4, 5, 5)));
    }
}
