package com.jeremy.demo.algorithm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jeremy.demo.R;

public class NumAlgorithmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_algorithm);
    }

    /**
     * 不用中间变量,交换两个数字
     */
    public static void numSwap(int a, int b) {
        a = a + b;
        b = a - b;
        a = a - b;
    }

    /**
     * 二分查找非递归实现
     */
    public static int binarySearch(int[] array, int key) {
        int len = array.length;
        if (len == 0) return -1;
        int low = 0, high = len - 1, middle;
        while (low <= high) {
            middle = (low + high) / 2;
            if (array[middle] == key) {
                return middle;
            } else if (array[middle] < key) {
                low = middle;
            } else {
                high = middle;
            }
        }
        return -1;
    }

    /**
     * 递归实现二分查找
     */
    public static int binarySearchRecurse(int[] array, int key, int low, int high) {
        int len = array.length;
        if (len == 0) return -1;
        int middle = (low + high) / 2;
        while (low <= high) {
            if (array[middle] == key) {
                return middle;
            } else if (array[middle] > key) {
                return binarySearchRecurse(array, key, low, middle);
            } else {
                return binarySearchRecurse(array, key, middle, high);
            }
        }
        return -1;
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int middle = getMiddle(arr, low, high);
            quickSort(arr, low, middle - 1);
            quickSort(arr, middle + 1, high);
        }
    }

    private static int getMiddle(int[] arr, int low, int high) {
        int temp = arr[low];
        while (low < high) {
            while (low < high && arr[high] > temp) {
                high--;
            }
            arr[low] = arr[high];
            while (low < high && arr[low] < temp) {
                low++;
            }
            arr[high] = arr[low];
        }
        arr[low] = temp;
        return low;
    }


}
