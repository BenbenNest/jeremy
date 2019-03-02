package com.jeremy.algorithm;

import org.junit.Test;

public class SortTest {

    @Test
    public void testHeapSort() {
        int[] array = new int[]{1, 2, 3, 4, 7, 8, 9, 10, 14, 16};
        HeapSort.MaxHeap heap = new HeapSort.MaxHeap(array);
        System.out.println("执行最大堆化前堆的结构：");
        HeapSort.printHeapTree(heap.heap);
        heap.BuildMaxHeap();
        System.out.println("执行最大堆化后堆的结构：");
        HeapSort.printHeapTree(heap.heap);
        heap.HeapSort();
        System.out.println("执行堆排序后数组的内容");
        HeapSort.printHeap(heap.heap);
    }


}
