package com.tmn.leetcode;

/**
 * There is a singly-linked list head and we want to delete a node node in it.
 *
 * You are given the node to be deleted node.
 * You will not be given access to the first node of head.
 *
 * All the values of the linked list are unique,
 * and it is guaranteed that the given node node is not the last node in the linked list.
 *
 * Delete the given node. Note that by deleting the node,
 * we do not mean removing it from memory. We mean:
 *
 * The value of the given node should not exist in the linked list.
 * The number of nodes in the linked list should decrease by one.
 * All the values before node should be in the same order.
 * All the values after node should be in the same order.
 *
 * @author NhatTranMinh
 */
public class DeleteNodeInALinkedList {

    public static void deleteNode(ListNode node) {
        ListNode current = node;
        ListNode next = current.next;
        while (next.next != null) {
            current.val = next.val;
            current = next;
            next = next.next;
        }
        current.val = next.val;
        current.next = null;
    }

    public static void main(String[] args) {
        ListNode head = ListNode.fromRange(0, 20);
        deleteNode(head);
        System.out.println(head);
    }
}
