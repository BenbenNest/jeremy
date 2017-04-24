package com.jeremy.demo.algorithm;

/**
 * Created by changqing.zhao on 2017/4/24.
 */

public class SortMethods {

    /**
     * 快速排序
     *
     * @param a
     */
    public static void quick(int[] a) {
        if (null != a && a.length > 0) {
            quickSort(a, 0, a.length - 1);
        }
    }

    public static void quickSort(int[] a, int low, int high) {
        if (low < high) {
            int middle = getMiddle(a, low, high);
            quickSort(a, 0, middle - 1);
            quickSort(a, middle + 1, high);
        }
    }

    public static int getMiddle(int[] a, int low, int high) {
        int temp = a[low];
        while (low < high) {
            while (low < high && a[high] >= temp) {
                high--;
            }
            a[low] = a[high];
            while ((low < high && a[low] <= temp)) {
                low++;
            }
            a[high] = a[low];
        }
        return low;
    }

}
