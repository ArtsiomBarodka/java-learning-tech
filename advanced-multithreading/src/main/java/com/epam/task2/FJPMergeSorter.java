package com.epam.task2;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public final class FJPMergeSorter implements MergeSorter {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    @Override
    public void sort(int[] arr) {
        System.out.printf("Array before sorting step: %s \n", Arrays.toString(arr));
        long start = System.nanoTime();
        forkJoinPool.invoke(new MergeSortRecursiveAction(arr));
        long end = System.nanoTime();
        System.out.printf("Array after sorting step: %s \n", Arrays.toString(arr));
        System.out.printf("The time of execution: %d \n", end - start);
    }
}
