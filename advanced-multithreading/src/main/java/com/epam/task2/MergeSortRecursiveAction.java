package com.epam.task2;

import java.util.concurrent.RecursiveAction;

public class MergeSortRecursiveAction extends RecursiveAction {
    private final int[] arr;

    public MergeSortRecursiveAction(int[] arr) {
        this.arr = arr;
    }

    @Override
    protected void compute() {
        if (arr.length < 2) {
            return;
        }
        int mid = arr.length / 2;
        int[] left = new int[mid];
        int[] right = new int[arr.length - mid];

        System.arraycopy(arr, 0, left, 0, mid);
        System.arraycopy(arr, mid, right, 0, arr.length - mid);

        invokeAll(new MergeSortRecursiveAction(left), new MergeSortRecursiveAction(right));

        merge(left, right);
    }

    private void merge(int[] l, int[] r) {
        int i = 0, j = 0, k = 0;
        while (i < l.length && j < r.length) {
            if (l[i] <= r[j]) {
                arr[k++] = l[i++];
            } else {
                arr[k++] = r[j++];
            }
        }
        while (i < l.length) {
            arr[k++] = l[i++];
        }
        while (j < r.length) {
            arr[k++] = r[j++];
        }
    }
}
