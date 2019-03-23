package com.jeremy.demo.algorithm;

/**
 * Created by changqing.zhao on 2017/4/24.
 */

public class SortMethods {

    /**
     * 快速排序
     *一趟快速排序的算法是：
     * 1）设置两个变量i、j，排序开始的时候：i=0，j=N-1；
     * 2）以第一个数组元素作为关键数据，赋值给key，即key=A[0]；
     * 3）从j开始向前搜索，即由后开始向前搜索(j--)，找到第一个小于key的值A[j]，将A[j]的值赋给A[i]；
     * 4）从i开始向后搜索，即由前开始向后搜索(i++)，找到第一个大于key的A[i]，将A[i]的值赋给A[j]；
     * 5）重复第3、4步，直到i=j； (3,4步中，没找到符合条件的值，即3中A[j]不小于key,4中A[i]不大于key的时候改变j、i的值，使得j=j-1，i=i+1，直至找到为止。找到符合条件的值，进行交换的时候i， j指针位置不变。另外，i==j这一过程一定正好是i+或j-完成的时候，此时令循环结束）。
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
