package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.List;

public class InsertionSortList {

    public static ListNode insertionSortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        List<ListNode> array = new ArrayList<>();
        while (head != null) {
            array.add(head);
            head = head.next;
        }
        array.sort((o1, o2) -> o1.val - o2.val);
        for (int i = 0; i < array.size() - 1; i++) {
            array.get(i).next = array.get(i + 1);
        }
        array.getLast().next = null;
        return array.get(0);
    }

    public static void main(String[] args) {
        ListNode head = ListNode.fromRange(1000, 200);
        System.out.println(head);
        System.out.println(insertionSortList(head));
    }
}
