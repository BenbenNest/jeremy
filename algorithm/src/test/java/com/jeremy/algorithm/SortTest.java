package com.jeremy.algorithm;

import org.junit.Test;

public class SortTest {

    @Test
    public void testHeapSort() {
        int[] array = new int[]{1, 2, 3, 4, 7, 8, 9, 10, 14, 16};
//        HeapSort.MaxHeap heap = new HeapSort.MaxHeap(array);
//        System.out.println("执行最大堆化前堆的结构：");
//        HeapSort.printHeapTree(heap.heap);
//        heap.BuildMaxHeap();
//        System.out.println("执行最大堆化后堆的结构：");
//        HeapSort.printHeapTree(heap.heap);
//        heap.HeapSort();
//        System.out.println("执行堆排序后数组的内容");
//        HeapSort.printHeap(heap.heap);

        int[] quickArr = new int[]{ 2, 1,3,7, 4,  8, 16, 9, 10, 14};
        print(quickArr);
        quick(quickArr);
        print(quickArr);
    }

    private void print(int[] arr){
        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println("");
    }

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
            a[high] = temp;
        }
        return low;
    }



}
