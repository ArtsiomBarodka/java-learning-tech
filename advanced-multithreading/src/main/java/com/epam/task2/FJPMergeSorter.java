package com.epam.task2;

import java.util.concurrent.ForkJoinPool;

public final class FJPMergeSorter implements MergeSorter {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    @Override
    public void sort(int[] arr) {
        long start = System.nanoTime();
        forkJoinPool.invoke(new MergeSortRecursiveAction(arr));
        long end = System.nanoTime();
        System.out.printf("The time of execution: %d \n", end - start);
    }
}
