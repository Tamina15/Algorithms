package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Easy {

    public static int SearchInsertPosition(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                left = mid;
                break;
            } else {
                if (nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return left;
    }

    public static int LengthOfLastWord(String s) {
        int i = s.length() - 1;
        int count = 0;
        while (s.charAt(i) == ' ') {
            i--;
        }
        while (s.charAt(i) != ' ') {
            i--;
            count++;
        }
        return count;
    }

    /**
     * You are climbing a staircase. It takes n steps to reach the top.
     * Each time you can either climb 1 or 2 steps.
     * In how many distinct ways can you climb to the top?
     *
     * @param n
     * @return
     */
    public static int ClimbingStairs(int n) {
        int[] cache = new int[n];
        return climb(n, cache);
    }

    public static int climb(int n, int[] cache) {
        int count = 0;
        if (n <= 1) {
            return 1;
        }
        if (cache[n - 1] == 0) {
            count += climb(n - 1, cache);
            cache[n - 1] = count;
        } else {
            count += cache[n - 1];
        }
        if (cache[n - 2] == 0) {
            count += climb(n - 2, cache);
            cache[n - 2] = count;
        } else {
            count += cache[n - 2];
        }
        return count;
    }

    /**
     * Merge nums1 and nums2 into a single array sorted in non-decreasing order.
     *
     * m and n, representing the number of elements in nums1 and nums2 respectively.
     *
     * nums1 has a length of m + n,
     * where the first m elements denote the elements that should be merged,
     * and the last n elements are set to 0 and should be ignored.
     * nums2 has a length of n.
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public static void MergeSortedArray(int[] nums1, int m, int[] nums2, int n) {
        int[] nums3 = new int[m];
        System.arraycopy(nums1, 0, nums3, 0, m);
        int i = 0, j = 0, k = 0;
        while (i < m && j < n) {
            if (nums3[i] < nums2[j]) {
                nums1[k++] = nums3[i];
                i++;
            } else {
                nums1[k++] = nums2[j];
                j++;
            }
        }
        while (i < m) {
            nums1[k++] = nums3[i];
            i++;
        }
        while (j < n) {
            nums1[k++] = nums2[j];
            j++;
        }
    }

    public static boolean PathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return false;
        }
        if (root.left == null && root.right == null) {
            return root.val == targetSum;
        }
        boolean left = PathSum(root.left, targetSum - root.val);
        boolean right = PathSum(root.right, targetSum - root.val);
        return left || right;
    }

    public static List<List<Integer>> PathSumII(TreeNode root, int targetSum) {
        List<List<Integer>> list = new ArrayList<>();
        List<Integer> cache = new ArrayList<>();
        pathSumII(root, targetSum, list, cache, 0);
        return list;
    }

    public static void pathSumII(TreeNode root, int targetSum, List<List<Integer>> list, List<Integer> cache, int level) {
        if (root == null) {
            return;
        }
        if (root.left == null && root.right == null) {
            if (root.val == targetSum) {
                List<Integer> l = new ArrayList<>(level);
                for (int i = 0; i < level; i++) {
                    l.add(cache.get(i));
                }
                l.add(root.val);
                list.add(l);
            }
            return;
        }
        if (level == cache.size()) {
            cache.add(root.val);
        } else {
            cache.set(level, root.val);
        }
        pathSumII(root.left, targetSum - root.val, list, cache, level + 1);
        pathSumII(root.right, targetSum - root.val, list, cache, level + 1);
    }

    public static List<List<Integer>> PascalsTriangle(int numRows) {
        List<List<Integer>> list = new ArrayList<>(numRows);
        list.add(List.of(1));
        for (int i = 2; i <= numRows; i++) {
            List<Integer> l = new ArrayList<>(i);
            l.add(1);
            var previous = list.get(i - 2);
            for (int j = 0; j < previous.size() - 1; j++) {
                l.add(previous.get(j) + previous.get(j + 1));
            }
            l.add(1);
            list.add(l);
        }
        return list;
    }

    /* Given an integer rowIndex, return the rowIndexth (0-indexed) row of the Pascal's triangle. */
    public static List<Integer> PascalsTriangleII(int rowIndex) {
        int n = rowIndex + 1;
        int[] row = new int[n];
        row[0] = 1;
        row[rowIndex] = 1;
        int previous = 1;
        for (int k = 1; k < row.length / 2 + 1; k++) {
            int current = (int) (previous * ((rowIndex + 1 - k) * 1.0 / (k * 1.0)));
            row[k] = current;
            row[n - k - 1] = current;
            previous = current;
        }
        List<Integer> list = new ArrayList<>();
        for (int i : row) {
            list.add(i);
        }
        return list;
    }

    /* Given an integer rowIndex, return the rowIndexth (0-indexed) row of the Pascal's triangle. */
    public static List<Integer> PascalsTriangleII2(int rowIndex) {
        List<Integer> res = new ArrayList<>();
        res.add(1);
        long prev = 1;
        for (int k = 1; k <= rowIndex; k++) {
            long next_val = prev * (rowIndex - k + 1) / k;
            res.add((int) next_val);
            prev = next_val;
        }
        return res;
    }

    /**
     * You are given an array prices where prices[i]
     * is the price of a given stock on the ith day.
     *
     * You want to maximize your profit by choosing a single day
     * to buy one stock and choosing a different day in the future to sell that stock.
     *
     * Return the maximum profit you can achieve from this transaction.
     * If you cannot achieve any profit, return 0.
     *
     * @param prices
     * @return
     */
    public static int BestTimeToBuyAndSellStock(int[] prices) {
        int left = 0, right = 0;
        int n = prices.length;
        int max = 0;
        while (right < n) {
            int price = prices[right] - prices[left];
            if (price > max) {
                max = price;
            }
            if (prices[right] < prices[left]) {
                left = right;
            } else {
                right++;
            }
        }
        return max;
    }

    public static boolean ValidPalindrome(String s) {
        s = s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        boolean isPalindrome = true;
        int n = s.length();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) != s.charAt(n - i - 1)) {
                isPalindrome = false;
                break;
            }
        }
        return isPalindrome;
    }

    public static boolean LinkedListCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        return FloydsCycleDetection(head);
    }

    public static boolean FloydsCycleDetection(ListNode head) {
        ListNode tortoise = head.next;
        ListNode hare = head.next.next;
        while (tortoise != null && hare != null && tortoise != hare) {
            tortoise = tortoise.next;
            var n = hare.next;
            if (n == null) {
                hare = n;
                break;
            }
            hare = n.next;
        }
        if (tortoise == null || hare == null) {
            return false;
        }
        return true;
        // It is sure that linked list contains cycle
//        int mu = 0;
//        tortoise = head;
//        while (tortoise != hare) {
//            tortoise = tortoise.next;
//            hare = hare.next;
//            mu++;
//        }
//        int lam = 1;
//        hare = tortoise.next;
//        while (tortoise != hare) {
//            hare = hare.next;
//            lam++;
//        }
//        return new int[]{lam, mu};
    }

    public static int MajorityElement(int[] nums) {
        if (nums.length == 1 || nums.length == 2) {
            return nums[0];
        }
        return Boyer_MooreMajorityVoteAlgorithm(nums);
    }

    public static int Boyer_MooreMajorityVoteAlgorithm(int[] nums) {
        int m = nums[0];
        int c = 0;
        for (int i = 0; i < nums.length; i++) {
            int x = nums[i];
            if (c == 0) {
                m = x;
                c = 1;
            } else {
                if (m == x) {
                    c++;
                } else {
                    c--;
                }
            }
        }
        return m;
    }

    public static boolean HappyNumber(int n) {
        HashSet<Integer> set = new HashSet<>();
        while (n > 1 && !set.contains(n)) {
            set.add(n);
            n = PerfectDigitalInvariantFunction(n);
        }
        return n == 1;
    }

    public static int PerfectDigitalInvariantFunction(int n) {
        int total = 0;
        while (n > 0) {
            int a = n % 10;
            total += a * a;
            n = n / 10;
        }
        return total;
    }

    public static List<String> SummaryRanges(int[] nums) {
        List<String> list = new ArrayList<>();
        int n = nums.length;
        if (n == 0) {
            return list;
        }
        int start = nums[0];
        int previous = nums[0];
        for (int i = 1; i < n; i++) {
            int now = nums[i];
            if (now - previous != 1) {
                if (previous == start) {
                    list.add(Integer.toString(start));
                } else {
                    list.add(start + "->" + previous);
                }
                start = now;
            }
            previous = now;
        }
        if (previous == start) {
            list.add(Integer.toString(start));
        } else {
            list.add(start + "->" + previous);
        }
        return list;
    }

    /*
      Reshape an m x n matrix into a new one
      with a different size r x c keeping its original data.
      
      If the reshape operation with given parameters is possible and legal,
      output the new reshaped matrix; Otherwise, output the original matrix.
     */
    public static int[][] ReshapeTheMatrix(int[][] mat, int r, int c) {
        int a = mat.length;
        int b = mat[0].length;
        if (a * b != r * c) {
            return mat;
        }
        int[][] m2 = new int[r][c];
        for (int i = 0; i < a * b; i++) {
            m2[i / c][i % c] = mat[i / b][i % b];
        }
        return m2;
    }

    public static void main(String[] args) {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        System.out.println(Arrays.deepToString(ReshapeTheMatrix(matrix, 2, 6)));
    }
}
