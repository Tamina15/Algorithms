package com.tmn.leetcode;

/**
 * Given a linked list, swap every two adjacent nodes and return its head.
 * <br>
 * <br>
 * You must solve the problem without modifying the values
 * in the list's nodes (i.e., only nodes themselves may be changed.)
 * <br>
 * <br>
 * Ex: Input: [1,2,3,4], Output: [2,1,4,3]
 *
 * @author NhatTranMinh
 */
public class SwapNodesInPairs {

    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode left = head, right = left.next, previous;
        previous = left; // previous = 1
        left.next = right.next; // 1 -> 3
        right.next = left;// 2 -> 1
        head = right; // head = 2;
        left = left.next; // left = 3
        if (left != null) {
            right = left.next; // right = 4
        } else {
            right = null;
        }
        // p l r
        // 2 -> 1 -> 3 -> 4
        while (right != null) {
            previous.next = right; // 1 -> 4
            previous = left; // previous = 3
            left.next = right.next; // 3 -> null
            right.next = left; // 4 -> 3
            left = left.next; // left = null or left = 5
            if (left == null) {
                break;
            }
            right = left.next; // right = 6 or null

        }
        return head;
    }

    public static void main(String[] args) {
        // All possible cases
        System.out.println(swapPairs(null));
        System.out.println(swapPairs(ListNode.from(1)));
        System.out.println(swapPairs(ListNode.from(1, 2)));
        System.out.println(swapPairs(ListNode.from(1, 2, 3)));
        System.out.println(swapPairs(ListNode.from(1, 2, 3, 4)));
        System.out.println(swapPairs(ListNode.from(1, 2, 3, 4, 5)));
        System.out.println(swapPairs(ListNode.from(1, 2, 3, 4, 5, 6)));
        System.out.println(swapPairs(ListNode.from(1, 2, 3, 4, 5, 6, 7)));
        System.out.println(swapPairs(ListNode.from(1, 2, 3, 4, 5, 6, 7, 8)));
    }
}
