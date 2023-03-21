package com.epam.task6.fibonacci;

import java.util.concurrent.ForkJoinPool;

public final class FJPFibonacciCounter implements FibonacciCounter {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    @Override
    public long count(int n) {
        long start = System.nanoTime();
        final long result = forkJoinPool.invoke(new FibonacciTask(n));
        long end = System.nanoTime();
        System.out.printf("The time of execution: %d \n", end - start);

        return result;
    }
}
